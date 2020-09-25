package stuckinlife.gamecore.characters.attack;

import stuckinlife.gamecore.World;
import stuckinlife.gamecore.characters.charsets.Char;
import utils.Utils;

import java.util.List;

public class LongRangeSmartAttack extends AttackController {

    private final double VIEW_RANGE;
    private final double THRESHOLD;

    public LongRangeSmartAttack(World world, double viewRange, double threshold) {
        super(world);
        VIEW_RANGE = viewRange;
        THRESHOLD = threshold;
    }

    @Override
    public boolean update(int milliseconds) {
        Char closestEnemy = getClosestEnemyInAngle();
        if (closestEnemy != null) {
            double distance = Utils.distanceEuclidean(owner.getX(), owner.getY(), closestEnemy.getX(), closestEnemy.getY());
            return distance < VIEW_RANGE;
        }
        return false;
    }

    private Char getClosestEnemyInAngle() {
        List<Char> characters = WORLD.getCharacters();

        double angle = owner.getOrientation() * Math.PI / 2;

        int closestEnemyIndex = -1;
        double lowestDistance = Double.POSITIVE_INFINITY;

        double ownerX = owner.getX();
        double ownerY = owner.getY();

        for (int i = 0; i < characters.size(); i++) {
            Char character = characters.get(i);

            double characterX = character.getX();
            double characterY = character.getY();

            double currentAngle = Math.atan2(characterY - ownerY, characterX - ownerX);
            if (character.getTEAM_CODE() != owner.getTEAM_CODE()) {
                if (Math.abs(Utils.relativeAngle(currentAngle - angle)) < Math.PI / 4 &&
                        Math.min(Math.abs(characterX - ownerX), Math.abs(characterY - ownerY)) < THRESHOLD) {
                    double currentDistance = Utils.distanceEuclidean(characterX, characterY, ownerX, ownerY);
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
