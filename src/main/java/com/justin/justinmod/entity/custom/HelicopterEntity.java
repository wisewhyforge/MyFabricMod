package com.justin.justinmod.entity.custom;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.justin.justinmod.JustinMod;
import net.minecraft.block.BlockState;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.BoatPaddleStateC2SPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HelicopterEntity extends Entity {

    private static final ImmutableMap<EntityPose, ImmutableList<Integer>> DISMOUNT_FREE_Y_SPACES_NEEDED = ImmutableMap.of(
            EntityPose.STANDING, ImmutableList.of(0, 1, -1), EntityPose.CROUCHING, ImmutableList.of(0, 1, -1), EntityPose.SWIMMING, ImmutableList.of(0, 1)
    );

    private int field_7708;
    private double x;
    private double y;
    private double z;

    private float velocityDecay;
    private float ticksUnderwater;
    private float yawVelocity;

    private float nearbySlipperiness;
    private HelicopterEntity.Location location;
    private HelicopterEntity.Location lastLocation;

    private double heloYaw;
    private double heloPitch;

    private double waterLevel;

    private double fallVelocity;
    private boolean pressingLeft;
    private boolean pressingForward;
    private boolean pressingRight;
    private boolean pressingBack;
    private boolean pressingUp;
    private boolean pressingDown;
    private boolean hovering;
    private double clientXVelocity;
    private double clientYVelocity;
    private double clientZVelocity;


    public HelicopterEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean collidesWith(Entity other) {
        return (other.isCollidable() || other.isPushable()) && !this.isConnectedThroughVehicle(other);
    }

    @Nullable
    private HelicopterEntity.Location getUnderWaterLocation() {
        Box box = this.getBoundingBox();
        double d = box.maxY + 0.001;
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(d);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER) && d < (double)((float)mutable.getY() + fluidState.getHeight(this.getWorld(), mutable))) {
                        if (!fluidState.isStill()) {
                            return HelicopterEntity.Location.UNDER_FLOWING_WATER;
                        }

                        bl = true;
                    }
                }
            }
        }

        return bl ? HelicopterEntity.Location.UNDER_WATER : null;
    }

    public float getNearbySlipperiness() {
        Box box = this.getBoundingBox();
        Box box2 = new Box(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = MathHelper.floor(box2.minX) - 1;
        int j = MathHelper.ceil(box2.maxX) + 1;
        int k = MathHelper.floor(box2.minY) - 1;
        int l = MathHelper.ceil(box2.maxY) + 1;
        int m = MathHelper.floor(box2.minZ) - 1;
        int n = MathHelper.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = VoxelShapes.cuboid(box2);
        float f = 0.0F;
        int o = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int p = i; p < j; p++) {
            for (int q = m; q < n; q++) {
                int r = (p != i && p != j - 1 ? 0 : 1) + (q != m && q != n - 1 ? 0 : 1);
                if (r != 2) {
                    for (int s = k; s < l; s++) {
                        if (r <= 0 || s != k && s != l - 1) {
                            mutable.set(p, s, q);
                            BlockState blockState = this.getWorld().getBlockState(mutable);
                            if (!(blockState.getBlock() instanceof LilyPadBlock)
                                    && VoxelShapes.matchesAnywhere(
                                    blockState.getCollisionShape(this.getWorld(), mutable).offset((double)p, (double)s, (double)q), voxelShape, BooleanBiFunction.AND
                            )) {
                                f += blockState.getBlock().getSlipperiness();
                                o++;
                            }
                        }
                    }
                }
            }
        }

        return f / (float)o;
    }

    private boolean checkHelicopterInWater() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.minY + 0.001);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        this.waterLevel = -Double.MAX_VALUE;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int o = i; o < j; o++) {
            for (int p = k; p < l; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        float f = (float)p + fluidState.getHeight(this.getWorld(), mutable);
                        this.waterLevel = Math.max((double)f, this.waterLevel);
                        bl |= box.minY < (double)f;
                    }
                }
            }
        }

        return bl;
    }

    private HelicopterEntity.Location checkLocation() {
        HelicopterEntity.Location location = this.getUnderWaterLocation();
        if (location != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return location;
        } else if (this.checkHelicopterInWater()) {
            return HelicopterEntity.Location.IN_WATER;
        } else {
            float f = this.getNearbySlipperiness();
            if (f > 0.0F) {
                this.nearbySlipperiness = f;
                return HelicopterEntity.Location.ON_LAND;
            } else {
                return HelicopterEntity.Location.IN_AIR;
            }
        }
    }

    public boolean isSmallerThanHelicopter(Entity entity) {
        return entity.getWidth() < this.getWidth();
    }

    protected int getMaxPassengers() {
        return 1;
    }


    @Override
    public void tick() {
        this.lastLocation = this.location;
        this.location = this.checkLocation();
        if (this.location != HelicopterEntity.Location.UNDER_WATER && this.location != HelicopterEntity.Location.UNDER_FLOWING_WATER) {
            this.ticksUnderwater = 0.0F;
        } else {
            this.ticksUnderwater++;
        }

        if (!this.getWorld().isClient && this.ticksUnderwater >= 60.0F) {
            this.removeAllPassengers();
        }



        super.tick();
        this.updatePositionAndRotation();
        if (this.isLogicalSideForUpdatingMovement()) {

            this.updateVelocity();

            if (this.getWorld().isClient) {
                this.updateHelicopterFromInput();
            }



            this.move(MovementType.SELF, this.getVelocity());
        } else {
            //this.setVelocity(this.getVelocity().x, this.getVelocity().y, this.getVelocity().z);
            //this.updateVelocity();
        }


        this.checkBlockCollision();
        List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.2F, -0.01F, 0.2F), EntityPredicates.canBePushedBy(this));
        if (!list.isEmpty()) {
            boolean bl = !this.getWorld().isClient && !(this.getControllingPassenger() instanceof PlayerEntity);

            for (int j = 0; j < list.size(); j++) {
                Entity entity = (Entity)list.get(j);
                if (!entity.hasPassenger(this)) {
                    if (bl
                            && this.getPassengerList().size() < this.getMaxPassengers()
                            && !entity.hasVehicle()
                            && this.isSmallerThanHelicopter(entity)
                            && entity instanceof LivingEntity
                            && !(entity instanceof WaterCreatureEntity)
                            && !(entity instanceof PlayerEntity)) {
                        entity.startRiding(this);
                    } else {
                        this.pushAwayFrom(entity);
                    }
                }
            }
        }
    }

    public void setInputs(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack, boolean pressingUp) {
        this.pressingLeft = pressingLeft;
        this.pressingRight = pressingRight;
        this.pressingForward = pressingForward;
        this.pressingBack = pressingBack;
        this.pressingUp = pressingUp;
        //JustinMod.LOGGER.info("Left: " + pressingLeft + " Right: " + pressingRight + " Forward: " + pressingForward + " Backwards: " + pressingBack);
    }

    public void setDownInput(boolean pressingDown) {
        this.pressingDown = pressingDown;
    }

    public float getWaterHeightBelow() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(box.maxY - this.fallVelocity);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        label39:
        for (int o = k; o < l; o++) {
            float f = 0.0F;

            for (int p = i; p < j; p++) {
                for (int q = m; q < n; q++) {
                    mutable.set(p, o, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        f = Math.max(f, fluidState.getHeight(this.getWorld(), mutable));
                    }

                    if (f >= 1.0F) {
                        continue label39;
                    }
                }
            }

            if (f < 1.0F) {
                return (float)mutable.getY() + f;
            }
        }

        return (float)(l + 1);
    }

    private void updateVelocity() {

        double d = -0.04F;
        double e = this.hasNoGravity() ? 0.0 : -0.04F;
        double f = 0.0;
        this.velocityDecay = 0.05F;
        if (this.lastLocation == HelicopterEntity.Location.IN_AIR && this.location != HelicopterEntity.Location.IN_AIR && this.location != HelicopterEntity.Location.ON_LAND) {
            this.waterLevel = this.getBodyY(1.0);
            this.setPosition(this.getX(), (double)(this.getWaterHeightBelow() - this.getHeight()) + 0.101, this.getZ());
            this.setVelocity(this.getVelocity().multiply(1.0, 0.0, 1.0));
            this.fallVelocity = 0.0;
            this.location = HelicopterEntity.Location.IN_WATER;
        } else {
            if (this.location == HelicopterEntity.Location.IN_WATER) {
                f = (this.waterLevel - this.getY()) / (double)this.getHeight();
                this.velocityDecay = 0.9F;
            } else if (this.location == HelicopterEntity.Location.UNDER_FLOWING_WATER) {
                e = -7.0E-4;
                this.velocityDecay = 0.9F;
            } else if (this.location == HelicopterEntity.Location.UNDER_WATER) {
                f = 0.01F;
                this.velocityDecay = 0.45F;
            } else if (this.location == HelicopterEntity.Location.IN_AIR) {
                this.velocityDecay = 0.95F;
            } else if (this.location == HelicopterEntity.Location.ON_LAND) {
                this.velocityDecay = this.nearbySlipperiness;
                this.velocityDecay = 0.0F;
                if (this.getControllingPassenger() instanceof PlayerEntity) {
                    this.nearbySlipperiness /= 2.0F;
                }
            }

            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x * (double)this.velocityDecay, vec3d.y + e, vec3d.z * (double)this.velocityDecay);
            if (this.location  == Location.IN_AIR) {
                this.yawVelocity = this.yawVelocity * 0.7F;
            } else {
                this.yawVelocity = this.yawVelocity * this.velocityDecay;
            }
            if (f > 0.0) {
                Vec3d vec3d2 = this.getVelocity();
                this.setVelocity(vec3d2.x, (vec3d2.y + f * 0.06153846016296973) * 0.75, vec3d2.z);
            }
        }


    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Direction direction = this.getMovementDirection();
        if (direction.getAxis() == Direction.Axis.Y) {
            return super.updatePassengerForDismount(passenger);
        } else {
            int[][] is = Dismounting.getDismountOffsets(direction);
            BlockPos blockPos = this.getBlockPos();
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            ImmutableList<EntityPose> immutableList = passenger.getPoses();

            for (EntityPose entityPose : immutableList) {
                EntityDimensions entityDimensions = passenger.getDimensions(entityPose);
                float f = Math.min(entityDimensions.width, 1.0F) / 2.0F;

                for (int i : DISMOUNT_FREE_Y_SPACES_NEEDED.get(entityPose)) {
                    for (int[] js : is) {
                        mutable.set(blockPos.getX() + js[0], blockPos.getY() + i, blockPos.getZ() + js[1]);
                        double d = this.getWorld()
                                .getDismountHeight(Dismounting.getCollisionShape(this.getWorld(), mutable), () -> Dismounting.getCollisionShape(this.getWorld(), mutable.down()));
                        if (Dismounting.canDismountInBlock(d)) {
                            Box box = new Box((double)(-f), 0.0, (double)(-f), (double)f, (double)entityDimensions.height, (double)f);
                            Vec3d vec3d = Vec3d.ofCenter(mutable, d);
                            if (Dismounting.canPlaceEntityAt(this.getWorld(), passenger, box.offset(vec3d))) {
                                passenger.setPose(entityPose);
                                return vec3d;
                            }
                        }
                    }
                }
            }

            double e = this.getBoundingBox().maxY;
            mutable.set((double)blockPos.getX(), e, (double)blockPos.getZ());

            for (EntityPose entityPose2 : immutableList) {
                double g = (double)passenger.getDimensions(entityPose2).height;
                int j = MathHelper.ceil(e - (double)mutable.getY() + g);
                double h = Dismounting.getCeilingHeight(mutable, j, pos -> this.getWorld().getBlockState(pos).getCollisionShape(this.getWorld(), pos));
                if (e + g <= h) {
                    passenger.setPose(entityPose2);
                    break;
                }
            }

            return super.updatePassengerForDismount(passenger);
        }
    }

    private void updateHelicopterFromInput() {
        if (this.hasPassengers()) {
            float f = 0.0F;
            float upwardsSpeed = 0.0F;
            if (this.pressingLeft) {
                this.yawVelocity--;
            }

            if (this.pressingRight) {
                this.yawVelocity++;
            }

            if (this.pressingRight != this.pressingLeft && !this.pressingForward && !this.pressingBack) {
                f += 0.005F;
            }

            this.setYaw(this.getYaw() + this.yawVelocity);
            if (this.pressingForward) {
                f += 0.4F;
            }

            if (this.pressingBack) {
                f -= 0.4F;
            }

            if (this.pressingUp) {
                upwardsSpeed += 0.1F;
            }

            this.setVelocity(
                    this.getVelocity()
                            .add(
                                    (double)(MathHelper.sin(-this.getYaw() * (float) (Math.PI / 180.0)) * f), upwardsSpeed, (double)(MathHelper.cos(this.getYaw() * (float) (Math.PI / 180.0)) * f)
                            )
            );
        }
    }

    private void updatePositionAndRotation() {
        if (this.isLogicalSideForUpdatingMovement()) {
            this.field_7708 = 0;
            this.updateTrackedPosition(this.getX(), this.getY(), this.getZ());

        }

        if (this.field_7708 > 0) {
            double d = this.getX() + (this.x - this.getX()) / (double)this.field_7708;
            double e = this.getY() + (this.y - this.getY()) / (double)this.field_7708;
            double f = this.getZ() + (this.z - this.getZ()) / (double)this.field_7708;
            double g = MathHelper.wrapDegrees(this.heloYaw - (double)this.getYaw());
            this.setYaw(this.getYaw() + (float)g / (float)this.field_7708);
            this.setPitch(this.getPitch() + (float)(this.heloPitch - (double)this.getPitch()) / (float)this.field_7708);
            this.field_7708--;
            this.setPosition(d, e, f);
            this.setRotation(this.getYaw(), this.getPitch());
        }
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        if (!this.pressingUp) {
            this.fallVelocity = this.getVelocity().y;
            if (!this.hasVehicle()) {
                if (onGround) {
                    if (this.fallDistance > 3.0F) {
                        if (this.location != HelicopterEntity.Location.ON_LAND) {
                            this.onLanding();
                            return;
                        }
                    }

                    this.onLanding();
                } else if (!this.getWorld().getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0) {
                    this.fallDistance -= (float) heightDifference;
                }
            }
        }
    }

    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    @Override
    public boolean canHit() {
        return !this.isRemoved();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof LivingEntity livingEntity ? livingEntity : null;
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        JustinMod.LOGGER.info("Initial Interaction");
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else if (this.ticksUnderwater < 60.0F) {
            if (!this.getWorld().isClient) {
                boolean isRiding = player.startRiding(this);
                //JustinMod.LOGGER.info("Riding into helicopter result: " + isRiding + " Player Vehicle: " + player.getControllingVehicle());
                return isRiding ? ActionResult.CONSUME : ActionResult.PASS;
            } else {
                return ActionResult.SUCCESS;
            }
        } else {
            return ActionResult.PASS;
        }
    }

    public void setPressingDown(boolean pressingDown) {
        this.pressingDown = pressingDown;
    }

    public boolean getPressingDown() {
        return this.pressingDown;
    }

    @Override
    public void setVelocityClient(double x, double y, double z) {
        this.clientXVelocity = x;
        this.clientYVelocity = y;
        this.clientZVelocity = z;
        this.setVelocity(this.clientXVelocity, this.clientYVelocity, this.clientZVelocity);
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.heloYaw = (double)yaw;
        this.heloPitch = (double)pitch;
        this.field_7708 = 10;
        this.setVelocity(this.clientXVelocity, this.clientYVelocity, this.clientZVelocity);
    }

    public static enum Location {
        IN_WATER,
        UNDER_WATER,
        UNDER_FLOWING_WATER,
        ON_LAND,
        IN_AIR;
    }

    /**
     * Initializes data tracker.
     *
     * @apiNote Subclasses should override this and call {@link DataTracker#startTracking}
     * for any data that needs to be tracked.
     */
    @Override
    protected void initDataTracker() {

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

    }
}
