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

    // 4d array: [up-down][front-back][left-right][face_direction]
    private Color[][][][] cubieColors = new Color[3][3][3][6];

    // face direction mapping for the [6] index
    public static final int UP_FACE = 0;
    public static final int DOWN_FACE = 1;
    public static final int FRONT_FACE = 2;
    public static final int BACK_FACE = 3;
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
        copyFaceColors(Direction.FRONT, FRONT_FACE);
        copyFaceColors(Direction.BACK, BACK_FACE);
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

        if (x == 0) visibleFaces.add(UP_FACE);
        if (x == 2) visibleFaces.add(DOWN_FACE);
        if (y == 2) visibleFaces.add(FRONT_FACE);
        if (y == 0) visibleFaces.add(BACK_FACE);
        if (z == 0) visibleFaces.add(LEFT_FACE);
        if (z == 2) visibleFaces.add(RIGHT_FACE);

        return visibleFaces;
    }

    public static boolean isVisibleFace(int x, int y, int z, int faceIndex) {
        return getVisibleFaces(x, y, z).contains(faceIndex);
    }

    public static int directionToFaceIndex(Direction direction) {
        return switch (direction) {
            case UP -> UP_FACE;
            case DOWN -> DOWN_FACE;
            case FRONT -> FRONT_FACE;
            case BACK -> BACK_FACE;
            case LEFT -> LEFT_FACE;
            case RIGHT -> RIGHT_FACE;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
    }

    public static Direction faceIndexToDirection(int faceIndex) {
        return switch (faceIndex) {
            case UP_FACE -> Direction.UP;
            case DOWN_FACE -> Direction.DOWN;
            case FRONT_FACE -> Direction.FRONT;
            case BACK_FACE -> Direction.BACK;
            case LEFT_FACE -> Direction.LEFT;
            case RIGHT_FACE -> Direction.RIGHT;
            default -> throw new IllegalArgumentException("Invalid face index: " + faceIndex);
        };
    }

    // step 1: white cross analysis
    public EdgePieceInfo findWhiteEdge(Color adjacentColor) {
        updateCubieColors(); // refresh state before analysis

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (isEdgeCubie(x, y, z)) {
                        List<Integer> visibleFaces = getVisibleFaces(x, y, z);

                        if (visibleFaces.size() == 2) {
                            Color color1 = getCubieColorAt(x, y, z, visibleFaces.get(0));
                            Color color2 = getCubieColorAt(x, y, z, visibleFaces.get(1));

                            if ((Color.WHITE.equals(color1) && adjacentColor.equals(color2)) ||
                                    (adjacentColor.equals(color1) && Color.WHITE.equals(color2))) {

                                return createWhiteEdgePieceInfo(x, y, z, color1, color2, visibleFaces, adjacentColor);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private EdgePieceInfo createWhiteEdgePieceInfo(int x, int y, int z, Color color1, Color color2,
                                                   List<Integer> visibleFaces, Color adjacentColor) {
        int currentFace1 = visibleFaces.get(0);
        int currentFace2 = visibleFaces.get(1);

        int[] whiteTarget = getWhiteEdgeTarget4D(adjacentColor);
        int[] adjacentTarget = getAdjacentColorTarget4D(adjacentColor);

        Color whiteColor, otherColor;
        int whiteFace, otherFace;

        if (Color.WHITE.equals(color1)) {
            whiteColor = color1;
            otherColor = color2;
            whiteFace = currentFace1;
            otherFace = currentFace2;
        } else {
            whiteColor = color2;
            otherColor = color1;
            whiteFace = currentFace2;
            otherFace = currentFace1;
        }

        return new EdgePieceInfo(
                whiteColor, otherColor,
                x, y, z, whiteFace,
                x, y, z, otherFace,
                whiteTarget[0], whiteTarget[1], whiteTarget[2], whiteTarget[3],
                adjacentTarget[0], adjacentTarget[1], adjacentTarget[2], adjacentTarget[3]
        );
    }

    private int[] getWhiteEdgeTarget4D(Color adjacentColor) {
        if (Color.RED.equals(adjacentColor)) {
            return new int[]{2, 1, 2, DOWN_FACE};
        } else if (Color.ORANGE.equals(adjacentColor)) {
            return new int[]{2, 1, 0, DOWN_FACE};
        } else if (Color.BLUE.equals(adjacentColor)) {
            return new int[]{2, 0, 1, DOWN_FACE};
        } else if (Color.GREEN.equals(adjacentColor)) {
            return new int[]{2, 2, 1, DOWN_FACE};
        }
        throw new IllegalArgumentException("Unknown adjacent color: " + adjacentColor);
    }

    private int[] getAdjacentColorTarget4D(Color adjacentColor) {
        if (Color.RED.equals(adjacentColor)) {
            return new int[]{2, 1, 2, RIGHT_FACE};
        } else if (Color.ORANGE.equals(adjacentColor)) {
            return new int[]{2, 1, 0, LEFT_FACE};
        } else if (Color.BLUE.equals(adjacentColor)) {
            return new int[]{2, 0, 1, FRONT_FACE};
        } else if (Color.GREEN.equals(adjacentColor)) {
            return new int[]{2, 2, 1, BACK_FACE};
        }
        throw new IllegalArgumentException("Unknown adjacent color: " + adjacentColor);
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

        boolean hasWhiteCross =
                Color.WHITE.equals(getCubieColorAt(2, 1, 0, DOWN_FACE)) &&
                        Color.WHITE.equals(getCubieColorAt(2, 1, 2, DOWN_FACE)) &&
                        Color.WHITE.equals(getCubieColorAt(2, 0, 1, DOWN_FACE)) &&
                        Color.WHITE.equals(getCubieColorAt(2, 2, 1, DOWN_FACE));

        if (!hasWhiteCross) return false;

        return Color.ORANGE.equals(getCubieColorAt(2, 1, 0, LEFT_FACE)) &&
                Color.RED.equals(getCubieColorAt(2, 1, 2, RIGHT_FACE)) &&
                Color.BLUE.equals(getCubieColorAt(2, 0, 1, FRONT_FACE)) &&
                Color.GREEN.equals(getCubieColorAt(2, 2, 1, BACK_FACE));
    }

    // step 2: white corner analysis

    public CornerPieceInfo findWhiteCorner(Color color1, Color color2) {
        updateCubieColors();

        for (int x = 0; x < 3; x += 2) {
            for (int y = 0; y < 3; y += 2) {
                for (int z = 0; z < 3; z += 2) {
                    if (isCornerCubie(x, y, z)) {
                        List<Integer> visibleFaces = getVisibleFaces(x, y, z);

                        if (visibleFaces.size() == 3) {
                            Color faceColor1 = getCubieColorAt(x, y, z, visibleFaces.get(0));
                            Color faceColor2 = getCubieColorAt(x, y, z, visibleFaces.get(1));
                            Color faceColor3 = getCubieColorAt(x, y, z, visibleFaces.get(2));

                            List<Color> colors = List.of(faceColor1, faceColor2, faceColor3);
                            if (colors.contains(Color.WHITE) && colors.contains(color1) && colors.contains(color2)) {
                                return createWhiteCornerPieceInfo(x, y, z, faceColor1, faceColor2, faceColor3,
                                        visibleFaces, color1, color2);
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private CornerPieceInfo createWhiteCornerPieceInfo(int x, int y, int z, Color color1, Color color2, Color color3,
                                                       List<Integer> visibleFaces, Color targetColor1, Color targetColor2) {
        int currentFace1 = visibleFaces.get(0);
        int currentFace2 = visibleFaces.get(1);
        int currentFace3 = visibleFaces.get(2);

        int[] targetPos = getWhiteCornerTarget4D(targetColor1, targetColor2);

        int targetX = targetPos[0], targetY = targetPos[1], targetZ = targetPos[2];

        return new CornerPieceInfo(
                color1, color2, color3,
                x, y, z, currentFace1,
                x, y, z, currentFace2,
                x, y, z, currentFace3,
                targetX, targetY, targetZ, DOWN_FACE,
                targetX, targetY, targetZ, directionToFaceIndex(getColorDirection(targetColor1)),
                targetX, targetY, targetZ, directionToFaceIndex(getColorDirection(targetColor2))
        );
    }

    private int[] getWhiteCornerTarget4D(Color color1, Color color2) {
        if ((Color.RED.equals(color1) || Color.RED.equals(color2)) &&
                (Color.BLUE.equals(color1) || Color.BLUE.equals(color2))) {
            return new int[]{2, 0, 2};
        } else if ((Color.RED.equals(color1) || Color.RED.equals(color2)) &&
                (Color.GREEN.equals(color1) || Color.GREEN.equals(color2))) {
            return new int[]{2, 2, 2};
        } else if ((Color.ORANGE.equals(color1) || Color.ORANGE.equals(color2)) &&
                (Color.BLUE.equals(color1) || Color.BLUE.equals(color2))) {
            return new int[]{2, 0, 0};
        } else if ((Color.ORANGE.equals(color1) || Color.ORANGE.equals(color2)) &&
                (Color.GREEN.equals(color1) || Color.GREEN.equals(color2))) {
            return new int[]{2, 2, 0};
        }
        throw new IllegalArgumentException("Unknown color combination: " + color1 + ", " + color2);
    }

    private Direction getColorDirection(Color color) {
        if (Color.RED.equals(color)) return Direction.RIGHT;
        if (Color.ORANGE.equals(color)) return Direction.LEFT;
        if (Color.BLUE.equals(color)) return Direction.FRONT;
        if (Color.GREEN.equals(color)) return Direction.BACK;
        if (Color.YELLOW.equals(color)) return Direction.UP;
        if (Color.WHITE.equals(color)) return Direction.DOWN;
        throw new IllegalArgumentException("Unknown color: " + color);
    }

    public List<CornerPieceInfo> findAllWhiteCorners() {
        List<CornerPieceInfo> whiteCorners = new ArrayList<>();

        Color[][] cornerCombinations = {
                {Color.RED, Color.BLUE}, {Color.RED, Color.GREEN},
                {Color.ORANGE, Color.BLUE}, {Color.ORANGE, Color.GREEN}
        };

        for (Color[] combo : cornerCombinations) {
            CornerPieceInfo corner = findWhiteCorner(combo[0], combo[1]);
            if (corner != null) {
                whiteCorners.add(corner);
            }
        }

        return whiteCorners;
    }

    public boolean isFirstLayerComplete() {
        return isWhiteCrossComplete() && areWhiteCornersComplete();
    }

    private boolean areWhiteCornersComplete() {
        updateCubieColors();

        return Color.WHITE.equals(getCubieColorAt(2, 0, 0, DOWN_FACE)) &&
                Color.WHITE.equals(getCubieColorAt(2, 0, 2, DOWN_FACE)) &&
                Color.WHITE.equals(getCubieColorAt(2, 2, 0, DOWN_FACE)) &&
                Color.WHITE.equals(getCubieColorAt(2, 2, 2, DOWN_FACE));
    }

    // step 3: middle layer analysis
    public EdgePieceInfo findMiddleLayerEdge(Color color1, Color color2) {
        updateCubieColors();

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (isEdgeCubie(x, y, z)) {
                        List<Integer> visibleFaces = getVisibleFaces(x, y, z);

                        if (visibleFaces.size() == 2) {
                            Color edgeColor1 = getCubieColorAt(x, y, z, visibleFaces.get(0));
                            Color edgeColor2 = getCubieColorAt(x, y, z, visibleFaces.get(1));

                            if ((color1.equals(edgeColor1) && color2.equals(edgeColor2)) ||
                                    (color2.equals(edgeColor1) && color1.equals(edgeColor2))) {

                                if (!Color.WHITE.equals(edgeColor1) && !Color.WHITE.equals(edgeColor2) &&
                                        !Color.YELLOW.equals(edgeColor1) && !Color.YELLOW.equals(edgeColor2)) {

                                    return createMiddleLayerEdgePieceInfo(x, y, z, edgeColor1, edgeColor2, visibleFaces);
                                }
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    private EdgePieceInfo createMiddleLayerEdgePieceInfo(int x, int y, int z, Color color1, Color color2,
                                                         List<Integer> visibleFaces) {
        int currentFace1 = visibleFaces.get(0);
        int currentFace2 = visibleFaces.get(1);

        int[] target1 = getMiddleLayerTarget4D(color1);
        int[] target2 = getMiddleLayerTarget4D(color2);

        return new EdgePieceInfo(
                color1, color2,
                x, y, z, currentFace1,
                x, y, z, currentFace2,
                target1[0], target1[1], target1[2], target1[3],
                target2[0], target2[1], target2[2], target2[3]
        );
    }

    private int[] getMiddleLayerTarget4D(Color color) {
        if (Color.RED.equals(color)) {
            return new int[]{1, 1, 2, RIGHT_FACE};
        } else if (Color.ORANGE.equals(color)) {
            return new int[]{1, 1, 0, LEFT_FACE};
        } else if (Color.BLUE.equals(color)) {
            return new int[]{1, 0, 1, FRONT_FACE};
        } else if (Color.GREEN.equals(color)) {
            return new int[]{1, 2, 1, BACK_FACE};
        }
        throw new IllegalArgumentException("Unknown color for middle layer: " + color);
    }

    public List<EdgePieceInfo> findAllMiddleLayerEdges() {
        List<EdgePieceInfo> middleEdges = new ArrayList<>();

        Color[][] edgeCombinations = {
                {Color.RED, Color.BLUE}, {Color.RED, Color.GREEN},
                {Color.ORANGE, Color.BLUE}, {Color.ORANGE, Color.GREEN}
        };

        for (Color[] combo : edgeCombinations) {
            EdgePieceInfo edge = findMiddleLayerEdge(combo[0], combo[1]);
            if (edge != null) {
                middleEdges.add(edge);
            }
        }

        return middleEdges;
    }

    public boolean isMiddleLayerComplete() {
        updateCubieColors();

        List<EdgePieceInfo> middleEdges = findAllMiddleLayerEdges();
        return middleEdges.size() == 4 && middleEdges.stream().allMatch(EdgePieceInfo::isInCorrectPosition);
    }

    // step 4: yellow cross
    public YellowCrossPattern analyzeYellowCrossPattern() {
        updateCubieColors();

        boolean leftEdge = Color.YELLOW.equals(getCubieColorAt(0, 1, 0, UP_FACE));
        boolean rightEdge = Color.YELLOW.equals(getCubieColorAt(0, 1, 2, UP_FACE));
        boolean FRONTEdge = Color.YELLOW.equals(getCubieColorAt(0, 0, 1, UP_FACE));
        boolean BACKEdge = Color.YELLOW.equals(getCubieColorAt(0, 2, 1, UP_FACE));

        int yellowCount = (leftEdge ? 1 : 0) + (rightEdge ? 1 : 0) + (FRONTEdge ? 1 : 0) + (BACKEdge ? 1 : 0);

        return switch (yellowCount) {
            case 0 -> YellowCrossPattern.DOT;
            case 2 -> {
                if ((leftEdge && rightEdge) || (FRONTEdge && BACKEdge)) {
                    yield YellowCrossPattern.LINE;
                } else {
                    yield YellowCrossPattern.L_SHAPE;
                }
            }
            case 4 -> YellowCrossPattern.CROSS;
            default -> YellowCrossPattern.DOT;
        };
    }

    public boolean isYellowCrossComplete() {
        return analyzeYellowCrossPattern() == YellowCrossPattern.CROSS;
    }

    // step 5: yellow corners
    public int countCorrectlyOrientedYellowCorners() {
        updateCubieColors();

        int count = 0;

        if (Color.YELLOW.equals(getCubieColorAt(0, 0, 0, UP_FACE))) count++;
        if (Color.YELLOW.equals(getCubieColorAt(0, 0, 2, UP_FACE))) count++;
        if (Color.YELLOW.equals(getCubieColorAt(0, 2, 0, UP_FACE))) count++;
        if (Color.YELLOW.equals(getCubieColorAt(0, 2, 2, UP_FACE))) count++;

        return count;
    }

    public boolean areAllCornersInCorrectPosition() {
        updateCubieColors();
        return countCorrectlyOrientedYellowCorners() == 4;
    }

    public boolean areAllEdgesInCorrectPosition() {
        updateCubieColors();
        return isYellowCrossComplete();
    }

    // debug methods
    public void printFaceColors() {
        String[] faceNames = {"UP", "DOWN", "FRONT", "BACK", "LEFT", "RIGHT"};

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
    public void debugPieceStates() {
        System.out.println("\n" + "=".repeat(30));
        System.out.println("DEBUG: Piece States");
        System.out.println("=".repeat(30));

        // white edges
        System.out.println("White edges:");
        List<EdgePieceInfo> whiteEdges = findAllWhiteEdges();
        for (EdgePieceInfo edge : whiteEdges) {
            int x = edge.currentX1, y = edge.currentY1, z = edge.currentZ1;
            System.out.println(formatCubieState(x, y, z));
        }

        // white corners
        System.out.println("White corners:");
        List<CornerPieceInfo> whiteCorners = findAllWhiteCorners();
        for (CornerPieceInfo corner : whiteCorners) {
            int x = corner.currentX1, y = corner.currentY1, z = corner.currentZ1;
            System.out.println(formatCubieState(x, y, z));
        }

        // middle layer edges
        System.out.println("Middle layer edges:");
        List<EdgePieceInfo> middleEdges = findAllMiddleLayerEdges();
        for (EdgePieceInfo edge : middleEdges) {
            int x = edge.currentX1, y = edge.currentY1, z = edge.currentZ1;
            System.out.println(formatCubieState(x, y, z));
        }

        // yellow edges
        System.out.println("Yellow edges:");
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                for (int z = 0; z < 3; z++) {
                    if (isEdgeCubie(x, y, z)) {
                        // check if this edge has yellow
                        boolean hasYellow = false;
                        for (int face = 0; face < 6; face++) {
                            Color color = getCubieColorAt(x, y, z, face);
                            if (Color.YELLOW.equals(color)) {
                                hasYellow = true;
                                break;
                            }
                        }
                        if (hasYellow) {
                            System.out.println(formatCubieState(x, y, z));
                        }
                    }
                }
            }
        }

        // yellow corners
        System.out.println("Yellow corners:");
        for (int x = 0; x < 3; x += 2) {
            for (int y = 0; y < 3; y += 2) {
                for (int z = 0; z < 3; z += 2) {
                    if (isCornerCubie(x, y, z)) {
                        // check if this corner has yellow
                        boolean hasYellow = false;
                        for (int face = 0; face < 6; face++) {
                            Color color = getCubieColorAt(x, y, z, face);
                            if (Color.YELLOW.equals(color)) {
                                hasYellow = true;
                                break;
                            }
                        }
                        if (hasYellow) {
                            System.out.println(formatCubieState(x, y, z));
                        }
                    }
                }
            }
        }

        System.out.println("=".repeat(30) + "\n");
    }

    private String formatCubieState(int x, int y, int z) {
        // Format: (x, y, z)(UP, DOWN, FRONT, BACK, LEFT, RIGHT)
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("(%d, %d, %d)(", x, y, z));

        // Face order: UP=0, DOWN=1, FRONT=2, BACK=3, LEFT=4, RIGHT=5
        for (int face = 0; face < 6; face++) {
            Color color = getCubieColorAt(x, y, z, face);
            if (color == null) {
                sb.append("null");
            } else {
                sb.append(colorToChar(color));
            }
            if (face < 5) sb.append(", ");
        }
        sb.append(")");

        return sb.toString();
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

    public void testPieceFinding() {
        System.out.println("=== Testing Piece Finding ===");

        System.out.println("White edges:");
        List<EdgePieceInfo> whiteEdges = findAllWhiteEdges();
        for (EdgePieceInfo edge : whiteEdges) {
            System.out.println("  " + edge);
        }

        System.out.println("White corners:");
        List<CornerPieceInfo> whiteCorners = findAllWhiteCorners();
        for (CornerPieceInfo corner : whiteCorners) {
            System.out.println("  " + corner);
        }

        System.out.println("Yellow cross pattern: " + analyzeYellowCrossPattern());
        System.out.println("Correctly oriented yellow corners: " + countCorrectlyOrientedYellowCorners());
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