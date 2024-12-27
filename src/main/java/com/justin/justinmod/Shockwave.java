package com.justin.justinmod;

import com.google.common.collect.Maps;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.explosion.EntityExplosionBehavior;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.jetbrains.annotations.Nullable;

public class Shockwave {
    private static final ExplosionBehavior DEFAULT_BEHAVIOR = new ExplosionBehavior();
    private static final int field_30960 = 16;
    private final boolean createFire;
    private final net.minecraft.world.explosion.Explosion.DestructionType destructionType;
    private final Random random = Random.create();
    private final World world;
    private final double x;
    private final double y;
    private final double z;
    @Nullable
    private final Entity entity;
    private final float power;
    private final DamageSource damageSource;
    private final ExplosionBehavior behavior;
    //private final ObjectArrayList<BlockPos> affectedBlocks = new ObjectArrayList<>();
    private final Map<PlayerEntity, Vec3d> affectedPlayers = Maps.<PlayerEntity, Vec3d>newHashMap();

    public Shockwave(World world, @Nullable Entity entity, double x, double y, double z, float power, List<BlockPos> affectedBlocks) {
        this(world, entity, x, y, z, power, false, net.minecraft.world.explosion.Explosion.DestructionType.DESTROY_WITH_DECAY, affectedBlocks);
    }

    public Shockwave(
            World world,
            @Nullable Entity entity,
            double x,
            double y,
            double z,
            float power,
            boolean createFire,
            net.minecraft.world.explosion.Explosion.DestructionType destructionType,
            List<BlockPos> affectedBlocks
    ) {
        this(world, entity, x, y, z, power, createFire, destructionType);
        //this.affectedBlocks.addAll(affectedBlocks);
    }

    public Shockwave(
            World world, @Nullable Entity entity, double x, double y, double z, float power, boolean createFire, net.minecraft.world.explosion.Explosion.DestructionType destructionType
    ) {
        this(world, entity, null, null, x, y, z, power, createFire, destructionType);
    }

    public Shockwave(
            World world,
            @Nullable Entity entity,
            @Nullable DamageSource damageSource,
            @Nullable ExplosionBehavior behavior,
            double x,
            double y,
            double z,
            float power,
            boolean createFire,
            net.minecraft.world.explosion.Explosion.DestructionType destructionType
    ) {
        this.world = world;
        this.entity = entity;
        this.power = power;
        this.x = x;
        this.y = y;
        this.z = z;
        this.createFire = createFire;
        this.destructionType = destructionType;
        this.damageSource = damageSource == null ? world.getDamageSources().explosion(null, null) : damageSource;
        this.behavior = behavior == null ? this.chooseBehavior(entity) : behavior;
    }

    private ExplosionBehavior chooseBehavior(@Nullable Entity entity) {
        return (ExplosionBehavior)(entity == null ? DEFAULT_BEHAVIOR : new EntityExplosionBehavior(entity));
    }

    public static float getExposure(Vec3d source, Entity entity) {
        Box box = entity.getBoundingBox();
        double xDiff = 1.0 / ((box.maxX - box.minX) * 2.0 + 1.0);
        double yDiff = 1.0 / ((box.maxY - box.minY) * 2.0 + 1.0);
        double zDiff = 1.0 / ((box.maxZ - box.minZ) * 2.0 + 1.0);
        //JustinMod.LOGGER.info(xDiff + " " + yDiff + " " + zDiff);
        double g = (1.0 - Math.floor(1.0 / xDiff) * xDiff) / 2.0;
        double h = (1.0 - Math.floor(1.0 / zDiff) * zDiff) / 2.0;
        if (!(xDiff < 0.0) && !(yDiff < 0.0) && !(zDiff < 0.0)) {
            int i = 0;
            int j = 0;

            for (double k = 0.0; k <= 1.0; k += xDiff) {
                for (double l = 0.0; l <= 1.0; l += yDiff) {
                    for (double m = 0.0; m <= 1.0; m += zDiff) {
                        double n = MathHelper.lerp(k, box.minX, box.maxX);
                        double o = MathHelper.lerp(l, box.minY, box.maxY);
                        double p = MathHelper.lerp(m, box.minZ, box.maxZ);
                        Vec3d vec3d = new Vec3d(n + g, o, p + h);
                        if (entity.getWorld().raycast(new RaycastContext(vec3d, source, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, entity)).getType()
                                == HitResult.Type.MISS) {
                            i++;
                        }

                        j++;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public void collectBlocksAndDamageEntities() {
        this.world.emitGameEvent(this.entity, GameEvent.EXPLODE, new Vec3d(this.x, this.y, this.z));
        Set<BlockPos> set = Sets.<BlockPos>newHashSet();
        int i = 16;

        //this.affectedBlocks.addAll(set);
        float powerModifier = this.power * 2.0F;
        float horizontalAreaScalar = 4.0F;
        int x1 = MathHelper.floor(this.x - (double)(horizontalAreaScalar * powerModifier) - 1.0);
        int x2 = MathHelper.floor(this.x + (double)(horizontalAreaScalar * powerModifier) + 1.0);
        int y1 = MathHelper.floor(this.y - (double)powerModifier - 1.0);
        int y2 = MathHelper.floor(this.y + (double)powerModifier + 1.0);
        int z1 = MathHelper.floor(this.z - (double)(horizontalAreaScalar * powerModifier) - 1.0);
        int z2 = MathHelper.floor(this.z + (double)(horizontalAreaScalar * powerModifier) + 1.0);
        Box explosionBoundingBox = new Box((double)x1, (double)y1, (double)z1, (double)x2, (double)y2, (double)z2);
        //JustinMod.LOGGER.info("Explosion Entity Affect Bounding Block: " + explosionBoundingBox+"");
        List<Entity> list = this.world.getOtherEntities(this.entity, explosionBoundingBox);
        Vec3d explosionLocation = new Vec3d(this.x, this.y, this.z);
        //JustinMod.LOGGER.info("Affected Entity: " + list);
        // Affect entities
        for (int v = 0; v < list.size(); v++) {
            Entity entity = (Entity) list.get(v);
            if (!entity.isImmuneToExplosion()) {
                double distanceOverPower = Math.sqrt(entity.squaredDistanceTo(explosionLocation)) / (double)(powerModifier * horizontalAreaScalar);
                //JustinMod.LOGGER.info("Distance Over Power: " + distanceOverPower);
                if (distanceOverPower <= 1.0) {
                    double x = entity.getX() - this.x;
                    double y = (entity instanceof TntEntity ? entity.getY() : entity.getY()) - this.y;
                    double z = entity.getZ() - this.z;
                    double distanceToEntity = Math.sqrt(x * x + y * y + z * z);
                    //JustinMod.LOGGER.info("Distance to Entity: " + distanceToEntity);
                    if (distanceToEntity != 0.0) {
                        x /= distanceToEntity;
                        y /= distanceToEntity;
                        z /= distanceToEntity;
                        JustinMod.LOGGER.info(x + " " + y + " " + z);
                        float powerDropoff = (float) JustinMathHelper.sigmoid(0.5 * (this.power - 10)) * 10;
                        //powerDropoff = 1;
                        x = x != 0 ? 1 / x : 0;
                        y = y != 0 ? 1 / y : 0;
                        z = z != 0 ? 1 / z : 0;

                        double exposure = (double)getExposure(explosionLocation, entity);
                        double initialExposure = (1.0 - distanceOverPower) * exposure;
                        //float damage = (float)((int)((initialExposure * initialExposure + initialExposure) / 2.0 * 7.0 * (double)powerModifier + 1.0));
                        //entity.damage(this.getDamageSource(), damage);

                        double directionMultiplier;
                        if (entity instanceof LivingEntity livingEntity) {
                            directionMultiplier = ProtectionEnchantment.transformExplosionKnockback(livingEntity, initialExposure);
                        } else {
                            directionMultiplier = initialExposure;
                        }
                        double horizontalDirectionalScalar = horizontalAreaScalar / 2;
                        x *= directionMultiplier * horizontalDirectionalScalar * powerDropoff;
                        y *= directionMultiplier / 10 * powerDropoff;
                        z *= directionMultiplier * horizontalDirectionalScalar * powerDropoff;
                        Vec3d vec3d2 = new Vec3d(x, y, z);

                        //JustinMod.LOGGER.info("Entity " + entity + " Movement Vector: " + vec3d2+"");
                        entity.setVelocity(entity.getVelocity().add(vec3d2));
                        if (entity instanceof PlayerEntity) {
                            PlayerEntity playerEntity = (PlayerEntity)entity;
                            if (!playerEntity.isSpectator() && (!playerEntity.isCreative() || !playerEntity.getAbilities().flying)) {
                                this.affectedPlayers.put(playerEntity, vec3d2);
                            }
                        }
                    } else {
                        float shockwaveDamage = this.power;
                        entity.damage(this.getDamageSource(), shockwaveDamage);
                        //JustinMod.LOGGER.info("Damaged: " + shockwaveDamage);
                    }
                }
            }
        }
    }

    /**
     * @param particles whether this explosion should emit explosion or explosion emitter particles around the source of the explosion
     */
    public void affectWorld(boolean particles) {
        if (this.world.isClient) {
            this.world
                    .playSound(
                            this.x,
                            this.y,
                            this.z,
                            SoundEvents.ENTITY_GENERIC_EXPLODE,
                            SoundCategory.BLOCKS,
                            4.0F,
                            (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F,
                            false
                    );
        }

        boolean bl = this.shouldDestroy(); // Always false
        if (particles) {
            if (!(this.power < 2.0F) && bl) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        /*
        if (bl) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectArrayList = new ObjectArrayList<>();
            boolean bl2 = this.getCausingEntity() instanceof PlayerEntity;
            Util.shuffle(this.affectedBlocks, this.world.random);

            for (BlockPos blockPos : this.affectedBlocks) {
                BlockState blockState = this.world.getBlockState(blockPos);
                Block block = blockState.getBlock();
                if (!blockState.isAir()) {
                    BlockPos blockPos2 = blockPos.toImmutable();
                    this.world.getProfiler().push("explosion_blocks");
                    if (block.shouldDropItemsOnExplosion(this)) {
                        World blockEntity = this.world;
                        if (blockEntity instanceof ServerWorld) {
                            ServerWorld serverWorld = (ServerWorld)blockEntity;
                            BlockEntity blockEntityx = blockState.hasBlockEntity() ? this.world.getBlockEntity(blockPos) : null;
                            LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder(serverWorld)
                                    .add(LootContextParameters.ORIGIN, Vec3d.ofCenter(blockPos))
                                    .add(LootContextParameters.TOOL, ItemStack.EMPTY)
                                    .addOptional(LootContextParameters.BLOCK_ENTITY, blockEntityx)
                                    .addOptional(LootContextParameters.THIS_ENTITY, this.entity);
                            if (this.destructionType == net.minecraft.world.explosion.Explosion.DestructionType.DESTROY_WITH_DECAY) {
                                builder.add(LootContextParameters.EXPLOSION_RADIUS, this.power);
                            }

                            blockState.onStacksDropped(serverWorld, blockPos, ItemStack.EMPTY, bl2);
                            blockState.getDroppedStacks(builder).forEach(stack -> tryMergeStack(objectArrayList, stack, blockPos2));
                        }
                    }

                    this.world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
                    block.onDestroyedByExplosion(this.world, blockPos, this);
                    this.world.getProfiler().pop();
                }
            }

            for (Pair<ItemStack, BlockPos> pair : objectArrayList) {
                Block.dropStack(this.world, pair.getSecond(), pair.getFirst());
            }
        }

        if (this.createFire) {
            for (BlockPos blockPos3 : this.affectedBlocks) {
                if (this.random.nextInt(3) == 0
                        && this.world.getBlockState(blockPos3).isAir()
                        && this.world.getBlockState(blockPos3.down()).isOpaqueFullCube(this.world, blockPos3.down())) {
                    this.world.setBlockState(blockPos3, AbstractFireBlock.getState(this.world, blockPos3));
                }
            }
        }
        */

    }

    public boolean shouldDestroy() {
        return false; //this.destructionType != net.minecraft.world.explosion.Explosion.DestructionType.KEEP;
    }

    private static void tryMergeStack(ObjectArrayList<Pair<ItemStack, BlockPos>> stacks, ItemStack stack, BlockPos pos) {
        int i = stacks.size();

        for (int j = 0; j < i; j++) {
            Pair<ItemStack, BlockPos> pair = stacks.get(j);
            ItemStack itemStack = pair.getFirst();
            if (ItemEntity.canMerge(itemStack, stack)) {
                ItemStack itemStack2 = ItemEntity.merge(itemStack, stack, 16);
                stacks.set(j, Pair.of(itemStack2, pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }

        stacks.add(Pair.of(stack, pos));
    }

    public DamageSource getDamageSource() {
        return this.damageSource;
    }

    public Map<PlayerEntity, Vec3d> getAffectedPlayers() {
        return this.affectedPlayers;
    }

    @Nullable
    public LivingEntity getCausingEntity() {
        if (this.entity == null) {
            return null;
        } else if (this.entity instanceof TntEntity tntEntity) {
            return tntEntity.getOwner();
        } else {
            Entity entity = this.entity;
            if (entity instanceof LivingEntity) {
                return (LivingEntity)entity;
            } else {
                if (this.entity instanceof ProjectileEntity projectileEntity) {
                    entity = projectileEntity.getOwner();
                    if (entity instanceof LivingEntity) {
                        return (LivingEntity)entity;
                    }
                }

                return null;
            }
        }
    }

    /*
    @Nullable
    public Entity getEntity() {
        return this.entity;
    }

    public void clearAffectedBlocks() {
        this.affectedBlocks.clear();
    }

    public List<BlockPos> getAffectedBlocks() {
        return this.affectedBlocks;
    }

    public static enum DestructionType {
        KEEP,
        DESTROY,
        DESTROY_WITH_DECAY;
    }
    */

}

