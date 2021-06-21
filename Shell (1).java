import java.util.Scanner;

/**
 * This class works as communication between the System and the user.
 */
final class Shell {

    private static boolean running = true;
    private static Board game;
    private static boolean gameStarted = false;

    private Shell() {

    }

    /**
     * This method calls the run method.
     *
     * @param args
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        run(scanner);
    }

    /**
     * This method is running until the user quits it trough entering 'q'.
     * It runs commands accordingly to the user's input.
     *
     * @param scanner scans the user's inputs.
     */
    static void run(Scanner scanner) {
        while (running) {
            System.out.print("abalone> ");

            String input = scanner.nextLine().toLowerCase();

            if (!input.isBlank()) {
                String[] inputs = input.split("\\s+");

                switch (inputs[0]) {
                    case "quit":
                    case "q":
                        running = false;
                        break;
                    case "h":
                    case "help":
                        help();
                        break;
                    case "new":
                    case "n":

                        if (inputs.length == 1) {

                            game = new playingField(9, Player.HUMAN);
                            gameStarted = true;

                        } else {

                            int[] ints = new int[inputs.length];

                            if (ints.length > 2) {
                                System.out.println("Error! Please "
                                     + "type in the right amount of numbers.");
                                break;

                            } else {
                                try {
                                    for (int i = 1; i < ints.length; i++) {
                                        ints[i] = Integer.parseInt(inputs[i]);
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error! Please "
                                        + "type in 'n' and then only one number.");
                                    break;
                                }
                                if (ints[1] < game.MIN_SIZE) {
                                    System.out.println("Error! n must be higher "
                                            + "or equal 7");
                                } else {
                                    game = new playingField(ints[1], Player.HUMAN);
                                    gameStarted = true;
                                }
                            }
                        }
                        break;
                    case "print":
                    case "p":
                        System.out.println(game.toString());
                        break;
                    case "move":
                    case "m":
                        int[] ints = new int[inputs.length];

                        if (ints.length != 5) {
                            System.out.println("Error! Please "
                                    + "type in the right amount of numbers.");
                            break;

                        } else {
                            try {
                                for (int i = 1; i < ints.length; i++) {
                                    ints[i] = Integer.parseInt(inputs[i]);
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Error! Please "
                                        + "type in 'n' and then only one number.");
                                break;
                            }
                            game.move(ints[1], ints[2], ints[3], ints[4]);
                        }
                        break;
                    case "level":
                    case "l":
                        if (inputs.length != 2) {
                            System.out.println("Error! Type in 'l' and the level, which has to be at least 1.");
                            break;
                        }
                        int level = 1;
                        try {
                            level = Integer.parseInt(inputs[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Error! Level must be given as a number!");
                            break;
                        }
                        if (level < 1) {
                            System.out.println("Error! Level must be at least 1!");
                            break;
                        }
                        game.setLevel(level);
                        break;
                    case "switch":
                    case "s":

                        if (!gameStarted) {
                            game = new playingField(9, Player.MACHINE);
                            System.out.println("New game started. You are O");
                        } else if (gameStarted) {
                            if(game.getOpeningPlayer() == Player.HUMAN) {
                                game = new playingField(game.getSize(), Player.MACHINE);
                                System.out.println("New game started. You are O");
                            } else if (game.getOpeningPlayer() == Player.MACHINE) {
                                game = new playingField(game.getSize(), Player.HUMAN);
                                System.out.println("New game started. You are X");
                            }
                        }
                        break;
                    case "balls":
                    case "b":
                        System.out.println("X: " + game.getNumberOfBalls(Color.BLACK));
                        System.out.println("O: " + game.getNumberOfBalls(Color.WHITE));
                        break;
                    default:
                        System.out.println("Error! The input was not"
                                + " right. Please try again. Type in 'h'"
                                + " to see the help-menu.");
                        break;
                }
            }
        }
    }

    /**
     * This method prints out a help message, in where all the
     * possible commands are listed.
     */
    static void help() {
        System.out.println("--------------------------------------------------"
                + "---------------------------------------------------------");
        System.out.println("Welcome to the help-menu.\n \n"
                + "These are the commands you can choose from:\n"
                + "new (n) s:            Starts a new Game of abalone. "
                + "You can set a size 's'"
                + "of 7 or higher. Default size is 9.\n"
                + "switch (s):           Starts a new Game, but "
                + "switches the starting player.\n"
                + "                      If a game was already "
                + "created, the size of it is being used.\n"
                + "                      Otherwise the default "
                + "size of 9 is being used.\n"
                + "move (m) rF dF rT dT: Move your piece "
                + "from coordinates rF (row from) and dF(column from)\n"
                + "                                          "
                + "             to rT(row to) and dT(diagonal to).\n"
                + "                      Caution: U can only "
                + "move a piece 1 step at a time.\n"
                + "level (l):            Sets the difficulty of the Machine."
                + " 1 as 'easy', 2 as 'medium' and 3 as 'hard'.\n"
                + "print (p):            Prints out the board.\n"
                + "balls (b):            Tells you how many white/black"
                + " pieces are left on the board.\n"
                + "help (h):             Shows this help-menu.\n"
                + "quit (q):             Ends the program.\n"
                + "------------------------------------------------------"
                + "-----------------------------------------------------");
    }

}