package rs.edu.raf.mtomic.paclike;

import com.sun.javafx.UnmodifiableArrayList;
import rs.edu.raf.mtomic.paclike.agent.PlayingAgent;
import rs.edu.raf.mtomic.paclike.agent.ghost.*;
import rs.edu.raf.mtomic.paclike.agent.player.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GameState {

    private final FieldState[][] fields = new FieldState[28][31];
    private final List<PlayingAgent> agents;
    private final List<Ghost> ghosts;

    public GameState(Player player) {
        fillFieldStates();
        ghosts = new UnmodifiableArrayList<>(
                new Ghost[] {new Blinky(this),
                        new Pinky(this),
                        new Inky(this),
                        new Clyde(this)}, 4);
        agents = new UnmodifiableArrayList<>(
                new PlayingAgent[]{ghosts.get(0),
                        ghosts.get(1),
                        ghosts.get(2),
                        ghosts.get(3),
                        player}, 5);
        player.setGameState(this);
    }

    public final List<Ghost> getGhosts() {
        return ghosts;
    }

    public final List<PlayingAgent> getAgents() {
        return agents;
    }

    public final FieldState[][] getFields() {
        return fields.clone();
    }

    private void fillFieldStates() {
        try (Scanner sc = new Scanner(new File("level.txt"))) {
            for (int row = 0; row < 31; row++) {
                String next = sc.nextLine();
                for (int column = 0; column < 28; column++) {
                    switch (next.charAt(column)) {
                        case '*':
                            fields[column][row] = FieldState.BLOCKED;
                            break;
                        case 'p':
                            fields[column][row] = FieldState.PELLET;
                            break;
                        case ' ':
                            fields[column][row] = FieldState.EMPTY;
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
