package model.face;

import java.util.HashMap;
import java.util.Map;

public class RotationTable {
    public static final Map<String, RotationMapping> table = new HashMap<>();

    static {
        // FRONT
        table.put("FRONT_TRUE", new RotationMapping(Direction.FRONT, true, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 0, false, false),
                new EdgeMapping(Direction.DOWN, 0, true, true),
                new EdgeMapping(Direction.LEFT, 2, false, true),
        }));

        table.put("FRONT_FALSE", new RotationMapping(Direction.FRONT, false, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 2, true, true),
                new EdgeMapping(Direction.LEFT, 2, false, false),
                new EdgeMapping(Direction.DOWN, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 0, false, true),
        }));

        // BACK
        table.put("BACK_TRUE", new RotationMapping(Direction.BACK, true, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 0, true, true),
                new EdgeMapping(Direction.LEFT, 0, false, false),
                new EdgeMapping(Direction.DOWN, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 2, false, true),
        }));

        table.put("BACK_FALSE", new RotationMapping(Direction.BACK, false, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 2, false, false),
                new EdgeMapping(Direction.DOWN, 2, true, true),
                new EdgeMapping(Direction.LEFT, 0, false, true),
        }));

        // LEFT
        table.put("LEFT_TRUE", new RotationMapping(Direction.LEFT, true, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 0, false, true),
                new EdgeMapping(Direction.FRONT, 0, false, false),
                new EdgeMapping(Direction.DOWN, 0, false, false),
                new EdgeMapping(Direction.BACK, 2, false, true),
        }));

        table.put("LEFT_FALSE", new RotationMapping(Direction.LEFT, false, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 0, false, false),
                new EdgeMapping(Direction.BACK, 2, false, false),
                new EdgeMapping(Direction.DOWN, 0, false, true),
                new EdgeMapping(Direction.FRONT, 0, false, true),
        }));

        // RIGHT
        table.put("RIGHT_TRUE", new RotationMapping(Direction.RIGHT, true, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 2, false, false),
                new EdgeMapping(Direction.BACK, 0, false, true),
                new EdgeMapping(Direction.DOWN, 2, false, true),
                new EdgeMapping(Direction.FRONT, 2, false, false),
        }));

        table.put("RIGHT_FALSE", new RotationMapping(Direction.RIGHT, false, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 2, false, true),
                new EdgeMapping(Direction.FRONT, 2, false, true),
                new EdgeMapping(Direction.DOWN, 2, false, false),
                new EdgeMapping(Direction.BACK, 0, false, false),
        }));

        // UP
        table.put("UP_TRUE", new RotationMapping(Direction.UP, true, new EdgeMapping[] {
                new EdgeMapping(Direction.BACK, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 0, true, false),
                new EdgeMapping(Direction.FRONT, 0, true, false),
                new EdgeMapping(Direction.LEFT, 0, true, false),
        }));

        table.put("UP_FALSE", new RotationMapping(Direction.UP, false, new EdgeMapping[] {
                new EdgeMapping(Direction.BACK, 0, true, false),
                new EdgeMapping(Direction.LEFT, 0, true, false),
                new EdgeMapping(Direction.FRONT, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 0, true, false),
        }));

        // DOWN
        table.put("DOWN_TRUE", new RotationMapping(Direction.DOWN, true, new EdgeMapping[] {
                new EdgeMapping(Direction.FRONT, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 2, true, false),
                new EdgeMapping(Direction.BACK, 2, true, false),
                new EdgeMapping(Direction.LEFT, 2, true, false),
        }));

        table.put("DOWN_FALSE", new RotationMapping(Direction.DOWN, false, new EdgeMapping[] {
                new EdgeMapping(Direction.FRONT, 2, true, false),
                new EdgeMapping(Direction.LEFT, 2, true, false),
                new EdgeMapping(Direction.BACK, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 2, true, false),
        }));

        // 中層 MIDDLEX（Y=0），在 FRONT 和 BACK 中間 → Y軸旋轉
        table.put("MIDDLEX_TRUE", new RotationMapping(Direction.MIDDLEX, true, new EdgeMapping[] {
                new EdgeMapping(Direction.BACK, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, true, false),
                new EdgeMapping(Direction.FRONT, 1, true, false),
                new EdgeMapping(Direction.LEFT, 1, true, false),
        }));

        table.put("MIDDLEX_FALSE", new RotationMapping(Direction.MIDDLEX, false, new EdgeMapping[] {
                new EdgeMapping(Direction.BACK, 1, true, false),
                new EdgeMapping(Direction.LEFT, 1, true, false),
                new EdgeMapping(Direction.FRONT, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, true, false),
        }));

        // 中層 MIDDLEY（X=0），在 UP 和 DOWN 中間 → X軸旋轉
        table.put("MIDDLEY_TRUE", new RotationMapping(Direction.MIDDLEY, true, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 1, false, true),
                new EdgeMapping(Direction.FRONT, 1, false, false),
                new EdgeMapping(Direction.DOWN, 1, false, false),
                new EdgeMapping(Direction.BACK, 1, false, true),
        }));

        table.put("MIDDLEY_FALSE", new RotationMapping(Direction.MIDDLEY, false, new EdgeMapping[] {
                new EdgeMapping(Direction.UP, 1, false, false),
                new EdgeMapping(Direction.BACK, 1, false, false),
                new EdgeMapping(Direction.DOWN, 1, false, true),
                new EdgeMapping(Direction.FRONT, 1, false, true),
        }));

        // 中層 MIDDLEZ（Z=0），在 LEFT 和 RIGHT 中間 → Z軸旋轉
        table.put("MIDDLEZ_TRUE", new RotationMapping(Direction.MIDDLEZ, true, new EdgeMapping[] {
                new EdgeMapping(Direction.FRONT, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, false, false),
                new EdgeMapping(Direction.BACK, 1, true, true),
                new EdgeMapping(Direction.LEFT, 1, false, true),
        }));

        table.put("MIDDLEZ_FALSE", new RotationMapping(Direction.MIDDLEZ, false, new EdgeMapping[] {
                new EdgeMapping(Direction.FRONT, 1, true, true),
                new EdgeMapping(Direction.LEFT, 1, false, false),
                new EdgeMapping(Direction.BACK, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, false, true),
        }));
    }
}
