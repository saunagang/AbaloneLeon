public enum Player {

    HUMAN(Color.BLACK), MACHINE(Color.WHITE), NONE(null);

    private Color color;

    Player(Color color){
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
