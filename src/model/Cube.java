package model;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import model.face.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<Cubie> getAllCubies() {
        return allCubies;
    }

    public void applyRotation(Direction face, boolean clockwise) {
        // 1. 查表取得 RotationMapping
        RotationMapping mapping = RotationTable.table.get(face.name() + "_" + clockwise);

        // 2. 旋轉中心面
        getFace(mapping.center, faceMap).rotate(clockwise);

        // 3. 讀取四條邊，根據 reversed 進行處理
        List<Color[]> edges = new ArrayList<>();
        for (EdgeMapping e : mapping.edges) {
            Color[] colors = getEdge(getFace(e.face, faceMap), e.index, e.isRow);
            if (e.reversed) reverse(colors);
            edges.add(colors);
        }

        // 4. 寫回四條邊（依照方向搬移一格）
        for (int i = 0; i < 4; i++) {
            EdgeMapping target = mapping.edges[(i + 1) % 4];
            Color[] src = edges.get(i);
            if (target.reversed) reverse(src);
            setEdge(getFace(target.face, faceMap), target.index, target.isRow, src);
        }
    }
}