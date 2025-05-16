package ui;

import model.Cubie;
import javafx.scene.effect.Glow;
import logic.Axis;

import java.util.ArrayList;
import java.util.List;

public class CubeSelectionManager {
    private static Cubie selectedCubie = null;
    private static final Glow glowEffect = new Glow(0.8);
    private static Axis currentAxis = Axis.X;
    private static List<Cubie> selectedLayer = new ArrayList<>();

    public static void select(Cubie cubie) {
        if (selectedCubie == cubie) {
            cycleAxis(); // 如果點到同一個，再切換軸向
            updateSelectedLayer(); // 切換軸後，也要更新選取層
            return;
        }

        // 清除之前選取狀態
        if (selectedCubie != null) {
            selectedCubie.setSelected(false);
        }
        clearLayer(); // 也清除之前的選取層框

        // 新的選取
        selectedCubie = cubie;
        currentAxis = Axis.X;
        selectedCubie.setSelected(true);

        updateSelectedLayer(); // ✅ 額外新增的方法：選新 Cubie 後立即顯示該層
    }

    public static void clear() {
        if (selectedCubie != null) {
            selectedCubie.setEffect(null);
            selectedCubie = null;
        }
    }

    public static Cubie getSelected() {
        return selectedCubie;
    }

    public static Axis getCurrentAxis() {
        return currentAxis;
    }

    public static void cycleAxis() {
        currentAxis = switch (currentAxis) {
            case X -> Axis.Y;
            case Y -> Axis.Z;
            case Z -> Axis.X;
        };
        System.out.println("目前軸向：" + currentAxis);
    }

    public static void setSelectedLayer(List<Cubie> cubies) {
        // 清除前一次選取狀態
        for (Cubie c : selectedLayer) {
            c.setSelected(false);
        }

        selectedLayer = cubies;

        // 設定新選取狀態
        for (Cubie c : selectedLayer) {
            c.setSelected(true);
        }
    }

    private static void updateSelectedLayer() {
        if (selectedCubie == null) return;

        Axis axis = currentAxis;
        int value = switch (axis) {
            case X -> (int) selectedCubie.getTranslateX();
            case Y -> (int) selectedCubie.getTranslateY();
            case Z -> (int) selectedCubie.getTranslateZ();
        };

        List<Cubie> layer = selectedCubie.getParent().getChildrenUnmodifiable().stream()
                .filter(n -> n instanceof Cubie)
                .map(n -> (Cubie) n)
                .filter(c -> {
                    int coord = switch (axis) {
                        case X -> (int) c.getTranslateX();
                        case Y -> (int) c.getTranslateY();
                        case Z -> (int) c.getTranslateZ();
                    };
                    return coord == value;
                })
                .toList();

        setSelectedLayer(layer);
    }

    public static void clearLayer() {
        for (Cubie c : selectedLayer) {
            c.setSelected(false);
        }
        selectedLayer = new ArrayList<Cubie>();
    }


}
