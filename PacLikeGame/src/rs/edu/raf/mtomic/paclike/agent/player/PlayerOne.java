package rs.edu.raf.mtomic.paclike.agent.player;

import javafx.util.Pair;
import rs.edu.raf.mtomic.paclike.Chromosome;
import rs.edu.raf.mtomic.paclike.FieldState;
import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.agent.AvailableStruct;
import rs.edu.raf.mtomic.paclike.agent.PlayingAgent;
import rs.edu.raf.mtomic.paclike.agent.ghost.*;

import java.util.*;

import static rs.edu.raf.mtomic.paclike.MathUtils.distance;

public class PlayerOne extends Player {

    private Runnable nextMove = this::goLeft;
    private HashMap<Pair<Integer, Integer>, ArrayList<Pair<Integer, Integer>>> graph;
    private int moveNumber = 0;

    public float paramMaxValue = 100f;
    public float blinkyPenalty, clydePenalty, inkyPenalty, pinkyPenalty, pelletReward, GIB_TELEPORT = 0;
    public Chromosome chromosome;

    public PlayerOne(GameState gameState) {
        super(gameState);
        Random r = new Random();
        this.chromosome = new Chromosome();
        this.blinkyPenalty = chromosome.blinkyPenalty;
        this.clydePenalty = chromosome.clydePenalty;
        this.inkyPenalty = chromosome.inkyPenalty;
        this.pinkyPenalty = chromosome.pinkyPenalty;
        this.pelletReward = chromosome.pelletReward;
    }

    public PlayerOne(GameState gameState, Chromosome c) {
        super(gameState);
        Random r = new Random();
        this.chromosome = c;
        this.blinkyPenalty = c.blinkyPenalty;
        this.clydePenalty = c.clydePenalty;
        this.inkyPenalty = c.inkyPenalty;
        this.pinkyPenalty = c.pinkyPenalty;
        this.pelletReward = c.pelletReward;
    }

    @Override
    protected Runnable generateNextMove() {
        if (graph == null) {
            buildGraph();
        }
        moveNumber++;
        if (moveNumber % 10 == 0) {
            pelletReward += 0.01f;
        }
        List<AvailableStruct> availableFields = getAvailableFields();
        AvailableStruct bestMove = availableFields.get(0);
        float bestEval = -100f;
        for (AvailableStruct availableField : availableFields) {
            Pair<Integer, Integer> nextPosition = new Pair<>(availableField.gridPosition.getKey(), availableField.gridPosition.getValue());
            float eval = EvaluateState(nextPosition);
            //System.out.print(eval + " ");
            //System.out.println(nextPosition);
            if (bestEval < eval) {
                bestMove = availableField;
                bestEval = eval;
            }
        }
        return bestMove.method;
        //return chaseClosestPellet(new Pair<>(getGridX(), getGridY()));
    }

    private void buildGraph() {
        graph = new HashMap<>();
        for (int row = 0; row < 31; row++) {
            for (int col = 0; col < 28; col++) {
                graph.put(new Pair<>(col, row), new ArrayList<>());
                if (this.gameState.getFields()[col][row] == FieldState.BLOCKED) continue;
                graph.put(new Pair<>(col, row), getNeighbors(new Pair<Integer, Integer>(col, row)));
                if (row == 14 && (col == 0 || col == 27)) graph.get(new Pair<>(col, row)).add(new Pair<>(row, 27-col));
                //System.out.print(new Pair<>(col, row));
                //System.out.println(graph.get(new Pair<>(col, row)));
            }
        }
    }

    private ArrayList<Pair<Integer, Integer>> getNeighbors(Pair<Integer, Integer> cell) {
        ArrayList<Pair<Integer, Integer>> neighbors = new ArrayList<>();
        int col = cell.getKey();
        int row = cell.getValue();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i == 0 || j == 0) && !(i == 0 && j == 0)) {
                    int nrow = row+i;
                    int ncol = col+j;
                    if (nrow < 0 || nrow > 30 || ncol < 0 || ncol > 27) continue;

                    if (this.gameState.getFields()[ncol][nrow] != FieldState.BLOCKED) {
                        neighbors.add(new Pair<>(ncol, nrow));
                    }
                }
            }
        }
        return neighbors;
    }

    private float EvaluateState(Pair<Integer, Integer> pacmanPos) {
        float eval = 0f;
        eval += ghostEval(pacmanPos);
        eval += pelletEval(pacmanPos);
        eval += teleportEval(pacmanPos);
        return eval;
    }

    private float teleportEval(Pair<Integer, Integer> start) {
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        HashMap<Pair<Integer, Integer>, Pair<Integer,Integer>> parents = new HashMap<>();
        HashMap<Pair<Integer, Integer>, Boolean> visited = new HashMap<>();
        queue.add(start); // X i Y obrnuti jer ovako predstavljaju [row][col] u matrici
        visited.put(start, true);
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> cell = queue.remove();

            if (cell.getKey() == 14 && (cell.getValue() == 0 || cell.getValue() == 27)) {
                return GIB_TELEPORT * 1 / cellDistance(parents, start, cell);
            }

            for (Pair<Integer, Integer> neighbor : graph.get(cell)) {
                if (visited.get(neighbor) == null) {
                    visited.put(neighbor, true);
                    parents.put(neighbor, cell);
                    queue.add(neighbor);
                }
            }
        }
        return 0;
    }

    private float ghostEval(Pair<Integer, Integer> start) {
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        HashMap<Pair<Integer, Integer>, Pair<Integer,Integer>> parents = new HashMap<>();
        HashMap<Pair<Integer, Integer>, Boolean> visited = new HashMap<>();
        queue.add(start); // X i Y obrnuti jer ovako predstavljaju [row][col] u matrici
        visited.put(start, true);
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> cell = queue.remove();

            for (Ghost ghost : this.gameState.getGhosts()) {
                //AvailableStruct ghostMove = ghost.calculateBest(ghost.getAvailableFields());
                //int tx = ghostMove.gridPosition.getKey();
                //int ty = ghostMove.gridPosition.getValue();
                int tx = ghost.getGridX();
                int ty = ghost.getGridY();

                if (tx == cell.getKey() && ty == cell.getValue()) {
                    if (ghost instanceof Blinky) {
                        return blinkyPenalty * 1 / cellDistance(parents, start, cell);
                    }
                    if (ghost instanceof Clyde) {
                        return clydePenalty * 1 / cellDistance(parents, start, cell);
                    }
                    if (ghost instanceof Inky) {
                        return inkyPenalty * 1 / cellDistance(parents, start, cell);
                    }
                    return pinkyPenalty * 1 / cellDistance(parents, start, cell);
                }
            }

            for (Pair<Integer, Integer> neighbor : graph.get(cell)) {
                if (visited.get(neighbor) == null) {
                    visited.put(neighbor, true);
                    parents.put(neighbor, cell);
                    queue.add(neighbor);
                }
            }
        }
        return 0;
    }

    private float pelletEval(Pair<Integer, Integer> start) {
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        HashMap<Pair<Integer, Integer>, Pair<Integer,Integer>> parents = new HashMap<>();
        HashMap<Pair<Integer, Integer>, Boolean> visited = new HashMap<>();
        queue.add(start); // X i Y obrnuti jer ovako predstavljaju [row][col] u matrici
        visited.put(start, true);
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> cell = queue.remove();

            if (this.gameState.getFields()[cell.getKey()][cell.getValue()] == FieldState.PELLET) {
                return pelletReward * 1 / cellDistance(parents, start, cell);
            }

            for (Pair<Integer, Integer> neighbor : graph.get(cell)) {
                if (visited.get(neighbor) == null) {
                    visited.put(neighbor, true);
                    parents.put(neighbor, cell);
                    queue.add(neighbor);
                }
            }
        }
        return 0;
    }

    private Runnable chaseClosestPellet(Pair<Integer, Integer> start) {
        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        HashMap<Pair<Integer, Integer>, Pair<Integer,Integer>> parents = new HashMap<>();
        HashMap<Pair<Integer, Integer>, Boolean> visited = new HashMap<>();
        queue.add(start); // X i Y obrnuti jer ovako predstavljaju [row][col] u matrici
        visited.put(start, true);
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> cell = queue.remove();
            if (this.gameState.getFields()[cell.getKey()][cell.getValue()] == FieldState.PELLET) {
                return backtrack(parents, start, cell);
            }
            for (Pair<Integer, Integer> neighbor : graph.get(cell)) {
                if (visited.get(neighbor) == null) {
                    visited.put(neighbor, true);
                    parents.put(neighbor, cell);
                    queue.add(neighbor);
                }
            }
        }
        return nextMove;
    }

    // given a parents hashmap this method goes backwards from end to its parent until it reaches start
    // and returns a move method that would initiate that whole path [start->end]
    private Runnable backtrack(HashMap<Pair<Integer, Integer>, Pair<Integer,Integer>> parents, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        while(parents.get(end) != start) {
            end = parents.get(end);
        }
        List<AvailableStruct> availableFields = getAvailableFields();
        int targetX = end.getKey();
        int targetY = end.getValue();
        AvailableStruct move = availableFields.stream()
                        .min(Comparator.comparingDouble(x -> distance(x.gridPosition.getKey(), x.gridPosition.getValue(), targetX, targetY)))
                        .orElse(availableFields.get(0));
        nextMove = move.method;
        return nextMove;
    }

    private float cellDistance(HashMap<Pair<Integer, Integer>, Pair<Integer,Integer>> parents, Pair<Integer, Integer> start, Pair<Integer, Integer> end) {
        float distance = 1;
        if (parents.get(end) == null) return 0.000001f;
        while(parents.get(end) != start) {
            end = parents.get(end);
            distance += 1;
        }
        return distance;
    }

    @Override
    public String toString() {
        return chromosome.toString();
    }
}
