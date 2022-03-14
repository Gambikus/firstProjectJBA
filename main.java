package battleship;
import java.util.Scanner;
import java.lang.Math;

class Field {
    private char[][] field;
    private int n = 10;
    private int nShips = 5;
    public Field() {
        this.field = new char[n + 2][n + 2];
        for (int i = 0; i < this.n + 2; i++) {
            for (int j = 0; j < this.n + 2; j++) {
                this.field[i][j] = '~';
            }
        }
    }
    
    public int getNumberOfShips() {
        return nShips;
    }
    
    public void printField() {
        System.out.print("  ");
        System.out.println("1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < this.n + 1; i++) {
            System.out.print((char)('A' + i - 1));
            for (int j = 1; j < this.n + 1; j++) {
                System.out.print(" " + this.field[i][j]);
            }
            System.out.println();
        }
    }
    
    public void printEnemyField() {
        System.out.print("  ");
        System.out.println("1 2 3 4 5 6 7 8 9 10");
        for (int i = 1; i < this.n + 1; i++) {
            System.out.print((char)('A' + i - 1));
            for (int j = 1; j < this.n + 1; j++) {
                System.out.print(" " + (this.field[i][j] == 'O' ? '~': this.field[i][j]));
            }
            System.out.println();
        }
    }
    
    private boolean isCoordOk(int x) {
        return 0 < x && x < this.n + 1;
    }
    
    public int addShip(int x1, int y1, int x2, int y2, int length, String shipName) {
        if (x1 == x2 || y1 == y2) {
            if (isCoordOk(x1) && isCoordOk(y1) && isCoordOk(y2) && isCoordOk(x2)) {
                if (Math.abs(x1 - x2) + Math.abs(y1 - y2) == length - 1) {
                    for (int i = Math.min(x1, x2) - 1; i <= Math.max(x1, x2) + 1; i++) {
                        for (int j = Math.min(y1, y2) - 1; j <= Math.max(y1, y2) + 1; j++) {
                            if (this.field[i][j] == 'O') {
                                System.out.println("Error! You placed it too close to another one. Try again:");
                                System.out.println();
                                return -1;
                            }
                        }
                    }
                    for (int i = Math.min(x1, x2); i < Math.max(x1, x2) + 1; i++) {
                        for (int j = Math.min(y1, y2); j < Math.max(y1, y2) + 1; j++) {
                            this.field[i][j] = 'O';
                        }
                    }
                    this.printField();
                    System.out.println();
                    return 1;
                } else {
                    System.out.println("Error! Wrong length of the " + shipName +"! Try again:");
                    System.out.println();
                    return -1;
                }
            } else {
                System.out.println("Error! Wrong ship location! Try again:");
                System.out.println();
                return -1;    
            }
        } else {
            System.out.println("Error! Wrong ship location! Try again:");
            System.out.println();
            return -1;
        }
    }
    
    private boolean isLastCell(int x, int y) {
        boolean res = true;
        for (int i = x + 1; this.field[i][y] != '~' && this.field[i][y] != 'M'; i++) {
            res = field[i][y] != 'O' && res;
        }
        for (int i = y + 1; this.field[x][i] != '~' && this.field[x][i] != 'M'; i++) {
            res = field[x][i] != 'O' && res;
        }
        for (int i = x - 1; this.field[i][y] != '~' && this.field[i][y] != 'M'; i--) {
            res = field[i][y] != 'O' && res;
        }
        for (int i = y - 1; this.field[x][i] != '~' && this.field[x][i] != 'M'; i--) {
            res = field[x][i] != 'O' && res;
        }
        return res;
    }
    
    public int takeShot(int x, int y, Field otherField) {
        if (isCoordOk(x) && isCoordOk(y)) {
            if (this.field[x][y] == 'O' || this.field[x][y] == 'X') {
                int a = 1;
                if (this.field[x][y] == 'X') {
                    a = 0;
                }
                this.field[x][y] = 'X';
                this.printEnemyField();
                System.out.println("---------------------");
                otherField.printField();
                System.out.println();
                if (this.isLastCell(x, y)) {
                    nShips -= a;
                    if (nShips == 0) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                    } else {
                        System.out.println("You sank a ship! Specify a new target:");
                    }
                } else {
                    System.out.println("You hit a ship!");
                }
                System.out.println("Press Enter and pass the move to another player");
                System.out.println();
            } else {
                this.field[x][y] = 'M';
                this.printEnemyField();
                System.out.println("---------------------");
                otherField.printField();
                System.out.println();
                System.out.println("You missed.");
                System.out.println("Press Enter and pass the move to another player");
                System.out.println();
            }
            return 1;
        } else {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            System.out.println();
            return -1;
        }
    }
    
    
}

public class Main {
    public static void parseCoord(String s, int[] coords) {
        coords[0] = s.charAt(0) - 'A' + 1;
        coords[1] = Integer.parseInt(s.substring(1, s.length()));
    }
    
    public static void fillField(String[][] ships, Field playerField) {
        playerField.printField();
        System.out.println();
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < ships.length; i++) {
            int res = -1;
            while (res == -1) {
                System.out.println("Enter the coordinates of the " + ships[i][0] + " (" + ships[i][1] + " cells):");
                System.out.println();
                String s = sc.next();
                int[] coords1 = new int[2];
                parseCoord(s, coords1);
                s = sc.next();
                int[] coords2 = new int[2];
                parseCoord(s, coords2);
                s = sc.nextLine();
                System.out.println();
                res = playerField.addShip(coords1[0], coords1[1], coords2[0], coords2[1], 
                                          Integer.parseInt(ships[i][1]), ships[i][0]);
            }
        }
    }
    
    public static void shoot(Field playerField, Field otherField) {
        int res = -1;
        Scanner sc = new Scanner(System.in);
        while (res == -1) {
            String s = sc.nextLine();
            int[] coords = new int[2];
            parseCoord(s, coords);
            System.out.println();
            res = playerField.takeShot(coords[0], coords[1], otherField);
        }
    }
    
    public static void game(Field firstField, Field secondField) {
        int player = 1;
        /* System.out.println("The game starts!");
        System.out.println(); */
        Scanner sc = new Scanner(System.in);
        while (firstField.getNumberOfShips() != 0 && secondField.getNumberOfShips() != 0) {
            if (player == 1) {
                secondField.printEnemyField();
                System.out.println("---------------------");
                firstField.printField();
                System.out.println();
                System.out.println("Player 1, it's your turn:");
                System.out.println();
                shoot(secondField, firstField);
            } else {
                firstField.printEnemyField();
                System.out.println("---------------------");
                secondField.printField();
                System.out.println();
                System.out.println("Player 2, it's your turn:");
                System.out.println();
                shoot(firstField, secondField);
            }
            player = (player + 1) % 2;
            String s = sc.nextLine();
        }
    }
    
    public static void startGame(Field firstField, Field secondField, String[][] ships) {
        System.out.println("Player 1, place your ships on the game field");
        System.out.println();
        fillField(ships, firstField);
        System.out.println("Press Enter and pass the move to another player");
        System.out.println();
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println("Player 2, place your ships on the game field");
        System.out.println();
        fillField(ships, secondField);
        System.out.println("Press Enter and pass the move to another player");
        s = sc.nextLine();
        System.out.println();
    }
    
    public static void main(String[] args) {
        Field firstField = new Field();
        Field secondField = new Field();
        String[][] ships = {{"Aircraft Carrier", "5"}, {"Battleship", "4"}, {"Submarine", "3"}, {"Cruiser", "3"},
                            {"Destroyer", "2"}};
        
        startGame(firstField, secondField, ships);
        
        game(firstField, secondField);
    }
}
