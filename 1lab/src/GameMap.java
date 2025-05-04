import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GameMap {
    private int size;
    private String[][] grid;
    private String[][] baseGrid;
    private Hero player;
    private Hero computer;
    public GameMap(int size, Castle playerC, Castle compC) {
        this.size = size;
        this.grid = new String[size][size];
        this.baseGrid = new String[size][size];
        initMap(playerC, compC);
    }
    private void initMap(Castle pc, Castle cc) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < size / 3 && j < size / 3) {
                    grid[i][j] = "P";
                } else if (i >= 2 * size / 3 && j >= 2 * size / 3) {
                    grid[i][j] = "C";
                } else {
                    grid[i][j] = ".";
                }
                baseGrid[i][j] = grid[i][j];
            }
        }
        grid[pc.getX()][pc.getY()] = "P_Castle";
        baseGrid[pc.getX()][pc.getY()] = "P_Castle";
        grid[cc.getX()][cc.getY()] = "C_Castle";
        baseGrid[cc.getX()][cc.getY()] = "C_Castle";
        int px = pc.getX();
        int py = pc.getY();
        int cx = cc.getX();
        int cy = cc.getY();
        while (px != cx || py != cy) {
            if (!(px == pc.getX() && py == pc.getY()) && !(px == cc.getX() && py == cc.getY())) {
                grid[px][py] = "R";
                baseGrid[px][py] = "R";
            }
            if (px < cx) px++; else if (px > cx) px--;
            if (py < cy) py++; else if (py > cy) py--;
        }
        addDiagonalObstacles();
        System.out.println("Карта инициализирована. Размер: " + size + "x" + size);
    }
    private void addDiagonalObstacles() {
        int half = size / 2;
        for (int i = 2; i < half; i++) {
            grid[i][size - 1 - i] = "#";
            baseGrid[i][size - 1 - i] = "#";
            grid[size - 1 - i][i] = "#";
            baseGrid[size - 1 - i][i] = "#";
        }
        System.out.println("Диагональные препятствия размещены.");
    }
    public int getSize() { return size; }
    public String[][] getGrid() { return grid; }
    public String[][] getBaseGrid() { return baseGrid; }
    public Hero getPlayer() { return player; }
    public Hero getComputer() { return computer; }
    public void setPlayer(Hero h) { player = h; }
    public void setComputer(Hero c) { computer = c; }
    public boolean checkVictory(int x, int y, boolean isPlayer) {
        String zone = baseGrid[x][y];
        if (isPlayer) {
            // Проверяем, находится ли игрок в замке компьютера
            if (zone.equals("C_Castle")) {
                System.out.println("Победа игрока: вошёл в замок противника (" + x + "," + y + ")");
                return true;
            }
        } else {
            // Проверяем, находится ли компьютер в замке игрока
            if (zone.equals("P_Castle")) {
                System.out.println("Победа компьютера: вошёл в замок игрока (" + x + "," + y + ")");
                return true;
            }
        }
        return false;
    }
    public boolean isWalkable(int x, int y, boolean isPlayer) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            System.out.println("Координаты вне карты: (" + x + "," + y + ")");
            return false;
        }
        if (baseGrid[x][y].equals("#")) {
            System.out.println("Клетка заблокирована препятствием: (" + x + "," + y + ")");
            return false;
        }
        if (cellOccupied(x, y)) {
            System.out.println("Клетка занята: (" + x + "," + y + ")");
            return false;
        }
        return true;
    }
    private boolean cellOccupied(int x, int y) {
        for (Unit u : player.getUnits()) {
            if (u.getX() == x && u.getY() == y) return true;
        }
        for (Unit u : computer.getUnits()) {
            if (u.getX() == x && u.getY() == y) return true;
        }
        if ((player.getX() == x && player.getY() == y)
                || (computer.getX() == x && computer.getY() == y)) return true;
        return false;
    }
    public boolean positionFreeForHero(int x, int y) {
        return (player.getX() != x || player.getY() != y)
                && (computer.getX() != x || computer.getY() != y);
    }
    public void printToConsole() {
        System.out.println("Текущее состояние карты:");
        for (int j = 0; j < size; j++) {
            for (int i = 0; i < size; i++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
