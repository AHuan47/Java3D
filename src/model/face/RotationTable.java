package model.face;

import java.util.HashMap;
import java.util.Map;

public class RotationTable {
    public static final Map<String, RotationMapping> table = new HashMap<>();

    static {
        table.put("FRONT_true", new RotationMapping(Direction.FRONT, true, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 0, false, false),
                new EdgeMapping(Direction.DOWN, 0, true, true),
                new EdgeMapping(Direction.LEFT, 2, false, true),
        }));
        table.put("FRONT_false", new RotationMapping(Direction.FRONT, false, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 2, true, true),
                new EdgeMapping(Direction.LEFT, 2, false, false),
                new EdgeMapping(Direction.DOWN, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 0, false, true),
        }));

        table.put("BACK_true", new RotationMapping(Direction.BACK, false, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 2, false, false),
                new EdgeMapping(Direction.DOWN, 2, true, true),
                new EdgeMapping(Direction.LEFT, 0, false, true),
        }));
        table.put("BACK_false", new RotationMapping(Direction.BACK, true, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 0, true, true),
                new EdgeMapping(Direction.LEFT, 0, false, false),
                new EdgeMapping(Direction.DOWN, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 2, false, true),
        }));

        table.put("LEFT_true", new RotationMapping(Direction.LEFT, true, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 0, false, false),
                new EdgeMapping(Direction.FRONT, 0, false, false),
                new EdgeMapping(Direction.DOWN, 0, false, false),
                new EdgeMapping(Direction.BACK, 2, false, true),
        }));
        table.put("LEFT_false", new RotationMapping(Direction.LEFT, false, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 0, false, false),
                new EdgeMapping(Direction.BACK, 2, false, true),
                new EdgeMapping(Direction.DOWN, 0, false, false),
                new EdgeMapping(Direction.FRONT, 0, false, false),
        }));

        table.put("RIGHT_true", new RotationMapping(Direction.RIGHT, false, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 2, false, false),
                new EdgeMapping(Direction.FRONT, 2, false, false),
                new EdgeMapping(Direction.DOWN, 2, false, false),
                new EdgeMapping(Direction.BACK, 0, false, true),

        }));
        table.put("RIGHT_false", new RotationMapping(Direction.RIGHT, true, new EdgeMapping[]{ //
                new EdgeMapping(Direction.UP, 2, false, false),
                new EdgeMapping(Direction.BACK, 0, false, true),
                new EdgeMapping(Direction.DOWN, 2, false, false),
                new EdgeMapping(Direction.FRONT, 2, false, false),
        }));

        table.put("UP_false", new RotationMapping(Direction.UP, false, new EdgeMapping[]{ //
                new EdgeMapping(Direction.FRONT, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 0, true, false),
                new EdgeMapping(Direction.BACK, 0, true, false),
                new EdgeMapping(Direction.LEFT, 0, true, false),
        }));
        table.put("UP_true", new RotationMapping(Direction.UP, true, new EdgeMapping[]{ //
                new EdgeMapping(Direction.FRONT, 0, true, false),
                new EdgeMapping(Direction.LEFT, 0, true, false),
                new EdgeMapping(Direction.BACK, 0, true, false),
                new EdgeMapping(Direction.RIGHT, 0, true, false),
        }));

        table.put("DOWN_true", new RotationMapping(Direction.DOWN, false, new EdgeMapping[]{ //
                new EdgeMapping(Direction.FRONT, 2, true, false),
                new EdgeMapping(Direction.LEFT, 2, true, false),
                new EdgeMapping(Direction.BACK, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 2, true, false),
        }));
        table.put("DOWN_false", new RotationMapping(Direction.DOWN, true, new EdgeMapping[]{ //
                new EdgeMapping(Direction.FRONT, 2, true, false),
                new EdgeMapping(Direction.RIGHT, 2, true, false),
                new EdgeMapping(Direction.BACK, 2, true, false),
                new EdgeMapping(Direction.LEFT, 2, true, false),
        }));

// MIDDLEX = Y軸中層 → X軸旋轉
        table.put("MIDDLEX_false", new RotationMapping(Direction.MIDDLEX, true, new EdgeMapping[]{
                new EdgeMapping(Direction.FRONT, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, true, false),
                new EdgeMapping(Direction.BACK, 1, true, false),
                new EdgeMapping(Direction.LEFT, 1, true, false),
        }));
        table.put("MIDDLEX_true", new RotationMapping(Direction.MIDDLEX, false, new EdgeMapping[]{
                new EdgeMapping(Direction.FRONT, 1, true, false),
                new EdgeMapping(Direction.LEFT, 1, true, false),
                new EdgeMapping(Direction.BACK, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, true, false),
        }));

// MIDDLEY = X軸中層 → Y軸旋轉
        table.put("MIDDLEY_false", new RotationMapping(Direction.MIDDLEY, true, new EdgeMapping[]{
                new EdgeMapping(Direction.UP, 1, false, true),
                new EdgeMapping(Direction.BACK, 1, false, false),
                new EdgeMapping(Direction.DOWN, 1, false, true),
                new EdgeMapping(Direction.FRONT, 1, false, true),
        }));
        table.put("MIDDLEY_true", new RotationMapping(Direction.MIDDLEY, false, new EdgeMapping[]{
                new EdgeMapping(Direction.UP, 1, false, false),
                new EdgeMapping(Direction.FRONT, 1, false, false),
                new EdgeMapping(Direction.DOWN, 1, false, false),
                new EdgeMapping(Direction.BACK, 1, false, true),
        }));

// MIDDLEZ = Z軸中層 → Z軸旋轉
        table.put("MIDDLEZ_true", new RotationMapping(Direction.MIDDLEZ, true, new EdgeMapping[]{
                new EdgeMapping(Direction.UP, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, false, false),
                new EdgeMapping(Direction.DOWN, 1, true, true),
                new EdgeMapping(Direction.LEFT, 1, false, true),
        }));
        table.put("MIDDLEZ_false", new RotationMapping(Direction.MIDDLEZ, false, new EdgeMapping[]{
                new EdgeMapping(Direction.UP, 1, true, true),
                new EdgeMapping(Direction.LEFT, 1, false, false),
                new EdgeMapping(Direction.DOWN, 1, true, false),
                new EdgeMapping(Direction.RIGHT, 1, false, true),
        }));
    }
}