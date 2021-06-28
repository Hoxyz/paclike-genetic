package rs.edu.raf.mtomic.paclike.agent;

import rs.edu.raf.mtomic.paclike.Direction;
import rs.edu.raf.mtomic.paclike.FieldState;
import rs.edu.raf.mtomic.paclike.GameState;
import rs.edu.raf.mtomic.paclike.sprite.Sprite;
import rs.edu.raf.mtomic.paclike.sprite.SpriteLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public abstract class PlayingAgent {
    protected int RIGHT_OFFSET = 0;
    protected int LEFT_OFFSET = 2;
    protected int UP_OFFSET = 4;
    protected int DOWN_OFFSET = 6;
    protected int currentOffset = RIGHT_OFFSET;

    protected Sprite[] sprites = new Sprite[8];
    protected BufferedImage[] images;
    protected int activeSpriteId;
    protected int spriteCenterX;
    protected int spriteCenterY;
    protected int speed = 1; // frames for 1 pixel movement
    protected GameState gameState;
    private int frameCounter = 0;
    private Direction currentDirection = Direction.LEFT;

    public PlayingAgent(GameState gameState) {
        this.gameState = gameState;
    }

    public BufferedImage getActiveSpriteImage() {
        return images[activeSpriteId];
    }

    public final void updateSprite() {
        activeSpriteId = currentOffset + ((1 + activeSpriteId) % (sprites.length / 4));
    }

    private boolean move() {
        frameCounter = (frameCounter + 1) % speed;
        return frameCounter == 0;
    }

    private boolean checkAvailableCoords(int centerX, int centerY) {
        if (centerX < 4) {
            centerX = 28 * 8 - 1;
        } else if (centerX > 28 * 8 - 1) {
            centerX = 4;
        }

        int gridX = getGridX(centerX);
        int gridY = getGridY(centerY);
        return !gameState.getFields()[gridX][gridY].equals(FieldState.BLOCKED);
    }

    private void adjustSpriteCenterY() {
        int mod = spriteCenterY % 8 - 4;
        if (mod > 0) {
            spriteCenterY--;
        }
        if (mod < 0) {
            spriteCenterY++;
        }
    }

    private void adjustSpriteCenterX() {
        int mod = spriteCenterX % 8 - 4;
        if (mod > 0) {
            spriteCenterX--;
        }
        if (mod < 0) {
            spriteCenterX++;
        }
    }

    public final void goLeft() {
        currentOffset = LEFT_OFFSET;
        currentDirection = Direction.LEFT;
        if (move() && checkAvailableCoords(spriteCenterX - 4, spriteCenterY)) {
            spriteCenterX--;
        }
        if (spriteCenterX < 4) {
            spriteCenterX = 28 * 8 - 1;
        }
        adjustSpriteCenterY();
    }

    public final void goRight() {
        currentOffset = RIGHT_OFFSET;
        currentDirection = Direction.RIGHT;
        if (move() && checkAvailableCoords(spriteCenterX + 4, spriteCenterY)) {
            spriteCenterX++;
        }
        if (spriteCenterX > 28 * 8 - 1) {
            spriteCenterX = 4;
        }
        adjustSpriteCenterY();
    }

    public final void goDown() {
        currentOffset = DOWN_OFFSET;
        currentDirection = Direction.DOWN;
        if (move() && checkAvailableCoords(spriteCenterX, spriteCenterY + 4)) {
            spriteCenterY++;
        }
        adjustSpriteCenterX();
    }

    public final void goUp() {
        currentOffset = UP_OFFSET;
        currentDirection = Direction.UP;
        if (move() && checkAvailableCoords(spriteCenterX, spriteCenterY - 4)) {
            spriteCenterY--;
        }
        adjustSpriteCenterX();
    }

    public final int getGridX() {
        return spriteCenterX / 8;
    }

    public final int getGridX(int centerX) {
        return centerX / 8;
    }

    public final int getGridY() {
        return spriteCenterY / 8;
    }

    public final int getGridY(int centerY) {
        return centerY / 8;
    }

    public final int getSpriteTopX() {
        return spriteCenterX - 7;
    }

    public final int getSpriteTopY() {
        return spriteCenterY - 7;
    }

    public abstract void playMove();

    protected abstract void fillSprites();

    public final void loadSpriteImages(BufferedImage spriteResourceImage) {
        final Color color = new Color(spriteResourceImage.getRGB(0, 0));
        fillSprites();
        images = new BufferedImage[sprites.length];
        for (int i = 0; i < sprites.length; i++) {
            Sprite active = sprites[i];
            images[i] =
                    SpriteLoader.makeTransparent(
                            spriteResourceImage.getSubimage(active.getX(), active.getY(), active.getW(), active.getH()),
                            color
                    );
        }
    }

    public Direction getCurrentDirection() {
        return currentDirection;
    }
}
