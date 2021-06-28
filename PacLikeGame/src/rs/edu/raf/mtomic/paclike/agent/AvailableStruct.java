package rs.edu.raf.mtomic.paclike.agent;

import javafx.util.Pair;
import rs.edu.raf.mtomic.paclike.Direction;

public class AvailableStruct {
    public final Direction direction;
    public final Pair<Integer, Integer> gridPosition;
    public final Runnable method;

    public AvailableStruct(Direction direction, Pair<Integer, Integer> gridPosition, Runnable method) {
        this.direction = direction;
        this.gridPosition = gridPosition;
        this.method = method;
    }
}
