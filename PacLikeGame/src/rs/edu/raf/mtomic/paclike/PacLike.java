package rs.edu.raf.mtomic.paclike;

import rs.edu.raf.mtomic.paclike.agent.PlayingAgent;
import rs.edu.raf.mtomic.paclike.agent.player.Player;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Adapted from https://stackoverflow.com/questions/1963494/java-2d-game-graphics

/**
 * Simulator igre
 **/
public class PacLike extends Thread {
    // Ovde možete promeniti fps i da li želite renderovanje simulacije.
    private static final boolean RENDER = true;
    private static final int FPS = 150;

    // NADALJE NE MENJATI NIŠTA!
    private final GameState gameState;
    private boolean isRunning = true;
    private BufferStrategy strategy;
    private BufferedImage background;
    private Graphics2D backgroundGraphics;
    private Graphics2D graphics;
    private final JFrame frame;
    private final int width = 29 * 8;
    private final int height = 34 * 8;
    private final int scale = 2;
    private int totalPoints = 0;
    private Timer spriteUpdateTimer;
    private final GraphicsConfiguration config =
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();
    private BufferedImage backgroundImage;
    private BufferedImage pelletImage;

    // Setup
    public PacLike(Player player) {
        gameState = new GameState(player);
        // JFrame
        frame = new JFrame();
        frame.addWindowListener(new FrameClose());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setSize(width * scale, height * scale);
        frame.setTitle("PacLike - Simulator");
        if (RENDER) {
            frame.setVisible(true);

            // Canvas
            Canvas canvas = new Canvas(config);
            canvas.setSize(width * scale, height * scale);
            frame.add(canvas, 0);

            // Background & Buffer
            background = create(width, height, false);
            initSprites();
            canvas.createBufferStrategy(2);
            do {
                strategy = canvas.getBufferStrategy();
            } while (strategy == null);
        }
        start();
    }

    // create a hardware accelerated image
    public final BufferedImage create(final int width, final int height,
                                      final boolean alpha) {
        return config.createCompatibleImage(width, height, alpha
                ? Transparency.TRANSLUCENT : Transparency.OPAQUE);
    }

    private void initSprites() {
        try {
            BufferedImage mainSpritesImage = ImageIO.read(new File("pac0.png"));
            backgroundImage = mainSpritesImage.getSubimage(Sprite.WALL.getX(), Sprite.WALL.getY(),
                    Sprite.WALL.getW(), Sprite.WALL.getH());
            pelletImage = mainSpritesImage.getSubimage(Sprite.PELLET.getX(), Sprite.PELLET.getY(),
                    Sprite.PELLET.getW(), Sprite.PELLET.getH());
            for (PlayingAgent playingAgent : gameState.getAgents()) {
                playingAgent.loadSpriteImages(mainSpritesImage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Screen and buffer stuff
    private Graphics2D getBuffer() {
        if (graphics == null) {
            try {
                graphics = (Graphics2D) strategy.getDrawGraphics();
            } catch (IllegalStateException e) {
                return null;
            }
        }
        return graphics;
    }

    private boolean updateScreen() {
        graphics.dispose();
        graphics = null;
        try {
            strategy.show();
            Toolkit.getDefaultToolkit().sync();
            return (!strategy.contentsLost());

        } catch (NullPointerException | IllegalStateException e) {
            return true;
        }
    }

    public void run() {
        if (RENDER) {
            backgroundGraphics = (Graphics2D) background.getGraphics();
            spriteUpdateTimer = new Timer(125, e -> {
                for (PlayingAgent agent : gameState.getAgents()) {
                    agent.updateSprite();
                }
            });
            spriteUpdateTimer.setInitialDelay(0);
            spriteUpdateTimer.start();
        }

        long fpsWait = (long) (1.0 / FPS * 1000);
        main:
        while (isRunning) {
            updateGame();

            if (RENDER) {
                long renderStart = System.nanoTime();
                // Update Graphics
                do {
                    Graphics2D bg = getBuffer();
                    if (!isRunning) {
                        break main;
                    }
                    renderGame(backgroundGraphics); // this calls your draw method
                    // thingy
                    if (bg != null) {
                        if (scale != 1) {
                            bg.drawImage(background, 0, 0, width * scale, height
                                    * scale, 0, 0, width, height, null);
                        } else {
                            bg.drawImage(background, 0, 0, null);
                        }
                        bg.dispose();
                    }
                } while (!updateScreen());

                // Better do some FPS limiting here
                long renderTime = (System.nanoTime() - renderStart) / 1000000;
                try {
                    Thread.sleep(Math.max(0, fpsWait - renderTime));
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    break;
                }
            }
        }
        if (RENDER) {
            spriteUpdateTimer.stop();
            frame.dispose();
        }
    }

    public void updateGame() {
        // update game logic here
        for (PlayingAgent agent : gameState.getAgents()) {
            agent.playMove();
        }
        PlayingAgent playerOne = gameState.getAgents().get(gameState.getAgents().size() - 1);
        FieldState[][] fields = gameState.getFields();
        for (PlayingAgent agent : gameState.getAgents()) {
            if (agent != playerOne && agent.getGridX() == playerOne.getGridX() &&
                    agent.getGridY() == playerOne.getGridY()) {
                endGame();
                return;
            }
        }

        if (fields[playerOne.getGridX()][playerOne.getGridY()].equals(FieldState.PELLET)) {
            fields[playerOne.getGridX()][playerOne.getGridY()] = FieldState.EMPTY;
            totalPoints += 1;
        }

        if (totalPoints == 244) {
            endGame();
        }
    }

    private void endGame() {
        //System.out.println(totalPoints);
        isRunning = false;
    }

    public void renderGame(Graphics2D g) {
        g.drawImage(backgroundImage, 0, 0, null);

        FieldState[][] fields = gameState.getFields();
        for (int column = 0; column < fields.length; column++) {
            for (int row = 0; row < fields[0].length; row++) {
                if (fields[column][row].equals(FieldState.PELLET)) {
                    g.drawImage(pelletImage, column * 8, row * 8, null);
                }
            }
        }

        for (PlayingAgent agent : gameState.getAgents()) {
            g.drawImage(agent.getActiveSpriteImage(), agent.getSpriteTopX(), agent.getSpriteTopY(), null);
        }
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    private class FrameClose extends WindowAdapter {
        @Override
        public void windowClosing(final WindowEvent e) {
            isRunning = false;
        }
    }
}
