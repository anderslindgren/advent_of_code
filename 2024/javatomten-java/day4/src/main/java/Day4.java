import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day4 {

    public static void main(String[] args) throws IOException {
        List<String> lines = parseFile(args);
        char[][] board = toBoard(lines);
//        printBoard(board);
        int count = findXmases(board);
        System.out.println(count);
        int count2 = findXmases2(board);
        System.out.println(count2);
    }

    private static int findXmases(char[][] board) {
        int result = 0;
        int maxY = board.length;
        for (int y = 0; y < maxY; y++) {
            int maxX = board[0].length;
            for (int x = 0; x < maxX; x++) {
                char c = board[x][y];
                if (c == 'X') {
//                    System.out.println("x:" + x + ", y:" + y);
                    if (x >= 3) {
                        // Check W
                        result += checkW(board, x, y);
                        if (y >= 3) {
                            // Check NW
                            result += checkNW(board, x, y);
                        }
                    }
                    if (y >= 3) {
                        // Check N
                        result += checkN(board, x, y);
                        if (x < maxX - 3) {
                            // Check NE
                            result += checkNE(board, x, y);
                        }
                    }
                    if (x < maxX - 3) {
                        // Check E
                        result += checkE(board, x, y);
                        if (y < maxY - 3) {
                            // Check SE
                            result += checkSE(board, x, y);
                        }
                    }
                    if (y < maxY - 3) {
                        // Check S
                        result += checkS(board, x, y);
                        if (x >= 3) {
                            // Check SW
                            result += checkSW(board, x, y);
                        }
                    }
                }
            }
        }

        return result;
    }

    private static int findXmases2(char[][] board) {
        int result = 0;
        int maxY = board.length;
        for (int y = 1; y < maxY - 1; y++) {
            int maxX = board[0].length;
            for (int x = 1; x < maxX - 1; x++) {
                char c = board[x][y];
                if (c == 'A') {
//                    System.out.println("x:" + x + ", y:" + y);
                    result += checkCross(board, x, y);
                }
            }
        }

        return result;
    }

    private static int checkCross(char[][] board, int x, int y) {
        if (((board[x + 1][y - 1] == 'M' && board[x - 1][y + 1] == 'S') ||
                (board[x + 1][y - 1] == 'S' && board[x - 1][y + 1] == 'M')) &&
                ((board[x - 1][y - 1] == 'M' && board[x + 1][y + 1] == 'S') ||
                        (board[x - 1][y - 1] == 'S' && board[x + 1][y + 1] == 'M'))
        ) {
            return 1;
        }
        return 0;
    }

    private static int checkW(char[][] board, int x, int y) {
        if (board[x - 1][y] == 'M' && board[x - 2][y] == 'A' && board[x - 3][y] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkN(char[][] board, int x, int y) {
        if (board[x][y - 1] == 'M' && board[x][y - 2] == 'A' && board[x][y - 3] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkNW(char[][] board, int x, int y) {
        if (board[x - 1][y - 1] == 'M' && board[x - 2][y - 2] == 'A' && board[x - 3][y - 3] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkNE(char[][] board, int x, int y) {
        if (board[x + 1][y - 1] == 'M' && board[x + 2][y - 2] == 'A' && board[x + 3][y - 3] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkE(char[][] board, int x, int y) {
        if (board[x + 1][y] == 'M' && board[x + 2][y] == 'A' && board[x + 3][y] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkS(char[][] board, int x, int y) {
        if (board[x][y + 1] == 'M' && board[x][y + 2] == 'A' && board[x][y + 3] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkSW(char[][] board, int x, int y) {
        if (board[x - 1][y + 1] == 'M' && board[x - 2][y + 2] == 'A' && board[x - 3][y + 3] == 'S') {
            return 1;
        }
        return 0;
    }

    private static int checkSE(char[][] board, int x, int y) {
        if (board[x + 1][y + 1] == 'M' && board[x + 2][y + 2] == 'A' && board[x + 3][y + 3] == 'S') {
            return 1;
        }
        return 0;
    }


/*
    private static void printBoard(char[][] board) {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[0].length; x++) {
                char c = board[x][y];
                System.out.print(c);
            }
            System.out.println();
        }
    }
*/

    private static char[][] toBoard(List<String> lines) {
        int sizeX = lines.getFirst().length();
        int sizeY = lines.size();
        char[][] result = new char[sizeX][sizeY];
        for (int y = 0; y < lines.size(); y++) {
            String line = lines.get(y);
            for (int x = 0; x < line.length(); x++) {
                char c = line.charAt(x);
                result[x][y] = c;
            }
        }
        return result;
    }

    private static List<String> parseFile(String[] args) throws IOException {
        String file = "input.txt";
        if (args.length == 1) {
            file = args[0];
        }
        Path path = FileSystems.getDefault().getPath(file);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines().toList();
        }
    }

}
