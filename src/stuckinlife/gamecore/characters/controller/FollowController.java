package stuckinlife.gamecore.characters.controller;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class FollowController extends CharacterController {

    private int[] moveDir = new int[2];

    private static final int DECISION_TIME = 500;
    private boolean attack;
    private int decisionCounter = 0;

    public FollowController(World world) {
        super(world);
    }

    @Override
    public void update(int milliseconds) {
        List<Char> characters = WORLD.getCharacters();
        if (decisionCounter <= 0) {
            Char enemy = getClosestEnemy();
            double lowestDanger = Double.POSITIVE_INFINITY;
            int[] bestMoveDir = new int[2];
            for (int[] dir : MOVE_DIR) {
                double x = owner.getX() + dir[0] * owner.getMoveSpeed();
                double y = owner.getY() + dir[1] * owner.getMoveSpeed();
                x = Math.max(WORLD.getMIN_X(), Math.min(x, WORLD.getMAX_X()));
                y = Math.max(WORLD.getMIN_Y(), Math.min(y, WORLD.getMAX_Y()));
                double moveAmt = Math.max(Math.abs(x - owner.getX()), Math.abs(y - owner.getY()));
                if(moveAmt != 0) {
                    double teammateDanger = 0;
                    double targetDanger = 0;
                    for (Char character : characters) {
                        double distance = Utils.distanceManhattan(character.getX(), character.getY(), x, y);
                        if (character.getTEAM_CODE() == owner.getTEAM_CODE()) {
                            teammateDanger += 1 / (distance + 1);
                        } else if (character == enemy) {
                            targetDanger += distance;
                        }
                    }
                    double currentDanger = teammateDanger * Utils.sq(targetDanger) / moveAmt;
                    if (targetDanger != 0 && currentDanger < lowestDanger) {
                        lowestDanger = currentDanger;
                        bestMoveDir = dir;
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
