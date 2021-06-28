package rs.edu.raf.mtomic.paclike;

public class MathUtils {

    public static double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(
                (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)
        );
    }

    public static int nextLeftGridX(int gridX) {
        return gridX > 0 ? gridX - 1 : 27;
    }

    public static int nextRightGridX(int gridX) {
        return gridX < 27 ? gridX + 1 : 0;
    }

}
