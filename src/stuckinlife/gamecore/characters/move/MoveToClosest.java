package stuckinlife.gamecore.characters.move;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class MoveToClosest extends MoveController {


    private final int MILLISECONDS_PER_DIR_CHANGE;
    private final double VIEW_RANGE;
    private int moveCounter;
    private int lastMoveDir;


    public MoveToClosest(World world, int millisecondsPerDirChange, double viewRange) {
        super(world);
        MILLISECONDS_PER_DIR_CHANGE = millisecondsPerDirChange;
        VIEW_RANGE = viewRange;
        moveCounter = 0;
        lastMoveDir = (int) (Math.random() * 4);
    }

    @Override
    public void update(int milliseconds) {
        moveCounter -= milliseconds;
        moveCounter = Math.max(moveCounter, 0);
    }

    @Override
    public int[] getMoveDirection() {
        Char closestEnemy = getClosestEnemy(WORLD);
        int orientation = lastMoveDir;
        if (closestEnemy != null) {
            if (moveCounter == 0) {
                double angle = Math.atan2(closestEnemy.getY() - owner.getY(), closestEnemy.getX() - owner.getX());
                orientation = (int) Math.round(angle * 2 / Math.PI) + 1;
                while (orientation < 0) {
                    orientation += 4;
                }
                orientation %= 4;
                lastMoveDir = orientation;
                moveCounter = (int) (MILLISECONDS_PER_DIR_CHANGE * Math.random());
            }
        } else {
            orientation = 4;
        }
        return MOVE_DIR[orientation];
    }

    private Char getClosestEnemy(World world) {
        List<Char> characters = world.getCharacters();

        int closestEnemyIndex = -1;
        double lowestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < characters.size(); i++) {
            Char character = characters.get(i);
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                double currentDistance = Utils.distanceEuclidean(character.getX(), character.getY(), owner.getX(), owner.getY());
                if (currentDistance < VIEW_RANGE) {
                    if (currentDistance < lowestDistance) {
                        closestEnemyIndex = i;
                        lowestDistance = currentDistance;
                    }
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
