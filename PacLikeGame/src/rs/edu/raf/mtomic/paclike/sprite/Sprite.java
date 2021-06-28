package rs.edu.raf.mtomic.paclike.sprite;

// downloaded from https://www.spriters-resource.com/arcade/pacman/sheet/52631/


public enum Sprite {
    PELLET(8, 8, 8, 8),
    EMPTY(0, 88, 8, 8),
    DOOM(600, 192, 15, 15),
    WALL(228, 0, 224, 248),
    //    WALL(664, 18, 8, 8),
    BLINKY_RIGHT_OPEN(456, 64, 15, 15),
    BLINKY_RIGHT_CLOSED(472, 64, 15, 15),
    BLINKY_LEFT_OPEN(488, 64, 15, 15),
    BLINKY_LEFT_CLOSED(504, 64, 15, 15),
    BLINKY_UP_OPEN(520, 64, 15, 15),
    BLINKY_UP_CLOSED(536, 64, 15, 15),
    BLINKY_DOWN_OPEN(552, 64, 15, 15),
    BLINKY_DOWN_CLOSED(568, 64, 15, 15),
    PINKY_RIGHT_OPEN(456, 80, 15, 15),
    PINKY_RIGHT_CLOSED(472, 80, 15, 15),
    PINKY_LEFT_OPEN(488, 80, 15, 15),
    PINKY_LEFT_CLOSED(504, 80, 15, 15),
    PINKY_UP_OPEN(520, 80, 15, 15),
    PINKY_UP_CLOSED(536, 80, 15, 15),
    PINKY_DOWN_OPEN(552, 80, 15, 15),
    PINKY_DOWN_CLOSED(568, 80, 15, 15),
    INKY_RIGHT_OPEN(456, 96, 15, 15),
    INKY_RIGHT_CLOSED(472, 96, 15, 15),
    INKY_LEFT_OPEN(488, 96, 15, 15),
    INKY_LEFT_CLOSED(504, 96, 15, 15),
    INKY_UP_OPEN(520, 96, 15, 15),
    INKY_UP_CLOSED(536, 96, 15, 15),
    INKY_DOWN_OPEN(552, 96, 15, 15),
    INKY_DOWN_CLOSED(568, 96, 15, 15),
    CLYDE_RIGHT_OPEN(456, 112, 15, 15),
    CLYDE_RIGHT_CLOSED(472, 112, 15, 15),
    CLYDE_LEFT_OPEN(488, 112, 15, 15),
    CLYDE_LEFT_CLOSED(504, 112, 15, 15),
    CLYDE_UP_OPEN(520, 112, 15, 15),
    CLYDE_UP_CLOSED(536, 112, 15, 15),
    CLYDE_DOWN_OPEN(552, 112, 15, 15),
    CLYDE_DOWN_CLOSED(568, 112, 15, 15),
    PAC_RIGHT_OPEN(456, 32, 15, 15),
    PAC_RIGHT_CLOSED(472, 32, 15, 15),
    PAC_LEFT_OPEN(456, 16, 15, 15),
    PAC_LEFT_CLOSED(472, 16, 15, 15),
    PAC_UP_OPEN(456, 0, 15, 15),
    PAC_UP_CLOSED(472, 0, 15, 15),
    PAC_DOWN_OPEN(456, 48, 15, 15),
    PAC_DOWN_CLOSED(472, 48, 15, 15),
    PAC_FULL_CLOSED(488, 0, 15, 15);

    private final int x;
    private final int y;
    private final int w;
    private final int h;

    Sprite(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
