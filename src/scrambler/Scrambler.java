package scrambler;

import logic.Move;

import java.util.*;

public class Scrambler {

    private final Random random;

    private static final String[] ALL_FACES = {"F", "B", "L", "R", "U", "D"};

    private static final String[] MOVE_TYPES = {"", "'", "2"};



    public Scrambler() {
        this.random = new Random();
    }

    public Scrambler(long seed) {
        this.random = new Random(seed);
    }

    public String genScrambleStr(int length) {
        if(length <= 0) throw new IllegalArgumentException("Scramble length cannot be smaller than 1");

        List<String> moves = new ArrayList<>(length);
        String lastFace = null;

        for(int i = 0; i < length; i++) {
            String nextMove = genRandMoveStr(lastFace);
            moves.add(nextMove);
            lastFace = extractFace(nextMove);
        }

        return String.join(" ", moves);
    }

    // default to this
    public List<Move> genStdScramble() {
        return genScrambleMoves(30);
    }

    public List<Move> genScrambleMoves(int length) {
        String scrambleTxt = genScrambleStr(length);
        return Parser.parseTxt(scrambleTxt);
    }

    private String selectRandomFace(String excludedFace) {
        List<String> availableFaces = new ArrayList<>();
        for (String face : ALL_FACES) {
            if (!face.equals(excludedFace)) {
                availableFaces.add(face);
            }
        }
        return availableFaces.get(random.nextInt(availableFaces.size()));
    }

    private String genRandMoveStr(String excludedFace) {
        String selectedFace = selectRandomFace(excludedFace);

        String moveType = MOVE_TYPES[random.nextInt(MOVE_TYPES.length)];

        return selectedFace + moveType;
    }

    private String extractFace(String moveString) {
        return moveString.substring(0, 1);
    }

}
