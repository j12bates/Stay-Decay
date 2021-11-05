import java.util.Scanner;

class Main {
    // Matrix of chip-states
    static boolean[][] matrix = {
        {false, false, false}, 
        {false, true, false}, 
        {false, false, false}
    };

    static int[] sticky = {4, -1};      // Two sticky chips
    static int[] score = new int[2];    // Current scores
    static boolean player = true;       // Current player (A)

    static Scanner input = new Scanner(System.in);
    
    // Messages
    static String prompt = "Col-Row to flip";
    static String exInvalid = "Invalid Chip ID";
    static String exStuck = "Chip is Stuck";

    // Matrix graphics
    static String matrixHead       = " ┌───┬───┬───┬───┐ ";
    static String matrixDivider    = " ├───┼───┼───┼───┤ ";
    static String matrixFoot       = " └───┴───┴───┴───┘ ";
    static String matrixSeparator  = " │ ";

    public static void main(String[] args) {
        // Print Game Version
        System.out.println("Game version 1.0.1");

        // Primary Game Loop
        do {
            printMatrix();
            printOrders();
            score();
            System.out.println("Score: " + score[0] + "," + score[1]);

            // The player must make a valid move
            do {
                try {
                    turn();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                break;
            } while (true);

        } while (gameExists());

        // End of Game
        printMatrix();
        printOrders();
        score();
        System.out.println(
            "Player " + 
            (score[0] > score[1] ? "A" : "B") + 
            " Wins " + 
            score[0] + "," + score[1]
        );
    }

    // Run one turn
    static void turn() throws Exception {
        // Prompt current player
        System.out.print(
            prompt + 
            " (" + 
            (player ? "A" : "B") + 
            ") "
        );

        int chip = decodeChipID(input.nextLine());          // Get chip
        flip(chip);                                         // Flip chip

        player = !player;
    }

    // Flip a Particular Chip
    static void flip(int chip) throws Exception {
        // Invalidate if this is one of the sticky chips
        if (chip == sticky[0] || chip == sticky[1]) throw new Exception(exStuck);

        sticky[1] = sticky[0];                  // Shift sticky chips over and stick this one
        sticky[0] = chip;

        matrix [chip / 3] [chip % 3] = 
        ! matrix [chip / 3] [chip % 3];         // Flip the boolean on this row/col
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

        p = matrix[2][0];

        for (boolean i : order(false)) {
            if (i != p) score[1]++;
            p = i;
        }
    }

    // Get player's chip order
    static boolean[] order(boolean player) {
        boolean order[] = new boolean[9];       // Array of ordered chip-states

        // Loop through all chips
        for (int i = 0; i < 9; i++) {
            order[i] = player 
                ? matrix [i / 3] [i % 3]        // Player A goes by row
                : matrix [2 - i % 3] [i / 3]    // Player B goes by col (and row is inverted)
            ;
        }

        return order;
    } 

    // Check if the game is over
    static boolean gameExists() {
        // Check diagonals for the same state
        return !(
            ((matrix[1][1] == matrix[0][0]) && (matrix[1][1] == matrix[2][2])) ||
            ((matrix[1][1] == matrix[0][2]) && (matrix[1][1] == matrix[2][0]))
        );
    }

    // Convert a Chip ID (C2) to an integer (7)
    static int decodeChipID(String chip) throws Exception {
        int index = 0;

        if (
            chip.length() != 2 ||                   // Input is not 2 chars long
            (int) chip.charAt(1) - '1' > 2          // Row is not valid
        ) throw new Exception(exInvalid);

        index = 3 * (int) (chip.charAt(1) - '1');   // Interpret row, row has 3 chips

        switch (chip.charAt(0)) {                   // Interpret column
            case 'C':
                index++;
            case 'B':
                index++;
            case 'A':
                break;

            default:                                // Column is not valid
                throw new Exception(exInvalid);
        }

        return index;
    }

    // Print the Entire Matrix
    static void printMatrix() {
        char[] headerRow = {'A', 'B', 'C'};                     // Array of column symbols

        System.out.println(matrixHead);                         // Print the top of the matrix
        printRow(' ', headerRow);

        // Print every row
        for (int row = 0; row < 3; row++) {
            System.out.println(matrixDivider);
            
            char[] rowSym = new char[3];                        // Array to hold characters for chip-states
            
            for (int chip = 0; chip < 3; chip++) {
                rowSym[chip] = matrix[row][chip] ? 'X' : 'O';   // Use an X or an O
            }

            printRow((char) (row + 1 + '0'), rowSym);           // Print this as a row
        }

        System.out.println(matrixFoot);                         // Print the bottom of the matrix
    }

    // Print a Matrix Row
    static void printRow(char label, char[] row) {
        System.out.print(matrixSeparator + label);

        // Print each character in the row
        for (char i : row) {
            System.out.print(matrixSeparator + i);
        }

        System.out.println(matrixSeparator);
    }

    // Print the players' chip orders
    static void printOrders() {
        System.out.print("A: ");
        for (boolean i : order(true)) {
            System.out.print((i ? 'X' : 'O') + " ");
        }
        System.out.println();
        
        System.out.print("B: ");
        for (boolean i : order(false)) {
            System.out.print((i ? 'X' : 'O') + " ");
        }
        System.out.println();
    }
}
