package model.sl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.scene.paint.Color;
import model.Cube;
import model.face.Direction;
import model.face.Face;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import com.google.gson.reflect.TypeToken;
import ui.Manifest;

public class SLManager {

    private static final String SAVE_FOLDER = "resources/assets/saves/Data/";
    private final String IMAGE_DIR = "resources/assets/saves/Thumbnail/";

    public static class LoadResult {  //回傳的資料結構
        public final Map<Direction, Color[][]> faceColors;
        public final Map<Direction, Color> customColors;

        public LoadResult(Map<Direction, Color[][]> faceColors, Map<Direction, Color> customColors) {
            this.faceColors = faceColors;
            this.customColors = customColors;
        }
    }

    private static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    private static Color fromHex(String hex) {
        return Color.web(hex);
    }

    public static void save(Cube cube, int slot) throws IOException {  // slot: 儲存槽位
        Map<String, List<String>> faceData = new HashMap<>();
        for (Direction dir : Direction.values()) {  // 讀取各面資訊
            Face face = cube.faceMap.get(dir);
            if (face == null) continue;

            Color[][] tiles = face.getTiles();
            List<String> flatColors = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    flatColors.add(toHex(tiles[i][j]));
                }
            }
            faceData.put(dir.name(), flatColors);
        }

        Map<String, String> colorMap = new HashMap<>();  // 邏輯與實際顏色地圖
        for (Map.Entry<Direction, Color> entry : cube.customColors.entrySet()) {
            colorMap.put(entry.getKey().name(), toHex(entry.getValue()));
        }

        Map<String, Object> saveObj = new HashMap<>();
        saveObj.put("faces", faceData);
        saveObj.put("customColors", colorMap);

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // 實際存檔
        String json = gson.toJson(saveObj);
        Path path = getSlotPath(slot);
        Files.createDirectories(path.getParent());
        Files.writeString(path, json);

        // manifest
        Manifest m = Manifest.getManifest();
        m.slots.get(slot).data = "save_slot_" + slot + ".json";
        m.slots.get(slot).thumbnail = "save_slot_" + slot + ".png";
    }

    public static void save(Cube cube, String fileName) throws IOException {  // slot: 儲存id
        Map<String, List<String>> faceData = new HashMap<>();
        for (Direction dir : Direction.values()) {  // 讀取各面資訊
            Face face = cube.faceMap.get(dir);
            if (face == null) continue;

            Color[][] tiles = face.getTiles();
            List<String> flatColors = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    flatColors.add(toHex(tiles[i][j]));
                }
            }
            faceData.put(dir.name(), flatColors);
        }

        Map<String, String> colorMap = new HashMap<>();  // 邏輯與實際顏色地圖
        for (Map.Entry<Direction, Color> entry : cube.customColors.entrySet()) {
            colorMap.put(entry.getKey().name(), toHex(entry.getValue()));
        }

        Map<String, Object> saveObj = new HashMap<>();
        saveObj.put("faces", faceData);
        saveObj.put("customColors", colorMap);

        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // 實際存檔
        String json = gson.toJson(saveObj);
        Path path = Path.of(SAVE_FOLDER + fileName);
        Files.createDirectories(path.getParent());
        Files.writeString(path, json);
    }

    public static LoadResult load(String fileName) throws IOException {

        String json = Files.readString(Path.of(SAVE_FOLDER + fileName));

        Gson gson = new Gson();
        Type outerType = new TypeToken<Map<String, Object>>() {}.getType();
        Map<String, Object> parsed = gson.fromJson(json, outerType);

        Map<Direction, Color[][]> faceMap = new EnumMap<>(Direction.class);
        Map<String, List<String>> facesRaw = gson.fromJson(
                gson.toJson(parsed.get("faces")),
                new TypeToken<Map<String, List<String>>>() {}.getType()
        );

        for (Map.Entry<String, List<String>> entry : facesRaw.entrySet()) {
            Direction dir = Direction.valueOf(entry.getKey());
            List<String> list = entry.getValue();
            Color[][] grid = new Color[3][3];
            for (int i = 0; i < 9; i++) {
                grid[i / 3][i % 3] = fromHex(list.get(i));
            }
            faceMap.put(dir, grid);
        }

        Map<Direction, Color> customColors = new EnumMap<>(Direction.class);
        Map<String, String> colorsRaw = gson.fromJson(
                gson.toJson(parsed.get("customColors")),
                new TypeToken<Map<String, String>>() {}.getType()
        );

        for (Map.Entry<String, String> entry : colorsRaw.entrySet()) {
            customColors.put(Direction.valueOf(entry.getKey()), fromHex(entry.getValue()));
        }

        return new LoadResult(faceMap, customColors);
    }

    private static Path getSlotPath(int slot) {
        return Paths.get(SAVE_FOLDER, "save_slot_" + slot + ".json");
    }
}