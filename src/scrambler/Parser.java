package scrambler;

import logic.Move;
import java.util.*;

public class Parser {
    private static final String[] VALID_FACES = {"F", "B", "L", "R", "U", "D"};
    private static final Set<String> VALID_FACE_SET = Set.of(VALID_FACES);

    public static List<Move> parseTxt(String scrambleTxt) {
        if (scrambleTxt == null || scrambleTxt.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String[] moveStrings = scrambleTxt.trim().split("\\s+");
        List<Move> moves = new ArrayList<>(moveStrings.length);

        for(String moveString : moveStrings) {
            Move move = parseMove(moveString);
            moves.add(move);
        }

        return moves;
    }

    public static Move parseMove(String moveString) {
        if(moveString == null || moveString.trim().isEmpty()) {
            throw new IllegalArgumentException("Move string cannot be null or empty");
        }

        moveString = moveString.trim();
        String face = moveString.substring(0, 1);
        String suffix = moveString.length() > 1 ? moveString.substring(1) : "";

        // shouldn't happen but here anyway
        if(!VALID_FACE_SET.contains(face)) {
            throw new IllegalArgumentException("Invalid face '" + face + "'");
        }

        String enumName = switch (suffix) {
            case "" -> face;
            case "'" -> face + "_PRIME";
            case "2" -> face + "_2";
            default -> throw new IllegalArgumentException("Invalid move");
        };

        try {
            return Move.valueOf(enumName);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not parse move: " + moveString + " (tried enum: " + enumName + ")", e);
        }
    }

    public static String movesToString(List<Move> moves) {
        if(moves == null || moves.isEmpty()) {
            return "";
        }

        List<String> moveStrings = new ArrayList<>();
        for(Move move : moves) {
           moveStrings.add(move.toString());
        }

        return String.join(" ", moveStrings);
    }


}
