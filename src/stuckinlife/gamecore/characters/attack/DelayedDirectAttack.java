package stuckinlife.gamecore.characters.attack;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class DelayedDirectAttack extends AttackController {

    private final int DELAY_MILLISECONDS;
    private final double VIEW_RANGE;
    private int delayCounter;

    public DelayedDirectAttack(World world, int delayMilliseconds, double viewRange) {
        super(world);
        DELAY_MILLISECONDS = delayMilliseconds;
        delayCounter = DELAY_MILLISECONDS;
        VIEW_RANGE = viewRange;
    }

    @Override
    public boolean update(int milliseconds) {
        Char closestEnemy = getClosestEnemy();
        if (closestEnemy != null) {
            double distance = Utils.distanceEuclidean(closestEnemy.getX(), closestEnemy.getY(), owner.getX(), owner.getY());
            if (distance < VIEW_RANGE) {
                if (delayCounter == 0) {
                    delayCounter = DELAY_MILLISECONDS;
                }
                delayCounter -= milliseconds;
                delayCounter = Math.max(delayCounter, 0);
                return delayCounter <= 0;
            }
            delayCounter = DELAY_MILLISECONDS;
        }
        return false;
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
