package rs.edu.raf.mtomic.paclike.agent.ghost;

import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;

import java.util.Comparator;
import java.util.List;

import static rs.edu.raf.mtomic.paclike.MathUtils.distance;

public final class Inky extends Ghost {
    private Blinky blinky = null;

    public Inky(GameState gameState) {
        super(gameState);
    }

    @Override
    protected boolean playMoveInit() {
        boolean flag = super.playMoveInit();
        if (!flag && blinky == null) {
            blinky = (Blinky) (gameState.getAgents().stream().filter(x -> x instanceof Blinky)).findFirst().orElse(null);
            return blinky == null;
        }
        return flag;
    }

    @Override
    public AvailableStruct calculateBest(List<AvailableStruct> availableFields) {
        int targetX = player.getGridX();
        int targetY = player.getGridY();
        switch (player.getCurrentDirection()) {
            case LEFT:
                targetX -= 2;
                break;
            case RIGHT:
                targetX += 2;
                break;
            case UP:
                targetY -= 2;
                break;
            case DOWN:
                targetY += 2;
                break;
        }
        targetX = 2 * targetX - blinky.getGridX();
        targetY = 2 * targetY - blinky.getGridY();

        final int tx = targetX;
        final int ty = targetY;

        return availableFields.stream()
                .min(Comparator.comparingDouble(x -> distance(x.gridPosition.getKey(), x.gridPosition.getValue(), tx, ty)))
                .orElse(availableFields.get(0));
    }

    @Override
    protected void fillSprites() {
        Sprite[] all = Sprite.values();
        int start = Sprite.INKY_RIGHT_OPEN.ordinal();
        System.arraycopy(all, start, sprites, 0, 8);
    }

}
