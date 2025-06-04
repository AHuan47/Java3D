package logic;
import model.Cubie;
import java.util.List;
import java.util.stream.Collectors;

public class LayerSelector {
    public static List<Cubie> getCubiesInSameLayer(List<Cubie> allCubies, Cubie selected, Axis axis) {
        int layerCoord = switch (axis) {
            case X -> (int) selected.getTranslateX();
            case Y -> (int) selected.getTranslateY();
            case Z -> (int) selected.getTranslateZ();
        };

        // 過濾出在同一層的 cubie
        return allCubies.stream()
                .filter(c -> {
                    int coord = switch (axis) {
                        case X -> (int) c.getTranslateX();
                        case Y -> (int) c.getTranslateY();
                        case Z -> (int) c.getTranslateZ();
                    };
                    return coord == layerCoord;
                })
                .collect(Collectors.toList());
    }

    // gets the cubies from direction
    public static List<Cubie> getCubiesInLayer(List<Cubie> allCubies, Axis axis, int coordinate) {
        return allCubies.stream()
                .filter(c -> {
                    int coord = switch (axis) {
                        case X -> (int) c.getTranslateX();
                        case Y -> (int) c.getTranslateY();
                        case Z -> (int) c.getTranslateZ();
                    };
                    return coord == coordinate;
                })
                .collect(Collectors.toList());
    }
}