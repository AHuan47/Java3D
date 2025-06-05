package logic;

import javafx.animation.*;
import javafx.util.*;
import model.Cube;
import model.Cubie;
import model.face.Direction;
import java.util.*;
import static model.face.FaceUtils.getAxis;
import static model.face.FaceUtils.getCord;


public class SequentialRotationAnimator {

    private static List<Move> currentMoves = null;
    private static Cube currentCube = null;
    private static int currentMoveIndex = 0;
    private static Timeline animationTimeline = null;
    private static boolean isSecondHalfOfDouble = false;
    private static boolean isComplete = false;

    public static void sequentialAnimator(List<Move> moves, Cube cube) {
        currentMoves = moves;
        currentCube = cube;
        currentMoveIndex = 0;
        isSecondHalfOfDouble = false;
        isComplete = false;

        if (animationTimeline != null) {
            animationTimeline.stop();
        }

        animationTimeline = new Timeline();
        animationTimeline.setCycleCount(moves.size() * 2); // just incase every thing is a double move
        animationTimeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(250), event -> applyNextMove())
        );
        animationTimeline.play();
    }

    private static void applyNextMove() {
        if (currentMoveIndex < currentMoves.size()) {
            Move currentMove = currentMoves.get(currentMoveIndex);
            Direction face = currentMove.getDirection();
            boolean needInversion = (face == Direction.BACK || face == Direction.RIGHT || face == Direction.DOWN);
            Axis axis = getAxis(face);
            int cord = getCord(face);
            List<Cubie> layer = LayerSelector.getCubiesInLayer(currentCube.allCubies, axis, cord);
            
            if (isSecondHalfOfDouble) {
                boolean clockwise = currentMove.isClockwise();
                boolean visualClockwise = needInversion ? !clockwise : clockwise;
                currentCube.applyRotation(face, visualClockwise);
                RotationAnimator.rotateLayer(layer, axis, visualClockwise ? 90 : -90, currentCube);
                currentMoveIndex++;
                isSecondHalfOfDouble = false;
            } else {
                if (currentMove.isDoubleMove()) {
                    boolean clockwise = currentMove.isClockwise();
                    boolean visualClockwise = needInversion ? !clockwise : clockwise;
                    currentCube.applyRotation(face, visualClockwise);
                    RotationAnimator.rotateLayer(layer, axis, visualClockwise ? 90 : -90, currentCube);
                    isSecondHalfOfDouble = true;
                } else {
                    boolean clockwise = currentMove.isClockwise();
                    boolean visualClockwise = needInversion ? !clockwise : clockwise;
                    currentCube.applyRotation(face, visualClockwise);
                    RotationAnimator.rotateLayer(layer, axis, visualClockwise ? 90 : -90, currentCube);
                    currentMoveIndex++;
                }
            }
        }
        else {
            if (!isComplete) {
                animationTimeline.stop();
                System.out.println("Moves applied successfully.");
                isComplete = true;
            }
        }
    }
}
