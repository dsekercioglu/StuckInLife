package stuckinlife.gamecore.characters.controller;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class ReaperController extends CharacterController {

    private int[] moveDir = new int[2];

    private static final int DECISION_TIME = 500;
    private boolean attack;
    private boolean useSuper;
    private int decisionCounter = 0;

    public ReaperController(World world) {
        super(world);
    }

    @Override
    public void update(int milliseconds) {
        List<Char> characters = WORLD.getCharacters();
        if (decisionCounter <= 0) {
            Char enemy = getBestTarget();
            double lowestDanger = Double.POSITIVE_INFINITY;
            int[] bestMoveDir = new int[2];
            if (enemy != null) {
                double distanceToEnemy = Utils.distanceManhattan(owner.getX(), owner.getY(), enemy.getX(), enemy.getY());

                double targetDistance = 0;
                if (distanceToEnemy > enemy.getWEAPON().getRANGE()) {
                    int numTeammates = 0;
                    double averageHealth = 0;
                    for (Char character : characters) {
                        if (character.getTEAM_CODE() == owner.getTEAM_CODE() && owner.getClass().isAssignableFrom(character.getClass())) {
                            numTeammates++;
                            averageHealth += character.getHealth();
                            targetDistance += Utils.distanceManhattan(character.getX(), character.getY(), enemy.getX(), enemy.getY());
                        }
                    }
                    targetDistance = targetDistance / numTeammates;
                    targetDistance = Math.min(targetDistance * 0.8, targetDistance - 50);
                    averageHealth /= numTeammates;
                    targetDistance *= averageHealth / owner.getHealth();
                }
                for (int[] dir : MOVE_DIR) {
                    double nextX = owner.getX() + dir[0] * owner.getMoveSpeed();
                    double nextY = owner.getY() + dir[1] * owner.getMoveSpeed();
                    double moveAmt = Math.max(Math.abs(nextX - owner.getX()), Math.abs(nextY - owner.getY()));
                    if (moveAmt != 0) {
                        double currentDanger = getDanger(enemy,
                                characters,
                                targetDistance,
                                nextX,
                                nextY);
                        if (currentDanger < lowestDanger) {
                            lowestDanger = currentDanger;
                            bestMoveDir = dir;
                        }
                    }
                }
            }
            moveDir = bestMoveDir;
            decisionCounter = DECISION_TIME;
        }

        double ownerAngle = owner.getOrientation() * Math.PI / 2;
        attack = false;
        for (Char character : characters) {
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                double angle = Math.atan2(character.getY() - owner.getY(), character.getX() - owner.getX());
                if (Math.abs(Utils.relativeAngle(ownerAngle - angle)) < Math.PI / 4) {
                    double currentDistance = Utils.distanceEuclidean(character.getX(), character.getY(), owner.getX(), owner.getY());
                    if (currentDistance < owner.getWEAPON().getRANGE()) {
                        attack = true;
                    }
                }
            }
        }

        decisionCounter -= milliseconds;
    }

    @Override
    public int[] getMoveDirection() {
        return moveDir;
    }

    @Override
    public boolean attack() {
        return attack;
    }

    @Override
    public boolean useSuper() {
        return false;
    }

    private Char getBestTarget() {
        List<Char> characters = WORLD.getCharacters();

        int targetIndex = -1;
        double lowestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < characters.size(); i++) {
            Char character = characters.get(i);
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                double currentDistance = 0;
                for (Char other : characters) {
                    if (owner.getTEAM_CODE() == other.getTEAM_CODE() && owner.getClass().isAssignableFrom(other.getClass())) {
                        currentDistance += Utils.distanceManhattan(character.getX(), character.getY(), other.getX(), other.getY());
                    }
                }
                if (currentDistance < lowestDistance) {
                    targetIndex = i;
                    lowestDistance = currentDistance;
                }
            }
        }
        if (targetIndex == -1) {
            return null;
        } else {
            return characters.get(targetIndex);
        }
    }

    private double getDanger(Char enemy, List<Char> characters, double targetDistance, double x, double y) {
        x = Math.max(WORLD.getMIN_X(), Math.min(x, WORLD.getMAX_X()));
        y = Math.max(WORLD.getMIN_Y(), Math.min(y, WORLD.getMAX_Y()));
        double teammateDanger = 0;
        double targetDanger = 0;
        double angleDanger = 0;
        for (Char character : characters) {
            double distance = Utils.distanceManhattan(character.getX(), character.getY(), x, y);
            double angle = Math.atan2(y - character.getY(), x - character.getX());
            if (character.getTEAM_CODE() == owner.getTEAM_CODE()) {
                teammateDanger += 1 / Utils.sq(distance + 1);
            } else {
                angleDanger += 1 / (Utils.sq(Utils.relativeAngle(angle - (character.getOrientation() * Math.PI * 0.5))) * 25 + 1);
                if (character == enemy) {
                    targetDanger += Utils.sq(distance - targetDistance);
                }
            }
        }

        return Utils.sq(teammateDanger * angleDanger) * targetDanger;
    }

}
