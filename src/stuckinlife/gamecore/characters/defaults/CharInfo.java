package stuckinlife.gamecore.characters.defaults;

import stuckinlife.gamecore.characters.charsets.Char;
import stuckinlife.gamecore.characters.charsets.CharacterType;

public class CharInfo {

    private final double HEALTH;
    private final double REGEN;
    private final double ARMOR;
    private final double MOVE_SPEED;
    private final int ATTACK_COOLDOWN;
    private final int SUPER_CHARGE;
    private final double DEFLECT_CHANCE;
    private final double STAMINA;
    private final double STAMINA_REGEN;
    private final CharacterType CHARACTER_TYPE;
    private final String CHAR_NAME;

    public CharInfo(double health,
                    double regen,
                    double armor,
                    double moveSpeed,
                    int attackCooldown,
                    int superCharge,
                    double deflectChance,
                    double stamina,
                    double staminaRegen,
                    CharacterType characterType,
                    String charName) {
        HEALTH = health;
        REGEN = regen;
        ARMOR = armor;
        MOVE_SPEED = moveSpeed;
        ATTACK_COOLDOWN = attackCooldown;
        SUPER_CHARGE = superCharge;
        DEFLECT_CHANCE = deflectChance;
        STAMINA = stamina;
        STAMINA_REGEN = staminaRegen;
        CHARACTER_TYPE = characterType;
        CHAR_NAME = charName;
    }

    public double getHEALTH() {
        return HEALTH;
    }

    public double getREGEN() {
        return REGEN;
    }

    public double getARMOR() {
        return ARMOR;
    }

    public double getMOVE_SPEED() {
        return MOVE_SPEED;
    }

    public int getATTACK_COOLDOWN() {
        return ATTACK_COOLDOWN;
    }

    public int getSUPER_CHARGE() {
        return SUPER_CHARGE;
    }

    public double getDEFLECT_CHANCE() {
        return DEFLECT_CHANCE;
    }

    public double getSTAMINA() {
        return STAMINA;
    }

    public double getSTAMINA_REGEN() {
        return STAMINA_REGEN;
    }

    public CharacterType getCHARACTER_TYPE() {
        return CHARACTER_TYPE;
    }

    public String getCHAR_NAME() {
        return CHAR_NAME;
    }
}