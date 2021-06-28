package rs.edu.raf.mtomic.paclike.agent.player;

import javafx.util.Pair;
import rs.edu.raf.mtomic.paclike.Direction;
import rs.edu.raf.mtomic.paclike.FieldState;
import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.agent.PlayingAgent;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;

import java.util.ArrayList;

import static rs.edu.raf.mtomic.paclike.FieldState.BLOCKED;
import static rs.edu.raf.mtomic.paclike.MathUtils.nextLeftGridX;
import static rs.edu.raf.mtomic.paclike.MathUtils.nextRightGridX;

public abstract class Player extends PlayingAgent {
    public Player(GameState gameState) {
        super(gameState);
        LEFT_OFFSET = 4;
        UP_OFFSET = 8;
        DOWN_OFFSET = 12;
        currentOffset = LEFT_OFFSET;
        activeSpriteId = LEFT_OFFSET;
        spriteCenterX = 14 * 8 + 4;
        spriteCenterY = 23 * 8 + 4;
    }

    public final void playMove() {
        Runnable best = generateNextMove();
        best.run();
    }

    protected abstract Runnable generateNextMove();

    @Override
    protected final void fillSprites() {
        sprites = new Sprite[16];
        sprites[0] = Sprite.PAC_UP_OPEN;
        sprites[1] = sprites[3] = Sprite.PAC_UP_CLOSED;
        sprites[2] = Sprite.PAC_FULL_CLOSED;
        sprites[4] = Sprite.PAC_LEFT_OPEN;
        sprites[5] = sprites[7] = Sprite.PAC_LEFT_CLOSED;
        sprites[6] = Sprite.PAC_FULL_CLOSED;
        sprites[8] = Sprite.PAC_RIGHT_OPEN;
        sprites[9] = sprites[11] = Sprite.PAC_RIGHT_CLOSED;
        sprites[10] = Sprite.PAC_FULL_CLOSED;
        sprites[12] = Sprite.PAC_DOWN_OPEN;
        sprites[13] = sprites[15] = Sprite.PAC_DOWN_CLOSED;
        sprites[14] = Sprite.PAC_FULL_CLOSED;
    }

    protected final ArrayList<AvailableStruct> getAvailableFields() {
        ArrayList<AvailableStruct> list = new ArrayList<>();
        FieldState[][] fields = gameState.getFields();
        int posX = getGridX();
        int posY = getGridY();

        for (Direction d : Direction.values()) {
            switch (d) {
                case UP: {
                    if (fields[posX][posY - 1].equals(BLOCKED)) {
                        continue;
                    }
                    // take into consideration
                    list.add(new AvailableStruct(d, new Pair<>(posX, posY - 1), this::goUp));
                    break;
                }
                case LEFT: {
                    if (fields[nextLeftGridX(posX)][posY].equals(BLOCKED)) {
                        continue;
                    }
                    // take into consideration
                    list.add(new AvailableStruct(d, new Pair<>(nextLeftGridX(posX), posY), this::goLeft));
                    break;
                }
                case DOWN: {
                    if (fields[posX][posY + 1].equals(BLOCKED)) {
                        continue;
                    }
                    // take into consideration
                    list.add(new AvailableStruct(d, new Pair<>(posX, posY + 1), this::goDown));
                    break;
                }
                case RIGHT: {
                    if (fields[nextRightGridX(posX)][posY].equals(BLOCKED)) {
                        continue;
                    }
                    // take into consideration
                    list.add(new AvailableStruct(d, new Pair<>(nextRightGridX(posX), posY), this::goRight));
                    break;
                }
            }
        }
        return list;
    }

    public final void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
