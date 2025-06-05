package model;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import model.face.*;
import model.sl.SLManager;

import java.util.*;
import static model.face.FaceUtils.*;
import static model.face.FaceUtils.getFace;

public class Cube extends Group {
    public final List<Cubie> allCubies = new ArrayList<>();

    public Face front = new Face(Color.GREEN);
    public Face back = new Face(Color.BLUE);
    public Face left = new Face(Color.ORANGE);
    public Face right = new Face(Color.RED);
    public Face up = new Face(Color.YELLOW);
    public Face down = new Face(Color.WHITE);
    public Map<Direction, Face> faceMap = new HashMap<>();
    public final Map<Direction, Color> customColors = new HashMap<>();

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

        customColors.put(Direction.FRONT, Color.RED);
        customColors.put(Direction.BACK, Color.ORANGE);
        customColors.put(Direction.LEFT, Color.BLUE);
        customColors.put(Direction.RIGHT, Color.GREEN);
        customColors.put(Direction.UP, Color.WHITE);
        customColors.put(Direction.DOWN, Color.YELLOW);

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

    public void printFace(Face face) {
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

    public void restoreStickersFromFaces() {
        // UP 面：y = -1
        Color[][] tiles = faceMap.get(Direction.UP).getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = j - 1;
                int z = 1 - i;
                int y = -1;
                final Color color = tiles[i][j];
                findCubie(x, y, z).ifPresent(c -> c.setSticker(Direction.UP, color));
            }
        }

        // DOWN 面：y = 1
        tiles = faceMap.get(Direction.DOWN).getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = j - 1;
                int z = -1 + i;
                int y = 1;
                final Color color = tiles[i][j];
                findCubie(x, y, z).ifPresent(c -> c.setSticker(Direction.DOWN, color));
            }
        }

        // FRONT 面：z = 1
        tiles = faceMap.get(Direction.FRONT).getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = 1 - j;
                int y = -1 + i;
                int z = 1;
                final Color color = tiles[i][j];

                findCubie(x, y, z).ifPresent(c -> c.setSticker(Direction.FRONT, color));
            }
        }

        // BACK 面：z = -1
        tiles = faceMap.get(Direction.BACK).getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = -1 + j;
                int y = -1 + i;
                int z = -1;
                final Color color = tiles[i][j];

                findCubie(x, y, z).ifPresent(c -> c.setSticker(Direction.BACK, color));
            }
        }

        // LEFT 面：x = -1
        tiles = faceMap.get(Direction.LEFT).getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int y = -1 + i;
                int z = 1 - j;
                int x = -1;
                final Color color = tiles[i][j];

                findCubie(x, y, z).ifPresent(c -> c.setSticker(Direction.LEFT, color));
            }
        }

        // RIGHT 面：x = 1
        tiles = faceMap.get(Direction.RIGHT).getTiles();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int y = -1 + i;
                int z = -1 + j;
                int x = 1;
                final Color color = tiles[i][j];

                findCubie(x, y, z).ifPresent(c -> c.setSticker(Direction.RIGHT, color));
            }
        }
    }

    public void loadData(SLManager.LoadResult loadResult){
        this.faceMap.put(Direction.UP, new Face(loadResult.faceColors.get(Direction.UP)));
        this.faceMap.put(Direction.DOWN, new Face(loadResult.faceColors.get(Direction.DOWN)));
        this.faceMap.put(Direction.FRONT, new Face(loadResult.faceColors.get(Direction.FRONT)));
        this.faceMap.put(Direction.BACK, new Face(loadResult.faceColors.get(Direction.BACK)));
        this.faceMap.put(Direction.LEFT, new Face(loadResult.faceColors.get(Direction.LEFT)));
        this.faceMap.put(Direction.RIGHT, new Face(loadResult.faceColors.get(Direction.RIGHT)));

        // 2. 套用六面自訂顏色
        this.customColors.putAll(loadResult.customColors);

        // 3. 更新貼紙外觀（視覺同步）
        restoreStickersFromFaces();
    }
}