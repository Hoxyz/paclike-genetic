package rs.edu.raf.mtomic.paclike.agent.ghost;

import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;

import java.util.Comparator;
import java.util.List;

import static rs.edu.raf.mtomic.paclike.MathUtils.distance;

public final class Pinky extends Ghost {
    public Pinky(GameState gameState) {
        super(gameState);
    }

    @Override
    public AvailableStruct calculateBest(List<AvailableStruct> availableFields) {
        int targetX = player.getGridX();
        int targetY = player.getGridY();
        switch (player.getCurrentDirection()) {
            case LEFT:
                targetX -= 4;
                break;
            case RIGHT:
                targetX += 4;
                break;
            case UP:
                targetY -= 4;
                break;
            case DOWN:
                targetY += 4;
                break;
        }
        final int tx = targetX;
        final int ty = targetY;

        return availableFields.stream()
                .min(Comparator.comparingDouble(x -> distance(x.gridPosition.getKey(), x.gridPosition.getValue(), tx, ty)))
                .orElse(availableFields.get(0));
    }

    @Override
    protected void fillSprites() {
        Sprite[] all = Sprite.values();
        int start = Sprite.PINKY_RIGHT_OPEN.ordinal();
        System.arraycopy(all, start, sprites, 0, 8);
    }

}
