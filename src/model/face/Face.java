package model.face;
import javafx.scene.paint.Color;

public class Face {
    private final Color[][] tiles = new Color[3][3];

    public Face(Color defaultColor) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                tiles[i][j] = defaultColor;
    }

    public Color getTile(int row, int col) {
        return tiles[row][col];
    }

    public void setTile(int row, int col, Color color) {
        tiles[row][col] = color;
    }

    public void rotateClockwise() {
        Color[][] temp = new Color[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                temp[j][2 - i] = tiles[i][j];
        for (int i = 0; i < 3; i++)
            System.arraycopy(temp[i], 0, tiles[i], 0, 3);
    }

    public void rotateCounterClockwise() {  //模擬魔術方塊其中一面逆時針轉 90°
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
}
