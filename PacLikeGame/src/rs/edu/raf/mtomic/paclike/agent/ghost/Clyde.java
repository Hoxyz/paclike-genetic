package rs.edu.raf.mtomic.paclike.agent.ghost;

import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;

import java.util.Comparator;
import java.util.List;

import static rs.edu.raf.mtomic.paclike.MathUtils.distance;

public final class Clyde extends Ghost {

    public Clyde(GameState gameState) {
        super(gameState);
    }

    @Override
    public AvailableStruct calculateBest(List<AvailableStruct> availableFields) {
        int targetX = player.getGridX();
        int targetY = player.getGridY();
        if (distance(targetX, targetY, getGridX(), getGridY()) > 8) {
            targetX = 0;
            targetY = 32;
        }

        final int tx = targetX;
        final int ty = targetY;
        return availableFields.stream()
                .min(Comparator.comparingDouble((AvailableStruct x) ->
                        distance(x.gridPosition.getKey(), x.gridPosition.getValue(), tx, ty))
                        .thenComparingInt(x -> x.gridPosition.getKey())
                        .thenComparingInt(x -> x.gridPosition.getValue()))
                .orElse(availableFields.get(0));
    }

    @Override
    protected void fillSprites() {
        Sprite[] all = Sprite.values();
        int start = Sprite.CLYDE_RIGHT_OPEN.ordinal();
        System.arraycopy(all, start, sprites, 0, 8);
    }

}
