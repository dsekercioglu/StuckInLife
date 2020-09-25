package stuckinlife.gamecore.characters.controller;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class PinchController extends CharacterController {

    private int[] moveDir = new int[2];

    private static final int DECISION_TIME = 500;
    private boolean attack;
    private int decisionCounter = 0;

    public PinchController(World world) {
        super(world);
    }

    @Override
    public void update(int milliseconds) {
        List<Char> characters = WORLD.getCharacters();
        if (decisionCounter <= 0) {
            Char enemy = getClosestEnemy();
            double lowestDanger = Double.POSITIVE_INFINITY;
            int[] bestMoveDir = new int[2];
            if (enemy != null) {
                double targetDistance = 0;
                int numSame = 0;
                int numTeammates = 0;
                for (Char character : characters) {
                    boolean sameTeam = character.getTEAM_CODE() == owner.getTEAM_CODE();
                    if (sameTeam) {
                        numSame++;
                        if (owner.getClass().isAssignableFrom(character.getClass())) {
                            numTeammates++;
                            targetDistance += Utils.distanceManhattan(character.getX(), character.getY(), enemy.getX(), enemy.getY());
                        }
                    }
                }
                targetDistance /= numSame + 2; //to get closer
                targetDistance /= characters.size() - numTeammates;
                for (int[] dir : MOVE_DIR) {
                    double nextX = owner.getX() + dir[0] * owner.getMoveSpeed();
                    double nextY = owner.getY() + dir[1] * owner.getMoveSpeed();
                    double moveAmt = Math.max(Math.abs(nextY - owner.getX()), Math.abs(nextY - owner.getY()));
                    if (moveAmt != 0) {
                        double currentDanger = getDanger(enemy,
                                characters,
                                targetDistance,
                                nextX,
                                nextY);
                        double secondaryDanger = Double.POSITIVE_INFINITY;
                        for (int[] branchDir : MOVE_DIR) {
                            secondaryDanger = Math.min(secondaryDanger, getDanger(enemy,
                                    characters,
                                    targetDistance,
                                    nextX + branchDir[0] * owner.getMoveSpeed(),
                                    nextY + branchDir[1] * owner.getMoveSpeed()));
                        }
                        currentDanger += secondaryDanger * 0.5;
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

    private double getDanger(Char enemy, List<Char> characters, double targetDistance, double x, double y) {
        x = Math.max(WORLD.getMIN_X(), Math.min(x, WORLD.getMAX_X()));
        y = Math.max(WORLD.getMIN_Y(), Math.min(y, WORLD.getMAX_Y()));
        double teammateDanger = 0;
        double targetDanger = 0;
        for (Char character : characters) {
            double distance = Utils.distanceManhattan(character.getX(), character.getY(), x, y);
            if (character.getTEAM_CODE() == owner.getTEAM_CODE()) {
                teammateDanger += 1 / Utils.sq(distance + 1);
            } else if (character == enemy) {
                targetDanger += distance < owner.getWEAPON().getRANGE() ? 1 : Utils.sq(distance - targetDistance);
            }
        }
        return Utils.sq(teammateDanger) * targetDanger;
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

    private Char getClosestEnemy() {
        List<Char> characters = WORLD.getCharacters();

        int closestEnemyIndex = -1;
        double lowestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < characters.size(); i++) {
            Char character = characters.get(i);
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                double currentDistance = Utils.distanceManhattan(character.getX(), character.getY(), owner.getX(), owner.getY());
                if (currentDistance < lowestDistance) {
                    closestEnemyIndex = i;
                    lowestDistance = currentDistance;
                }
            }
        }

        if (closestEnemyIndex == -1) {
            return null;
        } else {
            return characters.get(closestEnemyIndex);
        }
    }
}
