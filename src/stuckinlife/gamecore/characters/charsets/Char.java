package stuckinlife.gamecore.characters.charsets;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.animation.AnimationRenderer;
import stuckinlife.gamecore.characters.charsets.attack.AttackManager;
import stuckinlife.gamecore.characters.charsets.attack.MeleeAttackManager;
import stuckinlife.gamecore.characters.charsets.attack.PureAttackManager;
import stuckinlife.gamecore.characters.charsets.attack.RangedAttackManager;
import stuckinlife.gamecore.characters.controller.CharacterController;
import stuckinlife.gamecore.characters.defaults.CharInfo;
import stuckinlife.gamecore.gameutils.Animation;
import stuckinlife.gamecore.gameutils.AnimationLoader;
import stuckinlife.gamecore.supermove.SuperMove;
import stuckinlife.gamecore.weapon.Weapon;
import utils.Utils;

import java.util.HashMap;
import java.util.Map;

public abstract class Char {

    protected final World WORLD;

    protected final double MAX_HEALTH;
    protected final int MAX_SUPER_CHARGE;
    protected final int TEAM_CODE;
    protected final int ATTACK_COOLDOWN;
    protected final double DEFLECTION_CHANCE;
    protected final double STAMINA;
    protected final double STAMINA_REGEN;
    protected final AttackManager ATTACK_MANAGER;

    protected final String CHARACTER_NAME;

    protected double regen;
    protected double armor;
    protected double moveSpeed;
    protected double deflectionChance;


    protected double x;
    protected double y;
    protected int orientation;
    protected double health;
    protected int attackCooldown;
    protected double stamina;
    protected double superCharge;

    protected final CharacterController CONTROLLER;

    protected final Weapon WEAPON;
    protected final SuperMove SUPER_MOVE;

    private final Map<String, Animation> animationMap = new HashMap<>();

    String state = "Idle Down";
    String lastState = state;

    Animation currentAnimation;

    public Char(World world,
                double x,
                double y,
                int orientation,
                CharInfo charInfo,
                int teamCode,
                CharacterController controller,
                Weapon weapon,
                SuperMove superMove) {

        WORLD = world;
        MAX_HEALTH = charInfo.getHEALTH();
        regen = charInfo.getREGEN();
        armor = charInfo.getARMOR();
        moveSpeed = charInfo.getMOVE_SPEED();
        ATTACK_COOLDOWN = charInfo.getATTACK_COOLDOWN();
        MAX_SUPER_CHARGE = charInfo.getSUPER_CHARGE();
        deflectionChance = charInfo.getDEFLECT_CHANCE();
        DEFLECTION_CHANCE = deflectionChance;
        STAMINA = charInfo.getSTAMINA();
        stamina = STAMINA;
        STAMINA_REGEN = charInfo.getSTAMINA_REGEN();
        CHARACTER_NAME = charInfo.getCHAR_NAME();
        CONTROLLER = controller;

        CharacterType characterType = charInfo.getCHARACTER_TYPE();
        switch (characterType) {
            case WEAPONIZED:
                ATTACK_MANAGER = new MeleeAttackManager(this);
                break;
            case RANGED:
                ATTACK_MANAGER = new RangedAttackManager(this);
                break;
            case PURE:
                ATTACK_MANAGER = new PureAttackManager(this);
                break;
            default:
                throw new RuntimeException("\"Dood forgot to add the new character type to the switch\" Exception");
        }

        WEAPON = weapon;
        SUPER_MOVE = superMove;
        CONTROLLER.setOwner(this);
        WEAPON.setOwner(this);
        SUPER_MOVE.setOwner(this);
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        health = MAX_HEALTH;
        attackCooldown = ATTACK_COOLDOWN;
        superCharge = MAX_SUPER_CHARGE;
        TEAM_CODE = teamCode;


        Animation[] animations = AnimationLoader.loadCharAnimations(CHARACTER_NAME);

        for (Animation currentAnimation : animations) {
            animationMap.put(currentAnimation.getNAME(), currentAnimation);
        }
        currentAnimation = animationMap.get("Idle " + getDirection(orientation));
    }

    public void update(int milliseconds) {
        CONTROLLER.update(milliseconds);
        SUPER_MOVE.update(milliseconds);

        boolean useSuper = superCharge == MAX_SUPER_CHARGE && CONTROLLER.useSuper();
        if (useSuper && !SUPER_MOVE.active()) {
            superCharge = 0;
            SUPER_MOVE.activate();
        }
        int[] move = SUPER_MOVE.active() ? SUPER_MOVE.getMoveDirection() : CONTROLLER.getMoveDirection();
        if (!currentAnimation.isDISRUPTIVE()) {
            double moveAmount = moveSpeed * milliseconds / 1000;
            x += move[0] * moveAmount;
            y += move[1] * moveAmount;

            x = Math.min(Math.max(WORLD.getMIN_X(), x), WORLD.getMAX_X());
            y = Math.min(Math.max(WORLD.getMIN_Y(), y), WORLD.getMAX_Y());
        }
        boolean tryAttack = CONTROLLER.attack();
        boolean attack = attackCooldown == 0 && tryAttack && stamina >= 1;
        WEAPON.update(milliseconds, attack);

        if (useSuper) {
            state = "Move";
        } else if (attack) {
            attackCooldown = ATTACK_COOLDOWN;
            state = "Attack";
            ATTACK_MANAGER.onAttack();
            stamina -= 1;
        } else if (move[0] != 0 || move[1] != 0) {
            if (move[0] == -1) {
                orientation = 2;
            } else if (move[0] == 1) {
                orientation = 0;
            } else if (move[1] == 1) {
                orientation = 1;
            } else {
                orientation = 3;
            }
            state = "Move";
        } else {
            state = "Idle";
        }
        //TODO: Super Animations
        state += " " + getDirection(orientation);
        if (!state.equals(lastState)) {
            animationMap.get(lastState).reset();
        }
        lastState = state;
        if (state.contains("Attack") || currentAnimation.animationDone()) {
            if (currentAnimation.isDISRUPTIVE()) {
                currentAnimation.reset();
            }
            currentAnimation = animationMap.get(state);
        }
        currentAnimation.update(milliseconds);


        if (!tryAttack) {
            attackCooldown = Math.max(attackCooldown - milliseconds, 0);
        }

        ATTACK_MANAGER.update(milliseconds, currentAnimation.getNAME().contains("Attack"), tryAttack);

        health += regen * milliseconds / 1000;
        health = Math.min(MAX_HEALTH, health);
        stamina += STAMINA_REGEN * milliseconds / 1000;
        stamina = Math.min(STAMINA, stamina);
    }

    public double attacked(Char enemy, double damage) {
        boolean deflected = Math.random() < deflectionChance;
        if (deflected) {
            deflectionChance -= (1 - Utils.cb(deflectionChance)) * damage / MAX_HEALTH;
            damage = 0;
        } else {
            damage = Math.max(0, damage - armor);
            health -= damage;
        }
        if (currentAnimation.animationDone() && !currentAnimation.isDISRUPTIVE()) {
            currentAnimation = animationMap.get("Hurt " + getDirection(orientation));
        }
        return damage;
    }

    public void hit(Char enemy, double damage) {
        superCharge = Math.min(superCharge + damage, MAX_SUPER_CHARGE);
    }

    public abstract void onDeath(Char enemy);

    public abstract void onKill(Char enemy);


    public double getMAX_HEALTH() {
        return MAX_HEALTH;
    }

    public double getMAX_SUPER_CHARGE() {
        return MAX_SUPER_CHARGE;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public double getSuperCharge() {
        return superCharge;
    }

    public int getATTACK_COOLDOWN() {
        return ATTACK_COOLDOWN;
    }

    public double getDeflectionChance() {
        return deflectionChance;
    }

    public double getDEFLECTION_CHANCE() {
        return DEFLECTION_CHANCE;
    }

    public double getSTAMINA() {
        return STAMINA;
    }

    public double getSTAMINA_REGEN() {
        return STAMINA_REGEN;
    }

    public int getTEAM_CODE() {
        return TEAM_CODE;
    }

    public String getCHARACTER_NAME() {
        return CHARACTER_NAME;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getOrientation() {
        return orientation;
    }

    public double getHealth() {
        return health;
    }

    public double getRegen() {
        return regen;
    }

    public double getArmor() {
        return armor;
    }

    public double getStamina() {
        return stamina;
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public int getAnimationIndex() {
        return currentAnimation.getIndex();
    }

    public Weapon getWEAPON() {
        return WEAPON;
    }

    public SuperMove getSUPER_MOVE() {
        return SUPER_MOVE;
    }

    public void setRegen(double regen) {
        this.regen = regen;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void setDeflectionChance(double deflectionChance) {
        this.deflectionChance = deflectionChance;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public AnimationRenderer getDeathAnimation() {
        int animation = (int) (Math.random() * 2 + 1);
        Animation deathAnimation = animationMap.get("Death " + animation);
        return new AnimationRenderer(x, y, CHARACTER_NAME, deathAnimation);
    }

    private String getDirection(int orientation) {
        String[] indices = new String[]{"Right", "Down", "Left", "Up"};
        return indices[orientation];
    }
}
