package com.justin.justinmod.entity.custom;

import com.justin.justinmod.JustinMod;
import com.justin.justinmod.entity.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.Track;
import javax.xml.crypto.Data;
import java.util.UUID;

public class OrbitalLaserAuraEntity extends Entity {

    @Nullable
    private LivingEntity target;

    private static final TrackedData<Integer> TARGET_ID = DataTracker.registerData(OrbitalLaserAuraEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> BEAM_TICKS = DataTracker.registerData(OrbitalLaserAuraEntity.class, TrackedDataHandlerRegistry.INTEGER);

    private final int WARMUP_TIME = 200;

    @Nullable
    private UUID targetUuid;

    @Nullable
    private LivingEntity cachedBeamTarget;
    private double curError;

    private double prevError;

    private double totalError;

    public OrbitalLaserAuraEntity(EntityType<?> type, World world) {
        super(type, world);
        this.target = null;
        this.cachedBeamTarget = null;
        this.targetUuid = null;
        curError = 0.0;
        prevError = 0.0;
        totalError = 0.0;

    }

    public OrbitalLaserAuraEntity(World world, LivingEntity target) {
        this(ModEntities.ORBITAL_LASER_ENTITY, world);
        setBeamTarget(target);
        if (this.target != null) {
            this.setPosition(new Vec3d(this.target.getX(), this.target.getY(), this.target.getZ()));
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getWorld().isClient) {
            if (this.target == null && this.targetUuid != null) {
                Entity candidateTarget = ((ServerWorld) this.getWorld()).getEntity(this.targetUuid);
                if (candidateTarget instanceof LivingEntity && candidateTarget.isAlive()) {
                    this.target = (LivingEntity) candidateTarget;
                    this.dataTracker.set(TARGET_ID, this.target.getId());
                }

            }

            if (this.target == null || !this.target.isAlive()) {
                this.discard();
            } else {
                updateFollowVelocity();
            }
        }

        this.setPosition(this.getX() + this.getVelocity().x, this.getY() + this.getVelocity().y, this.getZ() + this.getVelocity().z);

        if (getBeamTarget() != null) {
            if (getBeamTicks() < this.getWarmupTime()) {
                this.dataTracker.set(BEAM_TICKS, getBeamTicks() + 1);
            } else if (this.getBeamTicks() >= this.getWarmupTime()) {
                this.dataTracker.set(BEAM_TICKS, 0);
                if (!this.getWorld().isClient) {
                    this.getWorld().createExplosion(this, this.getX(), this.getY() , this.getZ(), 5.0F, World.ExplosionSourceType.MOB);
                } else {
                    this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.HOSTILE, 5.0F, 1.0F, true);
                }
            }
        }
    }



    @Nullable
    public LivingEntity getBeamTarget() {
        if (dataTracker.get(TARGET_ID) == 0) {
            return null;
        } else if (this.getWorld().isClient) {
            if (this.cachedBeamTarget != null && this.cachedBeamTarget.isAlive()) {
                return this.cachedBeamTarget;
            } else {
                Entity entity = this.getWorld().getEntityById(this.dataTracker.get(TARGET_ID));
                if (entity instanceof LivingEntity && entity.isAlive()) {
                    this.cachedBeamTarget = (LivingEntity) entity;
                    return this.cachedBeamTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.target != null && this.target.isAlive() && this.target instanceof LivingEntity ? this.target : null;
        }
    }

    private void updateFollowVelocity() {
        Vec3d companionPosition = new Vec3d(this.target.getX(), this.target.getY() + 1, this.target.getZ());
        Vec3d positionErrorVector = companionPosition.subtract(this.getPos());
        curError = positionErrorVector.length();

        if (positionErrorVector.length() > 0.01) {
            double proportionalError = 0.1 * curError;
            //totalError += curError;
            double integralError  = 0 * totalError;
            double derivativeError = 0.1 * (curError - prevError);
            double pid = MathHelper.clamp(proportionalError + integralError + derivativeError, 0.0, 10.0F); // 0.25F is best
            positionErrorVector = positionErrorVector.normalize();
            positionErrorVector = positionErrorVector.multiply(pid);
            this.setVelocity(positionErrorVector);
            prevError = curError;
        } else {
            this.setVelocity(new Vec3d(0,0,0));
        }
    }

    public void setBeamTarget(LivingEntity target) {
        if (target != null && target.isAlive()) {
            this.target = target;
            this.dataTracker.set(TARGET_ID, target.getId());
            this.targetUuid = this.target.getUuid();
            this.cachedBeamTarget = target;
        }
    }

    public float getBeamProgress(float tickDelta) {
        return ((float)getBeamTicks() + tickDelta) / (float) this.getWarmupTime();
    }

    public int getWarmupTime() {
        return WARMUP_TIME;
    }

    public int getBeamTicks() {
        return this.dataTracker.get(BEAM_TICKS);
    }

    @Override
    public boolean isImmuneToExplosion() {
        return true;
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        double packetVelocityX = packet.getVelocityX();
        double packetVelocityY = packet.getVelocityY();
        double packetVelocityZ = packet.getVelocityZ();
        this.setVelocity(packetVelocityX, packetVelocityY, packetVelocityZ);
    }


    /**
     * Initializes data tracker.
     *
     * @apiNote Subclasses should override this and call {@link DataTracker#startTracking}
     * for any data that needs to be tracked.
     */
    @Override
    protected void initDataTracker() {
        if (this.target != null) {
           this.dataTracker.startTracking(TARGET_ID, this.target.getId());
        } else {
            this.dataTracker.startTracking(TARGET_ID, 0);
        }
        this.dataTracker.startTracking(BEAM_TICKS, 0);
    }

    /**
     * Called on the client when the tracked data is set.
     *
     * <p>This can be overridden to refresh other fields when the tracked data
     * is set or changed.
     */
    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (TARGET_ID.equals(data)) {
            this.cachedBeamTarget = null;
            this.dataTracker.set(BEAM_TICKS, 0);
        }
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
        if (nbt.contains("target")) {
            this.targetUuid = nbt.getUuid("target");
        }

        if (nbt.contains("beam_ticks")) {
            this.dataTracker.set(BEAM_TICKS, nbt.getInt("beam_ticks"));
            JustinMod.LOGGER.info("Beam Ticks: " + this.getBeamTicks() + " " + this.getUuidAsString());
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
        if (this.target != null && this.target.isAlive()) {
            nbt.putUuid("target", this.target.getUuid());
        }

        nbt.putInt("beam_ticks", this.getBeamTicks());
    }
}
