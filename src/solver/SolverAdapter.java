package solver;

import model.Cube;
import model.face.Direction;
import model.face.Face;
import javafx.scene.paint.Color;

public class SolverAdapter {

    public static String cubeToFaceletString(Cube cube) {
        StringBuilder facelets = new StringBuilder(54);

        appendFace(facelets, cube.faceMap.get(Direction.UP));
        appendFace(facelets, cube.faceMap.get(Direction.RIGHT));
        appendFace(facelets, cube.faceMap.get(Direction.FRONT));
        appendFace(facelets, cube.faceMap.get(Direction.DOWN));
        appendFace(facelets, cube.faceMap.get(Direction.LEFT));
        appendFace(facelets, cube.faceMap.get(Direction.BACK));

        return facelets.toString();
    }

    private static void appendFace(StringBuilder sb, Face face) {
        Color[][] tiles = face.getTiles();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                sb.append(colorToChar(tiles[row][col]));
            }
        }
    }

    private static char colorToChar(Color color) {
        if (Color.YELLOW.equals(color)) return 'U';
        if (Color.RED.equals(color)) return 'R';
        if (Color.GREEN.equals(color)) return 'F';
        if (Color.WHITE.equals(color)) return 'D';
        if (Color.ORANGE.equals(color)) return 'L';
        if (Color.BLUE.equals(color)) return 'B';
        throw new IllegalArgumentException("unknown color");
    }
}