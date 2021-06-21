import java.util.ArrayList;

public class playingField implements Board, Cloneable{

    private final int size;
    private Ball[][] ballsField;
    private Player openingPlayer;
    private Player lastPlayer;
    private int level = 1;
    private ArrayList<Ball> whiteBalls = new ArrayList<Ball>();
    private ArrayList<Ball> blackBalls = new ArrayList<Ball>();
    private int maxNumberOfBalls;

    public playingField(int input, Player player) {
        size = input;
        openingPlayer = player;
        maxNumberOfBalls = (int) (((size - 9) * 1.5) + 14);
        createLists();
        createBallsField();

        if (player == Player.HUMAN) {
            lastPlayer = Player.MACHINE;
            Player.HUMAN.setColor(Color.BLACK);
            Player.MACHINE.setColor(Color.WHITE);
        } else if (player == Player.MACHINE) {
            lastPlayer = Player.HUMAN;
            Player.HUMAN.setColor(Color.WHITE);
            Player.MACHINE.setColor(Color.BLACK);
        }
    }

    private void createLists() {
        for (int i = 0; i < maxNumberOfBalls; i++) {
            whiteBalls.add(new Ball(Color.WHITE,0,0));
            blackBalls.add(new Ball(Color.BLACK,0,0));
        }
    }

    public playingField clonen(){

       playingField clone;

       try {
           clone = (playingField) this.clone();
       } catch (CloneNotSupportedException e) {
           return null;
       }
       return clone;
    }

    private void createBallsField() {

        ballsField = new Ball[size][size];

        int min;
        int max;

        int black;
        int white;

        if (openingPlayer == Player.HUMAN) {
            black = 0;
            white = maxNumberOfBalls - 1;
        } else {
            black = maxNumberOfBalls - 1;
            white = 0;
        }

        for (int i = 0; i < size; i++) {

            if (0 > (i - (size / 2))) {
                min = 0;
            } else {
                min = i - (size / 2);
            }
            if (i + (size / 2) < size - 1) {
                max = i + (size / 2);
            } else {
                max = size - 1;
            }

            for (int j = 0; j < size; j++) {
                if (j >= min && j <= max) {
                    if (i <= 1) {
                        if (openingPlayer == Player.HUMAN) {
                            blackBalls.get(black).setPos(i, j);
                            ballsField[i][j] = blackBalls.get(black);
                            black = black + 1;
                        } else {
                            whiteBalls.get(white).setPos(i, j);
                            ballsField[i][j] = whiteBalls.get(white);
                            white = white + 1;
                        }
                    } else if (i >= size - 2) {
                        if (openingPlayer == Player.HUMAN) {
                            whiteBalls.get(white).setPos(i, j);
                            ballsField[i][j] = whiteBalls.get(white);
                            white = white - 1;
                        } else {
                            blackBalls.get(black).setPos(i, j);
                            ballsField[i][j] = blackBalls.get(black);
                            black = black - 1;
                        }
                    } else if (i == size - 3 && j >= (min + 2) && j <= (max - 2)) {
                        if (openingPlayer == Player.HUMAN) {
                            whiteBalls.get(white).setPos(i, j);
                            ballsField[i][j] = whiteBalls.get(white);
                            white = white - 1;
                        } else {
                            blackBalls.get(black).setPos(i, j);
                            ballsField[i][j] = blackBalls.get(black);
                            black = black - 1;
                        }
                    } else if (i == 2 && j >= (min + 2) && j <= (max - 2)) {
                        if (openingPlayer == Player.HUMAN) {
                            blackBalls.get(black).setPos(i, j);
                            ballsField[i][j] = blackBalls.get(black);
                            black = black - 1;
                        } else {
                            whiteBalls.get(white).setPos(i, j);
                            ballsField[i][j] = whiteBalls.get(white);
                            white = white - 1;
                        }
                    } else {
                        ballsField[i][j] = new Ball(null, i , j);
                    }
                } else {
                    ballsField[i][j] = null;
                }
            }
        }
    }

    @Override
    public Board move(int rowFrom, int diagFrom, int rowTo, int diagTo) {

        int rowDirection = rowTo - rowFrom;
        int columnDirection = diagTo - diagFrom;
        if (rowDirection > 1 || rowDirection < -1 || columnDirection > 1 || columnDirection < -1) {
            System.out.println("Error! Target is to far away.");
            return null;
        }

        playingField newBoard;

            newBoard = (playingField) this.clonen();



        //Bewegen wir überhaupt unsere eigenen steine?!

        if (ballsField[rowFrom][diagFrom].getColor() != getHumanColor()) {
            System.out.println("Error! You can only move your own pieces!");
            return null;
        }

        if (!canWePush(rowFrom, diagFrom, rowTo, diagTo, Player.HUMAN)) {
            System.out.println("Error! This move is not possible!");
            return null;
        }

        moveThePiece(rowFrom, diagFrom, rowDirection, columnDirection, newBoard, Player.HUMAN);

        if (isGameOver()) {
            getWinner();
        }

        lastPlayer = Player.HUMAN;
        machineMove();
        return newBoard;
    }

    private Board moveThePiece(int rf, int df, int rD, int cD, Board board, Player player){

        String playersColor;
        String othersColor;
        Player otherPlayer;

        if (player == Player.HUMAN) {
            otherPlayer = Player.MACHINE;
        } else {
            otherPlayer = Player.HUMAN;
        }

        if (player.getColor() == Color.BLACK) {
            playersColor = "X";
            othersColor = "O";
        } else {
            playersColor = "O";
            othersColor = "X";
        }

        if (isValidPosition(rf + rD, df + cD)) {
            if (ballsField[rf + rD][df + cD].getColorRepr().equals(playersColor)) {
                moveThePiece(rf + rD, df + cD, rD, cD, board, player);
                Ball temp = ballsField[rf + rD][df + cD];
                ballsField[rf + rD][df + cD] = ballsField[rf][df];
                ballsField[rf][df] = temp;
            } else if (ballsField[rf + rD][df + cD].getColorRepr().equals(".")) {
                Ball temp = ballsField[rf + rD][df + cD];
                ballsField[rf + rD][df + cD] = ballsField[rf][df];
                ballsField[rf][df] = temp;
            } else if (ballsField[rf + rD][df + cD].getColorRepr().equals(othersColor)) {
                moveThePiece(rf + rD, df + cD, rD, cD, board, otherPlayer);
                Ball temp = ballsField[rf + rD][df + cD];
                ballsField[rf + rD][df + cD] = ballsField[rf][df];
                ballsField[rf][df] = temp;
            }
        } else {
            Ball newBall = new Ball(null ,rf, df);
            ballsField[rf][df] = newBall;
        }

        return board;
    }

    @Override
    public Player getOpeningPlayer() {
        return openingPlayer;
    }

    @Override
    public Color getHumanColor() {
        return Player.HUMAN.getColor();
    }

    @Override
    public Player getNextPlayer() {

        if (lastPlayer == Player.HUMAN) {
            return Player.MACHINE;
        } else {
            if (canHumanMove()) {
                return Player.HUMAN;
            } else {
                return Player.MACHINE;
            }
        }
    }

    private boolean canHumanMove() {



        return true;
    }


    private boolean canWePush(int rF, int dF, int rT, int dT,Player player) {

        int rD = rT - rF;
        int dD = dT - dF;

        int white;
        int black;

        if (player.getColor() == Color.BLACK) {
            white = 0;
            black = 1;
        } else {
            white = 1;
            black = 0;
        }

        while (isValidPosition(rF + rD, dF +  dD) &&!(ballsField[rF + rD][dF +  dD].getColorRepr().equals("."))) {
            if (ballsField[rF + rD][dF + dD].getColorRepr().equals("X")) {
                black = black + 1;
                rF = rF + rD;
                dF = dF + dD;
            } else {
                white = white + 1;
                rF = rF + rD;
                dF = dF + dD;
            }
        }

        if (player.getColor() == Color.BLACK && black > white) {
            return true;
        } else if (player.getColor() == Color.WHITE && white > black) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isValidPosition(int row, int diag) {

        int min;
        int max;

        if (0 > (row - (size / 2))) {
            min = 0;
        } else {
            min = row - (size / 2);
        }
        if (row + (size / 2) < size - 1) {
            max = row + (size / 2);
        } else {
            max = size - 1;
        }

        if (row > size - 1 || row < 0) {
            return false;
        }
        if (diag > max || diag < min) {
            return false;
        }
        return true;
    }

    @Override // min/max aus math klasse
    public boolean isValidTarget(int row, int diag) {

        int min;
        int max;

        if (0 > (row - (size / 2))) {
            min = 0;
        } else {
            min = row - (size / 2);
        }
        if (row + (size / 2) < size - 1) {
            max = row + (size / 2);
        } else {
            max = size - 1;
        }

        if (row > size || row < -1) {
            return false;
        }
        if (diag > max + 1 || diag < min - 1) {
            return false;
        }

        return true;
    }

    @Override
    public Board machineMove() {

        //decideMove();


        //Bewegen wir überhaupt unsere eigenen steine?!


        playingField newBoard;
        newBoard = (playingField) this.clonen();

        newBoard.moveThePiece(8, 8, -1, -1, newBoard, Player.MACHINE);

        if (isGameOver()) {
            getWinner();
        }
        lastPlayer = Player.MACHINE;
        return newBoard;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public boolean isGameOver() {
        return getNumberOfBalls(Color.WHITE) <= maxNumberOfBalls - ELIM || getNumberOfBalls(Color.BLACK) <= maxNumberOfBalls - ELIM;
    }

    @Override
    public Player getWinner() {

        Color a = Player.HUMAN.getColor();

        if (getNumberOfBalls(Color.BLACK) <= maxNumberOfBalls - ELIM) {
            if (a == Color.BLACK) {
                System.out.println("Sorry! Maschine wins.");
                return Player.MACHINE;
            } else {
                System.out.println("Congratulations! You won.");
                return Player.HUMAN;
            }
        } else if (getNumberOfBalls(Color.WHITE) <= maxNumberOfBalls - ELIM) {
            if (a == Color.BLACK) {
                System.out.println("Sorry! Maschine wins.");
                return Player.MACHINE;
            } else {
                System.out.println("Congratulations! You won.");
                return Player.HUMAN;
            }
        }

        return null;
    }

    @Override
    public int getNumberOfBalls(Color color) {
        int numberOfBalls = 0;

        String colRep;
        if (color == Color.BLACK) {
            colRep = "X";
        } else {
            colRep = "O";
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (ballsField[i][j] != null && ballsField[i][j].getColorRepr().equals(colRep)) {
                    numberOfBalls = numberOfBalls + 1;
                }
            }
        }
        return numberOfBalls;
    }

    @Override
    public Color getSlot(int row, int diag) {
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override // Hier noch stringbuilder
    public String toString() {
        String str = "";
        int c = 1;
        for (int i = size - 1; i >= 0; i--) {
            for (int j = 0; j < size; j++) {
                if (i >= size / 2) {
                    if (ballsField[i][j] == null) {
                        str = str + " ";
                    } else {
                        str = str + " " + ballsField[i][j].getColorRepr();
                    }
                } else {
                    if (!(ballsField[i][j] == null)) {
                        if (j == 0) {
                            for (int k = 0; k < c; k++) {
                                str = str + " ";
                            }
                            c++;
                        }
                        str = str + " " + ballsField[i][j].getColorRepr();
                    }
                }
            }
            str = str + "\n";
        }
        return str;
    }

    private double validatePosition(int height) {

        String humanColor;
        String maschineColor;

        if (getHumanColor() == Color.BLACK) {
            humanColor = "X";
            maschineColor = "O";
        } else {
            humanColor = "O";
            maschineColor = "X";
        }

        //N als Vorteil durch Kugelmenge

        int nh = getNumberOfBalls(getHumanColor());
        int nm;

        if (getHumanColor() == Color.BLACK) {
            nm = getNumberOfBalls(Color.WHITE);
        } else {
            nm = getNumberOfBalls(Color.BLACK);
        }

        double N = nm - 1.5 * nh;

        // V als Vorteil durch Gewinner in i Zügen
        double vm;
        double vh;

        if (isGameOver()) {
            if (getWinner() == Player.MACHINE) {
                vm = 5000000 / height;
                vh = 0;
            } else {
                vm = 0;
                vh = 5000000 / height;
            }
        } else {
            vm = 0;
            vh = 0;
        }
        double V = vm - 1.5 * vh;


        // M als Vorteil

        double[] mWerteHuman = new double[nh];
        int counterHuman = 0;
        double[] mWerteMaschine = new double[nm];
        int counterMaschine = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (ballsField[i][j].getColorRepr().equals(humanColor)) {

                    double dI = i - j + size/2;
                    double I = Math.min(i, size - i - 1);
                    double II = Math.min(j, size - j - 1);
                    double III = Math.min(dI, size - dI - 1);

                    mWerteHuman[counterHuman] = Math.min(Math.min(I, II), III);
                    counterHuman = counterHuman + 1;
                }
                if (ballsField[i][j].getColorRepr().equals(maschineColor)) {

                    double dI = i - j + size/2;
                    double I = Math.min(i, size - i - 1);
                    double II = Math.min(j, size - j - 1);
                    double III = Math.min(dI, size - dI - 1);

                    mWerteMaschine[counterMaschine] = Math.min(Math.min(I, II), III);
                    counterMaschine = counterMaschine + 1;
                }
            }
        }
        double M = 0;
        for (int i = 0; i < size / 2; i++) {
            M = M + ((i * mWerteMaschine[i]) - (1.5 * (i * mWerteHuman[i])));
        }

        return size * N + M + V;
    }

    private Board decideMove() {

        int row = 0; //jeweilige Koordinate der Steine
        int diag = 0; //jeweilige Koordinate der Steine

        playingField moveI;
        playingField moveII;
        playingField moveIII;
        playingField moveIV;
        playingField moveV;
        playingField moveVI;

        try {
            moveI = (playingField) this.clone();
            moveII = (playingField) this.clone();
            moveIII = (playingField) this.clone();
            moveIV = (playingField) this.clone();
            moveV = (playingField) this.clone();
            moveVI = (playingField) this.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }

        moveThePiece(row, diag, 0, 1, moveI, getNextPlayer());
        moveThePiece(row, diag, 1, 1, moveII, getNextPlayer());
        moveThePiece(row, diag, 1, 0, moveIII, getNextPlayer());
        moveThePiece(row, diag, 1, -1, moveIV, getNextPlayer());
        moveThePiece(row, diag, -1, -1, moveV, getNextPlayer());
        moveThePiece(row, diag, -1, 0, moveVI, getNextPlayer());

        return null;
    }
}
