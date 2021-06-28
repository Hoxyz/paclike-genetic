package rs.edu.raf.mtomic.paclike.agent.ghost;

import javafx.util.Pair;
import rs.edu.raf.mtomic.paclike.Direction;
import rs.edu.raf.mtomic.paclike.FieldState;
import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.agent.PlayingAgent;
import rs.edu.raf.mtomic.paclike.agent.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static rs.edu.raf.mtomic.paclike.Direction.*;
import static rs.edu.raf.mtomic.paclike.FieldState.BLOCKED;
import static rs.edu.raf.mtomic.paclike.MathUtils.nextLeftGridX;
import static rs.edu.raf.mtomic.paclike.MathUtils.nextRightGridX;

public abstract class Ghost extends PlayingAgent {
    protected boolean shouldDecide = true;
    protected final boolean[] went = new boolean[4];
    protected int oldX = getGridX();
    protected int oldY = getGridY();
    protected Runnable nextMove;
    protected Player player = null;

    public Ghost(GameState gameState) {
        super(gameState);
        spriteCenterX = 13 * 8 + 4;
        spriteCenterY = 11 * 8 + 4;
    }

    @Override
    public final void playMove() {
        if (playMoveInit()) return;

        if (shouldDecide && spriteCenterX % 8 == 4 && spriteCenterY % 8 == 4) {
            AvailableStruct best = calculateBest(getAvailableFields());
            nextMove = best.method;
            Arrays.fill(went, false);
            went[best.direction.ordinal()] = true;
        }

        nextMove.run();
        if (oldX != getGridX() || oldY != getGridY()) {
            oldX = getGridX();
            oldY = getGridY();
            shouldDecide = true;

            if (oldY == 14 && (oldX < 5 || oldX > 22)) {
                speed = 2;
            } else {
                speed = 1;
            }
        }
    }

    public final ArrayList<AvailableStruct> getAvailableFields() {
        ArrayList<AvailableStruct> list = new ArrayList<>();
        FieldState[][] fields = gameState.getFields();
        int posX = getGridX();
        int posY = getGridY();
        for (Direction d : Direction.values()) {
            switch (d) {
                case UP: {
                    if ((posX == 12 || posX == 15) && (posY == 11 || posY == 23)) {
                        continue;
                    }
                    if (fields[posX][posY - 1].equals(BLOCKED)) {
                        continue;
                    }
                    if (!fields[posX][posY + 1].equals(BLOCKED) && went[DOWN.ordinal()]) {
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
                    if (!fields[nextRightGridX(posX)][posY].equals(BLOCKED) && went[RIGHT.ordinal()]) {
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
                    if (!fields[posX][posY - 1].equals(BLOCKED) && went[UP.ordinal()]) {
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
                    if (!fields[nextLeftGridX(posX)][posY].equals(BLOCKED) && went[LEFT.ordinal()]) {
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

    /**
     * return true if should skip move
     */
    protected boolean playMoveInit() {
        if (player == null) {
            player = (Player) (gameState.getAgents().stream().filter(x -> x instanceof Player)).findFirst().orElse(null);
            return player == null;
        }
        return false;
    }

    /**
     * return the best (chosen) position based on available positions for the next move
     */
    public abstract AvailableStruct calculateBest(List<AvailableStruct> availableFields);


}