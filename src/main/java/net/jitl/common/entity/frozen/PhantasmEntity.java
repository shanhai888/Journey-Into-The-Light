package net.jitl.common.entity.frozen;

import net.jitl.JITL;
import net.jitl.init.JParticleManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Random;

public class PhantasmEntity extends MobEntity implements IMob {
    public float targetSquish;
    public float squish;
    public float oSquish;
    private boolean wasOnGround;

    public PhantasmEntity(EntityType<? extends PhantasmEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new PhantasmEntity.MoveHelperController(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PhantasmEntity.FloatGoal(this));
        this.goalSelector.addGoal(2, new PhantasmEntity.AttackGoal(this));
        this.goalSelector.addGoal(3, new PhantasmEntity.FaceRandomGoal(this));
        this.goalSelector.addGoal(5, new PhantasmEntity.HopGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, (livingEntity_) -> {
            return Math.abs(livingEntity_.getY() - this.getY()) <= 4.0D;
        }));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("wasOnGround", this.wasOnGround);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public static boolean canSpawn(EntityType<? extends MobEntity> entityType, IWorld worldIn, SpawnReason reason, BlockPos pos, Random random) {
        return !worldIn.getBlockState(pos).is(Blocks.WATER)
                || Objects.equals(worldIn.getBiome(pos).getRegistryName(), JITL.rl("frozen_wastes"))
                || Objects.equals(worldIn.getBiome(pos).getRegistryName(), JITL.rl("dying_forest"))
                || Objects.equals(worldIn.getBiome(pos).getRegistryName(), JITL.rl("bitterwood_forest"));
    }

    protected IParticleData getParticleType() {
        return JParticleManager.MINERS_PEARL.get();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        this.squish += (this.targetSquish - this.squish) * 0.5F;
        this.oSquish = this.squish;
        super.tick();
        if (this.onGround && !this.wasOnGround) {
            int i = 1;

            if (spawnCustomParticles()) i = 0; // don't spawn particles if it's handled by the implementation itself
            for (int j = 0; j < i * 8; ++j) {
                float f = this.random.nextFloat() * ((float) Math.PI * 2F);
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.sin(f) * (float) i * 0.5F * f1;
                float f3 = MathHelper.cos(f) * (float) i * 0.5F * f1;
                this.level.addParticle(this.getParticleType(), this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            this.targetSquish = -0.5F;
        } else if (!this.onGround && this.wasOnGround) {
            this.targetSquish = 1.0F;
        }

        this.wasOnGround = this.onGround;
        this.decreaseSquish();
    }

    protected void decreaseSquish() {
        this.targetSquish *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay() {
        return this.random.nextInt(20) + 10;
    }

    public void refreshDimensions() {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        super.refreshDimensions();
        this.setPos(d0, d1, d2);
    }

    public EntityType<? extends PhantasmEntity> getType() {
        return (EntityType<? extends PhantasmEntity>) super.getType();
    }

    @Override
    public void remove(boolean keepData) {
        int i = 1;
        if (!this.level.isClientSide && i > 1 && this.isDeadOrDying() && !this.removed) {
            ITextComponent itextcomponent = this.getCustomName();
            boolean flag = this.isNoAi();
            float f = (float) i / 4.0F;
            int j = i / 2;
            int k = 2 + this.random.nextInt(3);

            for (int l = 0; l < k; ++l) {
                float f1 = ((float) (l % 2) - 0.5F) * f;
                float f2 = ((float) (l / 2) - 0.5F) * f;
                PhantasmEntity PhantasmEntity = this.getType().create(this.level);
                if (this.isPersistenceRequired()) {
                    PhantasmEntity.setPersistenceRequired();
                }

                PhantasmEntity.setCustomName(itextcomponent);
                PhantasmEntity.setNoAi(flag);
                PhantasmEntity.setInvulnerable(this.isInvulnerable());
                PhantasmEntity.moveTo(this.getX() + (double) f1, this.getY() + 0.5D, this.getZ() + (double) f2, this.random.nextFloat() * 360.0F, 0.0F);
                this.level.addFreshEntity(PhantasmEntity);
            }
        }

        super.remove(keepData);
    }

    /**
     * Applies a velocity to the entities, to push them away from eachother.
     */
    public void push(Entity entityIn) {
        super.push(entityIn);
        if (entityIn instanceof IronGolemEntity && this.isDealsDamage()) {
            this.dealDamage((LivingEntity) entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void playerTouch(PlayerEntity entityIn) {
        if (this.isDealsDamage()) {
            this.dealDamage(entityIn);
        }
    }

    protected void dealDamage(LivingEntity entityIn) {
        if (this.isAlive()) {
            int i = 1;
            if (this.distanceToSqr(entityIn) < 0.6D * (double) i * 0.6D * (double) i && this.canSee(entityIn) && entityIn.hurt(DamageSource.mobAttack(this), this.getAttackDamage())) {
                this.playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
                this.doEnchantDamageEffects(this, entityIn);
            }
        }

    }

    protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
        return 0.625F * sizeIn.height;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    protected float getAttackDamage() {
        return (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.SLIME_HURT_SMALL;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH_SMALL;
    }

    protected SoundEvent getSquishSound() {
        return SoundEvents.SLIME_SQUISH_SMALL;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume() {
        return 0.4F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getMaxHeadXRot() {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    protected boolean doPlayJumpSound() {
        return true;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jumpFromGround() {
        Vector3d vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.x, this.getJumpPower(), vector3d.z);
        this.hasImpulse = true;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.SLIME_JUMP_SMALL;
    }

    /**
     * Called when the slime spawns particles on landing, see onUpdate.
     * Return true to prevent the spawning of the default particles.
     */
    protected boolean spawnCustomParticles() {
        return false;
    }

    static class AttackGoal extends Goal {
        private final PhantasmEntity slime;
        private int growTiredTimer;

        public AttackGoal(PhantasmEntity slimeIn) {
            this.slime = slimeIn;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else {
                return (!(livingentity instanceof PlayerEntity) || !((PlayerEntity) livingentity).abilities.invulnerable) && this.slime.getMoveControl() instanceof MoveHelperController;
            }
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void start() {
            this.growTiredTimer = 300;
            super.start();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean canContinueToUse() {
            LivingEntity livingentity = this.slime.getTarget();
            if (livingentity == null) {
                return false;
            } else if (!livingentity.isAlive()) {
                return false;
            } else if (livingentity instanceof PlayerEntity && ((PlayerEntity) livingentity).abilities.invulnerable) {
                return false;
            } else {
                return --this.growTiredTimer > 0;
            }
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            this.slime.lookAt(Objects.requireNonNull(this.slime.getTarget()), 10.0F, 10.0F);
            ((PhantasmEntity.MoveHelperController) this.slime.getMoveControl()).setDirection(this.slime.yRot, this.slime.isDealsDamage());
        }
    }

    static class FaceRandomGoal extends Goal {
        private final PhantasmEntity slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public FaceRandomGoal(PhantasmEntity slimeIn) {
            this.slime = slimeIn;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return this.slime.getTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(Effects.LEVITATION)) && this.slime.getMoveControl() instanceof PhantasmEntity.MoveHelperController;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRandom().nextInt(60);
                this.chosenDegrees = (float) this.slime.getRandom().nextInt(360);
            }

            ((PhantasmEntity.MoveHelperController) this.slime.getMoveControl()).setDirection(this.chosenDegrees, false);
        }
    }

    static class FloatGoal extends Goal {
        private final PhantasmEntity slime;

        public FloatGoal(PhantasmEntity slimeIn) {
            this.slime = slimeIn;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
            slimeIn.getNavigation().setCanFloat(true);
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof PhantasmEntity.MoveHelperController;
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            ((PhantasmEntity.MoveHelperController) this.slime.getMoveControl()).setWantedMovement(1.2D);
        }
    }

    static class HopGoal extends Goal {
        private final PhantasmEntity slime;

        public HopGoal(PhantasmEntity slimeIn) {
            this.slime = slimeIn;
            this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            ((PhantasmEntity.MoveHelperController) this.slime.getMoveControl()).setWantedMovement(1.0D);
        }
    }

    static class MoveHelperController extends MovementController {
        private float yRot;
        private int jumpDelay;
        private final PhantasmEntity slime;
        private boolean isAggressive;

        public MoveHelperController(PhantasmEntity slimeIn) {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = 180.0F * slimeIn.yRot / (float) Math.PI;
        }

        public void setDirection(float yRotIn, boolean aggressive) {
            this.yRot = yRotIn;
            this.isAggressive = aggressive;
        }

        public void setWantedMovement(double speedIn) {
            this.speedModifier = speedIn;
            this.operation = MovementController.Action.MOVE_TO;
        }

        public void tick() {
            this.mob.yRot = this.rotlerp(this.mob.yRot, this.yRot, 90.0F);
            this.mob.yHeadRot = this.mob.yRot;
            this.mob.yBodyRot = this.mob.yRot;
            if (this.operation != MovementController.Action.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = MovementController.Action.WAIT;
                if (this.mob.isOnGround()) {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }

                        this.slime.getJumpControl().jump();
                        if (this.slime.doPlayJumpSound()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), 1.0F);
                        }
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    this.mob.setSpeed((float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }

            }
        }
    }
}
