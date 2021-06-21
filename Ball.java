public class Ball {

    private int row;
    private int diag;
    private Color color;
    private String colorRepr;


    public Ball(Color color, int row, int diag) {
        this.color = color;
        this.row = row;
        this.diag = diag;
        if (color == Color.BLACK) {
            colorRepr = "X";
        } else if (color == Color.WHITE) {
            colorRepr = "O";
        } else {
             colorRepr = ".";
        }
    }


    public Color getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getDiag() {
        return diag;
    }

    public String getColorRepr() {
        return colorRepr;
    }

    public void setPos(int row, int diag) {
        this.row = row;
        this.diag = diag;
    }

}
