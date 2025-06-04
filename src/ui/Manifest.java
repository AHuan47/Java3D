package ui;

import java.util.List;

public class Manifest {
    public List<Slot> slots;

    public static class Slot {
        public int slot;
        public String data;
        public String thumbnail;
    }
}