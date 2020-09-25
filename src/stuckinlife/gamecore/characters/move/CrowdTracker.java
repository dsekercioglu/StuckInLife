package stuckinlife.gamecore.characters.move;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class CrowdTracker extends MoveController {


    private final int MILLISECONDS_PER_DIR_CHANGE;
    private final double VIEW_RANGE;
    private int moveCounter;
    private int lastMoveDir = 4;


    public CrowdTracker(World world, int millisecondsPerDirChange, double viewRange) {
        super(world);
        MILLISECONDS_PER_DIR_CHANGE = millisecondsPerDirChange;
        VIEW_RANGE = viewRange;
        moveCounter = 0;
    }

    @Override
    public void update(int milliseconds) {
        moveCounter -= milliseconds;
        moveCounter = Math.max(0, moveCounter);
    }

    @Override
    public int[] getMoveDirection() {
        if (moveCounter == 0) {
            moveCounter = (int) (MILLISECONDS_PER_DIR_CHANGE * Math.random());
            List<Char> characters = WORLD.getCharacters();

            double[] orientations = new double[4];

            double ownerX = owner.getX();
            double ownerY = owner.getY();

            for (Char character : characters) {
                if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                    double characterX = character.getX();
                    double characterY = character.getY();
                    double angle = Math.atan2(characterY - ownerY, characterX - ownerX);
                    int orientation = (int) Math.round(angle / Math.PI * 2) + 1;
                    orientation += 4;
                    orientation %= 4;
                    orientations[orientation] += 1 / (Utils.distanceEuclidean(ownerX, ownerY, characterX, characterY) + 1);
                }
            }
            int bestOrientation = 4;
            double highestScore = 0;
            for (int i = 0; i < orientations.length; i++) {
                if (orientations[i] > highestScore) {
                    bestOrientation = i;
                    highestScore = orientations[i];
                }
            }
            lastMoveDir = bestOrientation;
        }
        return MOVE_DIR[lastMoveDir];
    }
}
