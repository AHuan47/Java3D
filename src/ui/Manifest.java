package ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Manifest {
    public List<Slot> slots;

    public static class Slot {
        public int slot;
        public String data;
        public String thumbnail;
    }

    public static Manifest getManifest(){
        Gson gson = new Gson();
        String raw = "";
        try{
            raw = Files.readString(Path.of("resources/assets/saves/manifest.json"));}
        catch (IOException e2){System.out.println("availableIds.json not found");}
        return gson.fromJson(raw, Manifest.class);
    }

    public static void updateManifest(Manifest manifest){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String updatedJson = gson.toJson(manifest);
        try{Files.writeString(Path.of("resources/assets/saves/manifest.json"), updatedJson);} catch (IOException e) {
            System.out.println("updateManifest error");
        }
    }

    public static void updateAvailableIds(int mode, int id){
        // 讀檔解析 JSON → Map
        try {
            String json = Files.readString(Path.of("resources/assets/saves/availableIds.json"));
            Type type = new TypeToken<Map<String, Object>>() {
            }.getType();
            Map<String, Object> map = new Gson().fromJson(json, type);

            List<Double> raw = (List<Double>) map.get("availableIds");
            List<Integer> available = raw.stream()
                    .map(Double::intValue)
                    .collect(Collectors.toList());

            System.out.println(available);

            if (mode == 0 && !available.contains(id)) { // 加入可用
                System.out.println(id + "add to av");
                available.add(id);
                Collections.sort(available);
            } else if (mode == 1 && available.contains(id)) {
                available.remove(Integer.valueOf(id));
            }
            map.put("availableIds", available);
            System.out.println("11: "+available);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String updatedJson = gson.toJson(map);
            Files.writeString(Path.of("resources/assets/saves/availableIds.json"), updatedJson);

        } catch (IOException e){
            System.out.println("availableIds.json not found");
        }

    }

    public String getSlotJson(int slot){
        return slots.get(slot).data;
    }

    public void passForward(){
        for(int i = 1; i < 6; i++){
            if(slots.get(i).data != null && slots.get(i-1).data == null){
                slots.get(i-1).data = slots.get(i).data;
                slots.get(i-1).thumbnail = slots.get(i).thumbnail;
                slots.get(i).data = null;
                slots.get(i).thumbnail = null;
            }
        }
    }

    public int returnSlot() {
        for (int i = 0; i < 6; i++) {
            if (slots.get(i).data == null) {
                return i;
            }
        }
        return 5;
    }

}