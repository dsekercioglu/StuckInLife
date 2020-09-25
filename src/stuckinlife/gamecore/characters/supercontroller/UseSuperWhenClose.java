package stuckinlife.gamecore.characters.supercontroller;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class UseSuperWhenClose extends SuperController {

    private final double DISTANCE_THRESHOLD;

    public UseSuperWhenClose(World world, double distanceThreshold) {
        super(world);
        DISTANCE_THRESHOLD = distanceThreshold;
    }

    public void update(int milliseconds) {

    }

    @Override
    public boolean useSuper() {
        Char closestEnemy = getClosestEnemy();
        return Utils.distanceEuclidean(closestEnemy.getX(), closestEnemy.getY(), owner.getX(), owner.getY()) < DISTANCE_THRESHOLD;
    }

    private Char getClosestEnemy() {
        List<Char> characters = WORLD.getCharacters();

        int closestEnemyIndex = -1;
        double lowestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i < characters.size(); i++) {
            Char character = characters.get(i);
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                double currentDistance = Utils.distanceEuclidean(character.getX(), character.getY(), owner.getX(), owner.getY());
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
