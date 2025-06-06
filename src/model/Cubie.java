package model;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import logic.Axis;
import ui.CubeSelectionManager;

import model.face.Direction;

import java.util.HashMap;
import java.util.Map;

public class Cubie extends Group {
    private final Box body;
    private Box selectionBox = null;
    private int size;
    private Affine orientation = new Affine();
    public int x ,y ,z;

    public Map<Direction, Box> stickers = new HashMap<>();

    public Cubie(int x, int y, int z, int size) {
        body = new Box(size * 0.95, size * 0.95, size * 0.95);
        body.setMaterial(new PhongMaterial(Color.DARKGRAY));
        getChildren().add(body);

        body.setDepthTest(DepthTest.ENABLE); // 啟用深度測試，允許點擊事件穿透進來
        body.setPickOnBounds(true);          // 允許邊界作為點擊範圍
        body.setOnMousePressed(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                CubeSelectionManager.select(this);
                System.out.println("1 x " + this.getTranslateX() + "y " + this.getTranslateY() + "z " + this.getTranslateZ());
            }
        });

        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.getTransforms().add(orientation); // 初始方向

        setTranslateX(x * size);
        setTranslateY(y * size);
        setTranslateZ(z * size);
        final double half = size / 2.0;
        // COLOR
        // FRONT
        if (z == -1) {
            Box sticker = new Box(size * 0.85, size * 0.85, 0.1);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.GREEN));
            sticker.setTranslateZ(-half);
            getChildren().add(sticker);
            stickers.put(Direction.FRONT, sticker);

            if (y == -1 && x == -1){
                System.out.println("s");
                sticker = new Box(size * 0.40, size * 0.40, 0.2);
                sticker.setMouseTransparent(true);
                sticker.setMaterial(new PhongMaterial(Color.BEIGE));
                sticker.setTranslateZ(-half);
                getChildren().add(sticker);
            }
        }
        // BACK
        if (z == 1) {
            Box sticker = new Box(size * 0.85, size * 0.85, 0.1);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.BLUE));
            sticker.setTranslateZ(half);
            getChildren().add(sticker);
            stickers.put(Direction.BACK, sticker);

            if (y == -1 && x == 1){
                System.out.println("s");
                sticker = new Box(size * 0.40, size * 0.40, 0.2);
                sticker.setMouseTransparent(true);
                sticker.setMaterial(new PhongMaterial(Color.BEIGE));
                sticker.setTranslateZ(half);
                getChildren().add(sticker);
            }
        }
        // LEFT
        if (x == -1) {
            Box sticker = new Box(0.1, size * 0.85, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.ORANGE));
            sticker.setTranslateX(-half);
            this.getChildren().add(sticker);

            stickers.put(Direction.LEFT, sticker);

            if(y == -1 && z == 1){
                sticker = new Box(0.2, size * 0.4, size * 0.4);
                sticker.setMouseTransparent(true);
                sticker.setMaterial(new PhongMaterial(Color.BEIGE));
                sticker.setTranslateX(-half);
                this.getChildren().add(sticker);
            }
        }
        // RIGHT
        if (x == 1) {
            Box sticker = new Box(0.1, size * 0.85, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.RED));
            sticker.setTranslateX(half);
            getChildren().add(sticker);

            stickers.put(Direction.RIGHT, sticker);

            if(y == -1 && z == -1){
                sticker = new Box(0.2, size * 0.4, size * 0.4);
                sticker.setMouseTransparent(true);
                sticker.setMaterial(new PhongMaterial(Color.BEIGE));
                sticker.setTranslateX(half);
                this.getChildren().add(sticker);
            }
        }
        // UP
        if (y == -1) {
            Box sticker = new Box(size * 0.85, 0.1, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.YELLOW));
            sticker.setTranslateY(-half);
            getChildren().add(sticker);

            stickers.put(Direction.UP, sticker);

            if(x == -1 && z == 1){
                sticker = new Box(size * 0.4, 0.2, size * 0.4);
                sticker.setMouseTransparent(true);
                sticker.setMaterial(new PhongMaterial(Color.BEIGE));
                sticker.setTranslateY(-half);
                getChildren().add(sticker);
            }
        }
        // DOWN
        if (y == 1) {
            Box sticker = new Box(size * 0.85, 0.1, size * 0.85);
            sticker.setMouseTransparent(true);
            sticker.setMaterial(new PhongMaterial(Color.WHITE));
            sticker.setTranslateY(half);
            getChildren().add(sticker);

            stickers.put(Direction.DOWN, sticker);

            if(x == -1 && z == -1){
                sticker = new Box(size * 0.4, 0.2, size * 0.4);
                sticker.setMouseTransparent(true);
                sticker.setMaterial(new PhongMaterial(Color.BEIGE));
                sticker.setTranslateY(half);
                getChildren().add(sticker);
            }
        }
    }

    public void setSelected(boolean selected) {
        if (selected) {
            if (selectionBox == null) {
                selectionBox = new Box(body.getWidth() + 3, body.getHeight() + 3, body.getDepth() + 3);
                PhongMaterial outline = new PhongMaterial(Color.rgb(207, 187, 187, 0.1)); // RGBA
                selectionBox.setMaterial(outline);
                selectionBox.setMouseTransparent(true);  // 不遮擋滑鼠事件
                getChildren().add(selectionBox);
            }
        } else {
            getChildren().remove(selectionBox);
            selectionBox = null;
        }
    }

    public void rotateAxis(Axis axis, double angle) {
        Rotate rotation = switch (axis) {
            case X -> new Rotate(normalizeAngle(angle), Rotate.X_AXIS);
            case Y -> new Rotate(normalizeAngle(angle), Rotate.Y_AXIS);
            case Z -> new Rotate(normalizeAngle(angle), Rotate.Z_AXIS);
        };
        orientation.prepend(rotation); // 將旋轉「累加到現有方向上」

    }

    private double normalizeAngle(double angle) {
        return ((angle % 360) + 360) % 360;
    }

    private static double snap(double value, double epsilon) {
        if (Math.abs(value - 1.0) < epsilon) return 1.0;
        if (Math.abs(value + 1.0) < epsilon) return -1.0;
        if (Math.abs(value) < epsilon) return 0.0;
        return value;
    }

    public void snapOrientation(double epsilon) {
        double[] m = new double[12];
        m[ 0] = orientation.getMxx(); // X row
        m[ 1] = orientation.getMxy();
        m[ 2] = orientation.getMxz();
        m[ 3] = orientation.getTx();

        m[ 4] = orientation.getMyx(); // Y row
        m[ 5] = orientation.getMyy();
        m[ 6] = orientation.getMyz();
        m[ 7] = orientation.getTy();

        m[ 8] = orientation.getMzx(); // Z row
        m[ 9] = orientation.getMzy();
        m[10] = orientation.getMzz();
        m[11] = orientation.getTz();

        for (int i = 0; i < m.length; i++) {
            m[i] = snap(m[i], epsilon);
        }

        orientation.setToTransform(
                m[0],  m[1],  m[2],  m[3],
                m[4],  m[5],  m[6],  m[7],
                m[8],  m[9],  m[10], m[11]
        );
    }

    public double snapToGrid(double value, int gridSize) {
        long gridPos = Math.round(value / gridSize);
        return (double) gridPos * gridSize;
    }


    public void setSticker(Direction direction, Color color){
        Box sticker = this.stickers.get(direction);
        if (sticker==null) return;
        sticker.setMaterial(new PhongMaterial(color));
    }
}