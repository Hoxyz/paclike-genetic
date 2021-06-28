package rs.edu.raf.mtomic.paclike.agent.ghost;

import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;

import java.util.Comparator;
import java.util.List;

import static rs.edu.raf.mtomic.paclike.MathUtils.distance;

public final class Blinky extends Ghost {

    public Blinky(GameState gameState) {
        super(gameState);
    }

    @Override
    public AvailableStruct calculateBest(List<AvailableStruct> availableFields) {
        int targetX = player.getGridX();
        int targetY = player.getGridY();

        return availableFields.stream()
                .min(Comparator.comparingDouble(x -> distance(x.gridPosition.getKey(), x.gridPosition.getValue(), targetX, targetY)))
                .orElse(availableFields.get(0));
    }

    @Override
    protected void fillSprites() {
        Sprite[] all = Sprite.values();
        int start = Sprite.BLINKY_RIGHT_OPEN.ordinal();
        System.arraycopy(all, start, sprites, 0, 8);
    }
}
