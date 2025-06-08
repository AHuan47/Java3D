package solver;

import javafx.scene.paint.Color;
import model.Cube;
import model.face.Direction;
import model.face.Face;
import solver.cases.YellowCrossPattern;

import java.util.ArrayList;
import java.util.List;


public class CubeAnalyzer {
    private final Cube cube;

    // 4d array: [up-down][back-front][left-right][face_direction]
    private Color[][][][] cubieColors = new Color[3][3][3][6];

    // face direction mapping for the [6] index
    public static final int UP_FACE = 0;
    public static final int DOWN_FACE = 1;
    public static final int BACK_FACE = 2;
    public static final int FRONT_FACE = 3;
    public static final int LEFT_FACE = 4;
    public static final int RIGHT_FACE = 5;

    public CubeAnalyzer(Cube cube) {
        this.cube = cube;
        updateCubieColors();
    }

    public void updateCubieColors() {
        // initialize all faces as null (invisible/interior)
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    for (int face = 0; face < 6; face++) {
                        cubieColors[x][y][z][face] = null;
                    }
                }
            }
        }

        copyFaceColors(Direction.UP, UP_FACE);
        copyFaceColors(Direction.DOWN, DOWN_FACE);
        copyFaceColors(Direction.BACK, BACK_FACE);
        copyFaceColors(Direction.FRONT, FRONT_FACE);
        copyFaceColors(Direction.LEFT, LEFT_FACE);
        copyFaceColors(Direction.RIGHT, RIGHT_FACE);
    }

    private void copyFaceColors(Direction direction, int faceIndex) {
        Face face = cube.faceMap.get(direction);
        Color[][] tiles = face.getTiles();

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int[] coords = facePositionToCubieCoords(direction, row, col);
                int x = coords[0], y = coords[1], z = coords[2];
                cubieColors[x][y][z][faceIndex] = tiles[row][col];
            }
        }
    }

    private int[] facePositionToCubieCoords(Direction direction, int row, int col) {
        return switch (direction) {
            case UP -> new int[]{0, row, col};
            case DOWN -> new int[]{2, 2 - row, col};
            case FRONT -> new int[]{row, 2, col};
            case BACK -> new int[]{row, 0, 2 - col};
            case LEFT -> new int[]{row, col, 0};
            case RIGHT -> new int[]{row, 2 - col, 2};
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    public Color getCubieColorAt(int x, int y, int z, int faceIndex) {
        return cubieColors[x][y][z][faceIndex];
    }

    public void setCubieColorAt(int x, int y, int z, int faceIndex, Color color) {
        cubieColors[x][y][z][faceIndex] = color;
    }

    public Color getColorAt(Direction direction, int row, int col) {
        int[] coords = facePositionToCubieCoords(direction, row, col);
        int faceIndex = directionToFaceIndex(direction);
        return getCubieColorAt(coords[0], coords[1], coords[2], faceIndex);
    }

    public static boolean isCornerCubie(int x, int y, int z) {
        // corners have all coordinates as 0 or 2 (no middle positions)
        return (x == 0 || x == 2) && (y == 0 || y == 2) && (z == 0 || z == 2);
    }

    public static boolean isEdgeCubie(int x, int y, int z) {
        // edges have exactly one coordinate = 1
        int middleCount = 0;
        if (x == 1) middleCount++;
        if (y == 1) middleCount++;
        if (z == 1) middleCount++;
        return middleCount == 1;
    }

    public static boolean isCenterCubie(int x, int y, int z) {
        // centers have exactly two coordinates = 1
        int middleCount = 0;
        if (x == 1) middleCount++;
        if (y == 1) middleCount++;
        if (z == 1) middleCount++;
        return middleCount == 2;
    }

    public static List<Integer> getVisibleFaces(int x, int y, int z) {
        List<Integer> visibleFaces = new ArrayList<>();

        if (x == 0) visibleFaces.add(UP_FACE);    // top face visible
        if (x == 2) visibleFaces.add(DOWN_FACE);  // bottom face visible
        if (y == 0) visibleFaces.add(BACK_FACE);  // back face visible
        if (y == 2) visibleFaces.add(FRONT_FACE); // front face visible
        if (z == 0) visibleFaces.add(LEFT_FACE);  // left face visible
        if (z == 2) visibleFaces.add(RIGHT_FACE); // right face visible

        return visibleFaces;
    }

    public static boolean isVisibleFace(int x, int y, int z, int faceIndex) {
        return getVisibleFaces(x, y, z).contains(faceIndex);
    }

    public static int directionToFaceIndex(Direction direction) {
        return switch (direction) {
            case UP -> UP_FACE;
            case DOWN -> DOWN_FACE;
            case BACK -> BACK_FACE;
            case FRONT -> FRONT_FACE;
            case LEFT -> LEFT_FACE;
            case RIGHT -> RIGHT_FACE;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    public static Direction faceIndexToDirection(int faceIndex) {
        return switch (faceIndex) {
            case UP_FACE -> Direction.UP;
            case DOWN_FACE -> Direction.DOWN;
            case BACK_FACE -> Direction.BACK;
            case FRONT_FACE -> Direction.FRONT;
            case LEFT_FACE -> Direction.LEFT;
            case RIGHT_FACE -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Invalid face index: " + faceIndex);
        };
    }

    // step 1: white cross analysis
    public EdgePieceInfo findWhiteEdge(Color adjacentColor) {
        updateCubieColors(); // refresh state before analysis

        // TODO: implement - search for edge piece with white and adjacentColor
        // iterate through all positions where isEdgeCubie(x, y, z) is true
        // for each edge, check the 2 visible faces for the if it contains white
        // create and return EdgePieceInfo with current and target positions

        return null; // placeholder
    }

    public List<EdgePieceInfo> findAllWhiteEdges() {
        List<EdgePieceInfo> whiteEdges = new ArrayList<>();

        // the four white edges should have these adjacent colors
        Color[] adjacentColors = {Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN};

        for (Color adjacentColor : adjacentColors) {
            EdgePieceInfo edge = findWhiteEdge(adjacentColor);
            if (edge != null) {
                whiteEdges.add(edge);
            }
        }

        return whiteEdges;
    }

    public boolean isWhiteCrossComplete() {
        updateCubieColors();

        // TODO: implement - check if all 4 white edges are in correct positions on bottom face
        // check that DOWN face has white cross pattern at edge positions
        // check that adjacent colors match their respective center colors

        return false; // placeholder
    }

    // step 2: white corner analysis

    public CornerPieceInfo findWhiteCorner(Color color1, Color color2) {
        updateCubieColors();

        // TODO: implement - search for corner piece with white, color1, and color2
        // iterate through all positions where isCornerCubie(x, y, z) is true
        // for each corner, check the 3 visible faces for the color combination
        // create and return CornerPieceInfo with current and target positions

        return null; // placeholder
    }

    public List<CornerPieceInfo> findAllWhiteCorners() {
        List<CornerPieceInfo> whiteCorners = new ArrayList<>();

        // TODO: implement - find all 4 white corner pieces
        // standard cube has white corners with these color combinations:
        // white-Red-Blue, white-Red-Green, white-Orange-Blue, white-Orange-Green

        return whiteCorners; // placeholder
    }

    public boolean isFirstLayerComplete() {
        // TODO: implement - check if white cross + white corners are all correct
        return isWhiteCrossComplete() && areWhiteCornersComplete();
    }

    private boolean areWhiteCornersComplete() {
        // TODO: implement
        return false; // placeholder
    }

    // step 3: middle layer analysis
    public EdgePieceInfo findMiddleLayerEdge(Color color1, Color color2) {
        updateCubieColors();

        // TODO: implement - search for edge piece with color1 and color2 (no white or yellow)
        // look for edges that don't contain white or yellow colors

        return null; // placeholder
    }

    public List<EdgePieceInfo> findAllMiddleLayerEdges() {
        List<EdgePieceInfo> middleEdges = new ArrayList<>();

        // TODO: implement - find all 4 middle layer edges
        // these are edges that don't contain white or yellow

        return middleEdges; // placeholder
    }

    public boolean isMiddleLayerComplete() {
        updateCubieColors();

        // TODO: implement - check if all middle layer edges are correct

        return false; // placeholder
    }

    // step 4: yellow cross
    public YellowCrossPattern analyzeYellowCrossPattern() {
        updateCubieColors();

        // TODO: implement - analyze the yellow cross pattern on UP face
        // check the 4 edge positions on the UP face for yellow color
        // return appropriate YellowCrossPattern based on count and arrangement

        return YellowCrossPattern.DOT; // placeholder
    }

    public boolean isYellowCrossComplete() {
        return analyzeYellowCrossPattern() == YellowCrossPattern.CROSS;
    }

    // step 5: yellow corners
    public int countCorrectlyOrientedYellowCorners() {
        updateCubieColors();

        // TODO: implement - count corners with yellow on UP face
        // check all 4 corner positions on the UP face

        return 0; // placeholder
    }

    // part 6: last layer permutation

    public boolean areAllCornersInCorrectPosition() {
        updateCubieColors();

        // TODO: implement

        return false; // placeholder
    }

    public boolean areAllEdgesInCorrectPosition() {
        updateCubieColors();

        // TODO: implement

        return false; // placeholder
    }

    // debug methods
    public void printFaceColors() {
        String[] faceNames = {"UP", "DOWN", "BACK", "FRONT", "LEFT", "RIGHT"};

        updateCubieColors();

        for (int faceIndex = 0; faceIndex < 6; faceIndex++) {
            System.out.println(faceNames[faceIndex] + ":");
            Direction direction = faceIndexToDirection(faceIndex);

            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    Color color = getColorAt(direction, row, col);
                    System.out.print(colorToChar(color) + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public void printCubieState() {
        updateCubieColors();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    System.out.printf("Cubie [%d][%d][%d]: ", x, y, z);

                    List<Integer> visibleFaces = getVisibleFaces(x, y, z);
                    for (int face : visibleFaces) {
                        Color color = getCubieColorAt(x, y, z, face);
                        System.out.printf("%s:%s ", faceIndexToDirection(face).name(), colorToChar(color));
                    }

                    System.out.println();
                }
            }
        }
    }

    private String colorToChar(Color color) {
        if (color == null) return "?";
        if (Color.WHITE.equals(color)) return "W";
        if (Color.YELLOW.equals(color)) return "Y";
        if (Color.RED.equals(color)) return "R";
        if (Color.ORANGE.equals(color)) return "O";
        if (Color.BLUE.equals(color)) return "B";
        if (Color.GREEN.equals(color)) return "G";
        return "?";
    }
}