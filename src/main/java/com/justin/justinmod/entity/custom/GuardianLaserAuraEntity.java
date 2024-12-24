package com.justin.justinmod.entity.custom;

import com.justin.justinmod.ExampleItem;
import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.ModEntities;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.GuardianEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class GuardianLaserAuraEntity extends Entity {

    private final double RADIUS = 0.5;
    private final double LINEAR_SPEED = 0.1;
    private final double OMEGA = LINEAR_SPEED / RADIUS;

    private static final TrackedData<Integer> BEAM_TARGET_ID = DataTracker.registerData(GuardianLaserAuraEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private int beamTicks;
    protected static final int WARMUP_TIME = 8;

    private LivingEntity cachedBeamTarget;


    @Nullable
    private LivingEntity companion;

    @Nullable
    private UUID companionUuid;
    private LivingEntity target;


    public GuardianLaserAuraEntity(EntityType<?> type, World world) {
        super(type, world);
        this.companion = null;

        //this.setPosition(playerCompanion.getX() + RADIUS, playerCompanion.getY() + playerCompanion.getStandingEyeHeight() + 1, playerCompanion.getZ() + RADIUS);
        //this.setVelocity(-1 * RADIUS * OMEGA * org.joml.Math.sin(OMEGA * playerCompanion.age), 0, -1 * RADIUS * OMEGA * org.joml.Math.cos(OMEGA * playerCompanion.age));

    }

    public GuardianLaserAuraEntity(World world, LivingEntity player) {
        this(ModEntities.GUARDIAN_LASER_AURA, world);
        this.companion = player;

        this.setPosition(companion.getX(), companion.getY() + companion.getStandingEyeHeight() + .5F, companion.getZ());
        this.setVelocity(-1 * RADIUS * OMEGA * org.joml.Math.sin(OMEGA * this.age), 0, -1 * RADIUS * OMEGA * org.joml.Math.cos(OMEGA * this.age));
    }



    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient()) {
            if (this.companion == null && this.companionUuid != null) {
                Entity candidateCompanion = ((ServerWorld) this.getWorld()).getEntity(this.companionUuid);
                if (candidateCompanion instanceof LivingEntity) {
                    this.companion = (LivingEntity) candidateCompanion;
                }
                if (this.companion == null) {
                    this.companionUuid = null;
                }
            }
            if (companion == null || !companion.isAlive() /*|| (companion instanceof PlayerEntity && ((PlayerEntity) companion).isUsingItem())*/) {
                JustinMod.LOGGER.info("Killing because Player Companion is null");
                this.kill();
            } else {
                this.setPosition(this.companion.getX()  + this.getVelocity().x, companion.getY() + companion.getStandingEyeHeight() + .5F, this.companion.getZ() + this.getVelocity().z);
                this.setVelocity(-1 * RADIUS * OMEGA * org.joml.Math.sin(OMEGA * this.age), 0, -1 * RADIUS * OMEGA * org.joml.Math.cos(OMEGA * this.age));
            }
        }

        if (getBeamTarget() != null) {
            if (this.beamTicks < this.getWarmupTime()) {
                this.beamTicks++;
            } else if (this.beamTicks >= this.getWarmupTime()) {
                this.beamTicks = 0;
                getBeamTarget().damage(this.getDamageSources().indirectMagic(this, this), 5);
                if (!this.getWorld().isClient()) {
                    Explosion explosion = this.getWorld().createExplosion(this,
                            getBeamTarget().getX(), getBeamTarget().getY() + (getBeamTarget().getHeight() * 0.5), getBeamTarget().getZ(), 5.0F, World.ExplosionSourceType.MOB);

                }
            }

//            LivingEntity livingEntity = this.getBeamTarget();
//            if (livingEntity != null && livingEntity.isAlive()) {
//                double d = (double)this.getBeamProgress(0.0F);
//                double e = livingEntity.getX() - this.getX();
//                double f = livingEntity.getBodyY(0.5) - this.getEyeY();
//                double g = livingEntity.getZ() - this.getZ();
//                double h = Math.sqrt(e * e + f * f + g * g);
//                e /= h;
//                f /= h;
//                g /= h;
//                double j = this.random.nextDouble();
//
//                while (j < h) {
//                    j += 1.8 - d + this.random.nextDouble() * (1.7 - d);
//                    this.getWorld().addParticle(ParticleTypes.BUBBLE, this.getX() + e * j, this.getEyeY() + f * j, this.getZ() + g * j, 0.0, 0.0, 0.0);
//                }
//            }
        }

    }

    public LivingEntity getCompanion() {
        return companion;
    }

    public int getWarmupTime() {
        return WARMUP_TIME;
    }

    public float getBeamTicks() {
        return (float)this.beamTicks;
    }

    public boolean hasBeamTarget() {
        return this.dataTracker.get(BEAM_TARGET_ID) != 0;
    }

    @Nullable
    public LivingEntity getBeamTarget() {
        if (!this.hasBeamTarget()) {
            return null;
        } else if (this.getWorld().isClient) {
            if (this.cachedBeamTarget != null) {
                return this.cachedBeamTarget;
            } else {
                Entity entity = this.getWorld().getEntityById(this.dataTracker.get(BEAM_TARGET_ID));
                if (entity instanceof LivingEntity) {
                    this.cachedBeamTarget = (LivingEntity)entity;
                    return this.cachedBeamTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    public LivingEntity getTarget() {
        return this.target;
    }



    public float getBeamProgress(float tickDelta) {
        return ((float)this.beamTicks + tickDelta) / (float)this.getWarmupTime();
    }

    /**
     * Initializes data tracker.
     *
     * @apiNote Subclasses should override this and call {@link DataTracker#startTracking}
     * for any data that needs to be tracked.
     */
    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(BEAM_TARGET_ID, 0);
    }

    public void setBeamTarget(int entityId) {
        Entity candidateEntity = this.getWorld().getEntityById(entityId);
        if (!(candidateEntity instanceof LivingEntity))
            return;
        LivingEntity candidateLivingEntity = (LivingEntity) candidateEntity;
        if (!candidateLivingEntity.isAlive())
            return;
        this.target = candidateLivingEntity;
        this.dataTracker.set(BEAM_TARGET_ID, entityId);
    }



    /**
     * Reads custom data from {@code nbt}. Subclasses has to implement this.
     *
     * <p>NBT is a storage format; therefore, a data from NBT is loaded to an entity instance's
     * fields, which are used for other operations instead of the NBT. The data is written
     * back to NBT when saving the entity.
     *
     * <p>{@code nbt} might not have all expected keys, or might have a key whose value
     * does not meet the requirement (such as the type or the range). This method should
     * fall back to a reasonable default value instead of throwing an exception.
     *
     * @param nbt
     * @see #writeCustomDataToNbt
     */
    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Companion")) {
            this.companionUuid = nbt.getUuid("Companion");
        }


    }

    /**
     * Writes custom data to {@code nbt}. Subclasses has to implement this.
     *
     * <p>NBT is a storage format; therefore, a data from NBT is loaded to an entity instance's
     * fields, which are used for other operations instead of the NBT. The data is written
     * back to NBT when saving the entity.
     *
     * @param nbt
     * @see #readCustomDataFromNbt
     */
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.companion != null) {
            nbt.putUuid("Companion", this.companion.getUuid());
        }


    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (BEAM_TARGET_ID.equals(data)) {
            this.beamTicks = 0;
            this.cachedBeamTarget = null;
        }
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        double d = packet.getVelocityX();
        double e = packet.getVelocityY();
        double f = packet.getVelocityZ();
        this.setVelocity(d,e,f);
    }
}
