package model;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import model.face.*;
import java.util.*;
import static model.face.FaceUtils.*;
import static model.face.FaceUtils.getFace;

public class Cube extends Group {
    public final List<Cubie> allCubies = new ArrayList<>();
    public final Face front = new Face(Color.RED);
    public final Face back = new Face(Color.ORANGE);
    public final Face left = new Face(Color.BLUE);
    public final Face right = new Face(Color.GREEN);
    public final Face up = new Face(Color.WHITE);
    public final Face down = new Face(Color.YELLOW);
    public final Map<Direction, Face> faceMap = new HashMap<>();

    public Cube() {
        System.out.println("Call Cube");

        final int SIZE = 50;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Cubie cubie = new Cubie(x, y, z, SIZE);
                    this.getChildren().add(cubie);
                    allCubies.add(cubie);
                }
            }
        }

        faceMap.put(Direction.FRONT, front);
        faceMap.put(Direction.BACK, back);
        faceMap.put(Direction.LEFT, left);
        faceMap.put(Direction.RIGHT, right);
        faceMap.put(Direction.UP, up);
        faceMap.put(Direction.DOWN, down);
        faceMap.put(Direction.MIDDLEX, null); // 中層可以根據需要補上虛擬面
        faceMap.put(Direction.MIDDLEY, null);
        faceMap.put(Direction.MIDDLEZ, null);
    }

    public void applyRotation(Direction face, boolean clockwise) {
        RotationMapping mapping = RotationTable.table.get(face.name() + "_" + clockwise);
        System.out.println("applyRotation: " + face + clockwise);

        clockwise = mapping.clockwise;
        // 1. 旋轉中心面（跳過中層）
        if (face != Direction.MIDDLEX && face != Direction.MIDDLEY && face != Direction.MIDDLEZ) {
            Face centerFace = getFace(face, faceMap);
            centerFace.rotate(clockwise);
        }

        // 2. 讀取四條邊，根據 reversed 進行處理
        List<Color[]> edges = new ArrayList<>();
        for (EdgeMapping e : mapping.edges) {
            Color[] colors = getEdge(getFace(e.face, faceMap), e.index, e.isRow);
            if (e.reversed) reverse(colors);
            edges.add(colors);
        }

        // 3. 寫回四條邊（依照方向搬移一格）
        for (int i = 0; i < 4; i++) {
            EdgeMapping target = mapping.edges[(i + 1) % 4];
            Color[] src = edges.get(i);
            if (target.reversed) reverse(src);
            setEdge(getFace(target.face, faceMap), target.index, target.isRow, src);
        }
    }

    public Optional<Cubie> findCubie(int x, int y, int z) {
        double gridSize = 50;
        double epsilon = 1e-3;

        double targetX = x * gridSize;
        double targetY = y * gridSize;
        double targetZ = z * gridSize;

        return allCubies.stream()
                .filter(c -> Math.abs(c.getTranslateX() - targetX) < epsilon &&
                        Math.abs(c.getTranslateY() - targetY) < epsilon &&
                        Math.abs(c.getTranslateZ() - targetZ) < epsilon)
                .findFirst();
    }

    public void printAllFaces(Map<Direction, Face> faceMap) {
        for (Direction dir : List.of(
                Direction.UP, Direction.DOWN,
                Direction.FRONT, Direction.BACK,
                Direction.LEFT, Direction.RIGHT)) {

            Face face = faceMap.get(dir);
            if (face == null) continue;

            System.out.println(dir.name());
            printFace(face);
            System.out.println();
        }
    }

    private void printFace(Face face) {
        Color[][] tiles = face.getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(colorToChar(tiles[i][j]) + " ");
            }
            System.out.println();
        }
    }

    private String colorToChar(Color color) {
        if (Color.WHITE.equals(color)) return "W";
        if (Color.YELLOW.equals(color)) return "Y";
        if (Color.RED.equals(color)) return "R";
        if (Color.ORANGE.equals(color)) return "O";
        if (Color.BLUE.equals(color)) return "B";
        if (Color.GREEN.equals(color)) return "G";
        return "?";
    }
}