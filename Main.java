import java.util.Scanner;

class Main {
    // Matrix of Chip-States
    static int x = 3;
    static boolean[][] matrix;

    // Sticky chips
    static int[] sticky;

    static int[] score = new int[2];            // Current scores
    static int turn = -1;                       // Current turn

    static Scanner input = new Scanner(System.in);
    
    // Messages
    static String prompt = "Col-Row to flip";
    static String exInvalid = "Invalid Chip ID";
    static String exStuck = "Chip is Stuck";

    // Matrix graphics
    static String matrixDivider;
    static String matrixHead, matrixFoot;

    static String matrixSeparator   = " │ ";

    // ANSI escape sequences for formatting
    static String fmtClear          = "\033[0m";
    static String fmtChipState      = "\033[1m";
    static String fmtStateX         = "\033[34m";
    static String fmtStateO         = "\033[31m";
    static String fmtOrderChange    = "\033[92;1m";
    static String fmtChipSticky     = "\033[47;1m";
    static String fmtInvalid        = "\033[41m";

    static String resetCursor;
    static String upCursor          = "\033[2F\033[J";

    public static void main(String[] args) {
        if (args.length > 0) {
            x = Integer.parseInt(args[0]);
        }

        // Print Game Version
        System.out.println("Game version 1.2");
        if (!init()) {
            System.out.println("Incorrectly compiled, exiting");
            return;
        }

        // Primary Game Loop
        do {
            // Next turn
            turn++;

            // Print UI
            printMatrix();
            printOrder(true);
            printOrder(false);
            score();
            System.out.println("Score: " + score[0] + "," + score[1]);
            System.out.println();

            // The player must make a valid move
            do {
                try {
                    turn();
                } catch (Exception e) {
                    // Print any error message in red
                    System.out.println(upCursor + fmtInvalid + e.getMessage() + fmtClear);
                    continue;
                }
                break;
            } while (true);

            // Set cursor back up and log turn
            System.out.print(resetCursor);
            System.out.println(encodeChipID(sticky[turn % (x - 1)]));
        } while (gameExists());

        // End of Game
        printMatrix();
        printOrder(true);
        printOrder(false);
        score();

        boolean winner;

        if (score[0] == score[1]) winner = !player();
        else winner = score[0] > score[1];

        System.out.println(
            "Player " + 
            (winner ? "A" : "B") + 
            " Wins " + 
            score[0] + "," + score[1]
        );
    }

    // Initialize matrix
    static boolean init() {
        matrix = new boolean[x][x];                         // Initialize matrix
        sticky = new int[x - 1];                            // Initialize sticky chips

        // Fill matrix
        for (int row = 0; row < x; row++) {
            for (int col = 0; col < x; col++) {
                matrix[row][col] =                          // For any matrix, do the centre
                    row == x / 2 && col == x / 2;

                if (x % 2 == 0) matrix[row][col] =          // For even matrices, do the whole centre
                    row <= x / 2 && row >= x / 2 - 1 &&
                    col <= x / 2 && col >= x / 2 - 1;
            }
        }

        // Fill sticky chips
        for (int i = 0; i < sticky.length; i++) sticky[i] = -1;

        sticky[x - 2] = x % 2 == 0 ? -1 : (x * x / 2);      // Regenerate dependencies
        matrixDivider = " ├" + "───┼".repeat(x) + "───┤"; 
        resetCursor = "\033[" + (8 + 2 * x) + "F\033[J";

        matrixHead = matrixDivider.replace('├', '┌').replace('┼', '┬').replace('┤', '┐');
        matrixFoot = matrixDivider.replace('├', '└').replace('┼', '┴').replace('┤', '┘');

        return x <= 9;
    }

    // Get player as a boolean
    static boolean player() {
        return turn % 2 == 0;
    }

    // Get player's name
    static String playerSymbol(boolean player) {
        return player ? "A" : "B";
    }

    // Run one turn
    static void turn() throws Exception {
        // Prompt current player
        System.out.print(
            prompt +
            " (" + 
            playerSymbol(player()) + 
            ") "
        );

        int chip = decodeChipID(input.nextLine());          // Get chip
        flip(chip);                                         // Flip chip
    }

    // Flip a Particular Chip
    static void flip(int chip) throws Exception {
        // Invalidate if this is one of the sticky chips
        if (chipSticky(chip)) throw new Exception(exStuck);

        sticky[turn % (x - 1)] = chip;          // Stick this one in the next sticky slot

        matrix [chip / x] [chip % x] = 
        ! matrix [chip / x] [chip % x];         // Flip the boolean on this row/col
    }

    // Analyse Scores
    static void score() {
        boolean p = matrix[0][0];               // Don't count the first chip as a change

        score[0] = 0;                           // Reset scores
        score[1] = 0;

        for (boolean i : order(true)) {         // Iterate through chip order
            if (i != p) score[0]++;
            p = i;
        }

        p = matrix[x - 1][0];

        for (boolean i : order(false)) {
            if (i != p) score[1]++;
            p = i;
        }
    }

    // Get player's chip order
    static boolean[] order(boolean player) {
        boolean order[] = new boolean[x * x];       // Array of ordered chip-states

        // Loop through all chips
        for (int i = 0; i < x * x; i++) {
            order[i] = player 
                ? matrix [i / x] [i % x]            // Player A goes by row
                : matrix [x - 1 - i % x] [i / x]    // Player B goes by col (and row is inverted)
            ;
        }

        return order;
    } 

    // Determine if a chip is sticky
    static boolean chipSticky(int chip) {
        // Check through all sticky chips
        for (int i : sticky) if (chip == i) return true;

        return false;
    }

    // Check if the game is over
    static boolean gameExists() {
        boolean diagonalBroken = false;
        
        // Check diagonal A
        for (int i = 0; i < x; i++) {
            if (matrix[i][i] != matrix[0][0]) diagonalBroken = true;
        }

        if (!diagonalBroken) return false;

        // Check diagonal B
        for (int i = 0; i < x; i++) {
            if (matrix[i][x - 1 - i] != matrix[x - 1][0]) return true;
        }

        return false;
    }

    // Convert a Chip ID to an integer
    static int decodeChipID(String chip) throws Exception {
        // Input is invalid
        if (chip.length() != 2) throw new Exception(exInvalid);

        // Interpret row/col
        int row = chip.charAt(1) - '1';
        int col = chip.charAt(0) - 'A';

        if (
            row >= x || row < 0 ||                  // Row is not valid
            col >= x || col < 0                     // Col is not valid
        ) throw new Exception(exInvalid);

        return row * x + col;
    }

    // Convert an Integer to a Chip ID
    static String encodeChipID(int index) {
        String chip = "";
        index %= x * x;                             // Make sure this is in-bounds

        chip += (char) ((index % x) + 'A');         // Add Row
        chip += index / x + 1;                      // Add Col

        return chip;
    }

    // Print the Entire Matrix
    static void printMatrix() {
        System.out.println(matrixHead);                                         // Print the top of the matrix

        // Print the heading
        System.out.print(matrixSeparator + " ");
        for (char i = 0; i < x; i++) {
            System.out.print(
                matrixSeparator + 
                (char) (i + 'A')
            );
        }

        System.out.println(matrixSeparator);

        // Print every row
        for (int row = 0; row < x; row++) {
            System.out.println(matrixDivider);                                  // Print a divider between rows
            System.out.print(matrixSeparator + (row + 1));                      // Row number
            
            // Print every chip
            for (int col = 0; col < x; col++) {
                System.out.print(
                    matrixSeparator + 
                    (chipSticky(x * row + col)
                    ? fmtChipSticky : fmtChipState) +                           // If the chip is sticky, highlight it, otherwise, just bold
                    (matrix[row][col] ? fmtStateX + "X" : fmtStateO + "O") +    // Print the appropriate symbol for the chip state
                    fmtClear
                );
            }

            System.out.println(matrixSeparator);
        }

        System.out.println(matrixFoot);                                         // Print the bottom of the matrix
    }

    // Print a player's chip order
    static void printOrder(boolean player) {
        boolean p = order(player)[0];
        System.out.print(playerSymbol(player) + ": ");

        for (boolean i : order(player)) {
            if (i != p) System.out.print(fmtOrderChange);
            System.out.print((i ? "X" : "O") + fmtClear + " ");
            p = i;
        }

        System.out.println();
    }        
}
