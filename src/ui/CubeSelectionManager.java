package ui;

import model.Cubie;
import javafx.scene.effect.Glow;

public class CubeSelectionManager {
    private static Cubie selectedCubie = null;
    private static final Glow glowEffect = new Glow(0.8);

    public static void select(Cubie cubie) {
        if (selectedCubie != null) {
            selectedCubie.setSelected(false);
        }

        selectedCubie = cubie;
        selectedCubie.setSelected(true);
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
}

