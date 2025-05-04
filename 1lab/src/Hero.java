import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Hero {
    private String name;
    private String symbol;
    private Castle castle;
    private ArrayList<Unit> units;
    private int gold;
    private int x;
    private int y;
    private int health;
    private int moveRange;
    private int movesLeft;
    private int totalDistance;
    private GameMap map;
    private MainLauncher game;
    private boolean testMode;
    private int baseMoveRange;




    public Hero(String name, String symbol, Castle c, GameMap m, MainLauncher g) {
        this.name = name;
        this.symbol = symbol;
        this.castle = c;
        this.x = c.getX();
        this.y = c.getY();
        this.map = m;
        this.game = g;
        this.units = new ArrayList<>();
        this.gold = 1000;
        this.health = 100;
        this.baseMoveRange = 8; // или другое базовое значение
        this.moveRange = this.baseMoveRange;
        this.movesLeft = this.moveRange;
        this.totalDistance = 0;
        this.testMode = false;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public Castle getCastle() { return castle; }
    public ArrayList<Unit> getUnits() { return units; }
    public int getGold() { return gold; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHealth() { return health; }
    public int getMoveRange() { return moveRange; }
    public int getMovesLeft() { return movesLeft; }
    public int getTotalDistance() { return totalDistance; }
    public void setGold(int g) { gold = g; }
    public void setX(int xx) { x = xx; }
    public void setY(int yy) { y = yy; }
    public void setHealth(int hh) { health = hh; }
    public boolean isTestMode() { return testMode; }
    public void setTestMode(boolean tm) {
        testMode = tm;
        if (tm) {
            health = 999;
            gold = 999;
            moveRange = 10;
            movesLeft = 10;
            System.out.println(name + " в тестовом режиме: health=" + health + ", gold=" + gold);
        }
    }

    public boolean hasArmy() {
        return !units.isEmpty();
    }

    public void resetTurn() {

        for (Unit u : units) {
            u.resetMovement();
            u.resetAttack();
        }


        String currentCell = map.getBaseGrid()[x][y];

        if (currentCell.equals(".")) {
            System.out.println(name + " начинает ход в нейтральной зоне. moveRange = 1.");
            this.moveRange = 1;
        } else {

            this.moveRange = this.baseMoveRange;
        }


        this.movesLeft = this.moveRange;

        System.out.println(name + " начинает новый ход. MovesLeft: " + this.movesLeft);


    }


    public void changeMoveRange(int delta) {
        moveRange += delta;
        movesLeft += delta;
        System.out.println(name + " увеличил дальность на " + delta + ". Новая дальность: " + moveRange);
    }

    private int calcMoveCost(int dist, int nx, int ny) {
        int cost = dist;
        String base = map.getBaseGrid()[nx][ny];
        if (false && base.equals(".")) {
            cost += 1;
        }
        return cost;
    }

    public void moveUnit(Unit uu, int nx, int ny) {
        int dist = Math.abs(uu.getX() - nx) + Math.abs(uu.getY() - ny);
        if (dist == 0) return;
        if (map.getBaseGrid()[nx][ny].equals("#")) {
            System.out.println("Клетка (" + nx + "," + ny + ") заблокирована препятствием.");
            return;
        }
        int finalCost = calcMoveCost(dist, nx, ny);
        System.out.println(name + " пытается переместить юнита " + uu.getName() +
                " с (" + uu.getX() + "," + uu.getY() + ") на (" + nx + "," + ny + "). Стоимость: " + finalCost);
        if (uu.getMovesLeft() >= finalCost) {
            if (map.isWalkable(nx, ny, false) && map.positionFreeForHero(nx, ny)) {
                map.getGrid()[uu.getX()][uu.getY()] = map.getBaseGrid()[uu.getX()][uu.getY()];
                uu.setX(nx);
                uu.setY(ny);
                map.getGrid()[nx][ny] = uu.getSymbol();
                uu.setMovesLeft(uu.getMovesLeft() - finalCost);
                if (this == map.getComputer()) {
                    if (map.checkVictory(nx, ny, false)) {
                        System.out.println(name + " юнит достиг зоны врага!");
                        game.checkWin();
                    }
                }
                System.out.println(name + " переместил юнита " + uu.getName() +
                        ". Осталось ходов: " + uu.getMovesLeft());
                map.printToConsole();
            } else {
                System.out.println("Перемещение юнита " + uu.getName() + " невозможно (целая клетка занята).");
            }
        } else {
            System.out.println("Недостаточно ходов для перемещения юнита " + uu.getName());
        }
    }

    public void moveUnitOneStepToward(Unit uu, int tx, int ty) {
        int dx = Integer.compare(tx, uu.getX());
        int dy = Integer.compare(ty, uu.getY());
        int nx = uu.getX() + dx;
        int ny = uu.getY() + dy;
        if (map.getBaseGrid()[nx][ny].equals("#")) {
            System.out.println("Клетка (" + nx + "," + ny + ") заблокирована препятствием.");
            return;
        }
        if (map.isWalkable(nx, ny, false)) {
            moveUnit(uu, nx, ny);
        } else {
            boolean success = false;
            for (int tries = 0; tries < 4; tries++) {
                Random rr = new Random();
                int dir = rr.nextInt(4);
                int altx = uu.getX();
                int alty = uu.getY();
                switch (dir) {
                    case 0 -> alty = uu.getY() - 1;
                    case 1 -> alty = uu.getY() + 1;
                    case 2 -> altx = uu.getX() - 1;
                    case 3 -> altx = uu.getX() + 1;
                }
                if (!map.getBaseGrid()[altx][alty].equals("#") && map.isWalkable(altx, alty, false)) {
                    moveUnit(uu, altx, alty);
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.out.println("Юнит " + uu.getName() + " не нашёл обхода.");
            }
        }
    }

    public void moveAllUnits() {
        int px = map.getPlayer().getCastle().getX();
        int py = map.getPlayer().getCastle().getY();
        for (Unit uu : units) {
            if (uu.getMovesLeft() <= 0) continue;

            // Проверяем, есть ли враги в зоне атаки
            ArrayList<Unit> enemyUnits = (this == map.getPlayer()) ? map.getComputer().getUnits() : map.getPlayer().getUnits();
            Unit closestEnemy = null;
            int minDistance = Integer.MAX_VALUE;

            // Ищем ближайшего врага
            for (Unit enemy : enemyUnits) {
                int distance = Math.abs(uu.getX() - enemy.getX()) + Math.abs(uu.getY() - enemy.getY());
                if (distance < minDistance) {
                    minDistance = distance;
                    closestEnemy = enemy;
                }
            }

            // Если враг в зоне атаки, атакуем его
            if (closestEnemy != null && minDistance <= uu.getAttackRange()) {
                System.out.println(name + " юнит " + uu.getName() + " атакует " + closestEnemy.getName());
                uu.attack(closestEnemy);
                uu.setHasAttacked(true);
                if (!closestEnemy.isAlive()) {
                    System.out.println(name + " уничтожил юнита " + closestEnemy.getName());
                    enemyUnits.remove(closestEnemy);
                    map.getGrid()[closestEnemy.getX()][closestEnemy.getY()] = map.getBaseGrid()[closestEnemy.getX()][closestEnemy.getY()];
                    gold += 5;
                    System.out.println(name + " получает +5 золота за убийство " + closestEnemy.getName());
                }
                map.printToConsole();
            } else if (closestEnemy != null) {
                // Если враг не в зоне атаки, двигаемся к нему
                System.out.println(name + " юнит " + uu.getName() + " двигается к врагу " + closestEnemy.getName());
                moveUnitOneStepToward(uu, closestEnemy.getX(), closestEnemy.getY());
            } else {
                // Если врагов нет, двигаемся к замку
                System.out.println(name + " перемещает юнита " + uu.getName() + " к замку противника (" + px + "," + py + ")");
                moveUnitOneStepToward(uu, px, py);
            }
        }
    }

    public void attackAllUnits() {
        System.out.println(name + " все юниты пытаются атаковать вражеских.");
        ArrayList<Unit> enemyUnits = (this == map.getPlayer()) ? map.getComputer().getUnits() : map.getPlayer().getUnits();
        if (enemyUnits.isEmpty()) {
            System.out.println("Нет вражеских юнитов для атаки.");
            return;
        }
        boolean anyAttack = false;
        for (Unit uu : units) {
            if (!uu.canAttack()) continue;
            Unit closest = null;
            int minD = Integer.MAX_VALUE;
            for (Unit en : enemyUnits) {
                int dd = Math.abs(uu.getX() - en.getX()) + Math.abs(uu.getY() - en.getY());
                if (dd < minD) {
                    minD = dd;
                    closest = en;
                }
            }
            if (closest != null) {
                if (minD <= uu.getAttackRange()) {
                    System.out.println(name + " юнит " + uu.getName() + " атакует " + closest.getName() +
                            " на расстоянии " + minD);
                    uu.attack(closest);
                    uu.setHasAttacked(true);
                    if (!closest.isAlive()) {
                        System.out.println(name + " уничтожил юнита " + closest.getName());
                        enemyUnits.remove(closest);
                        map.getGrid()[closest.getX()][closest.getY()] = map.getBaseGrid()[closest.getX()][closest.getY()];
                        gold += 5;
                        System.out.println(name + " получает +5 золота за убийство " + closest.getName());
                    }
                    anyAttack = true;
                    map.printToConsole();
                } else {
                    System.out.println(name + " юнит " + uu.getName() + " не в зоне атаки (расстояние " + minD +
                            "), делает шаг к " + closest.getName());
                    if (uu.getMovesLeft() > 0) {
                        moveUnitOneStepToward(uu, closest.getX(), closest.getY());
                    }
                }
            }
        }
        if (!anyAttack) {
            Hero enemyHero = (this == map.getPlayer()) ? map.getComputer() : map.getPlayer();
            int d = Math.abs(x - enemyHero.getX()) + Math.abs(y - enemyHero.getY());
            if (d <= 1) {
                System.out.println(name + " герой атакует врага " + enemyHero.getName());
                int heroDamage = 20;
                enemyHero.setHealth(enemyHero.getHealth() - heroDamage);
                if (enemyHero.getHealth() <= 0) {
                    System.out.println(name + " герой уничтожил врага " + enemyHero.getName());
                    enemyHero.setHealth(0);
                    gold += 10;
                    System.out.println(name + " получает +10 золота за убийство героя " + enemyHero.getName());
                }
                movesLeft = 0;
                map.printToConsole();
            } else {
                System.out.println("Ни один юнит не смог атаковать; враг слишком далеко (расстояние " + d + ").");
            }
        }
    }

    private void attackHero() {
        if (movesLeft <= 0) return;
        System.out.println(name + " герой пытается атаковать ближайшего врага.");
        Hero enemyHero = (this == map.getPlayer()) ? map.getComputer() : map.getPlayer();
        int d = Math.abs(x - enemyHero.getX()) + Math.abs(y - enemyHero.getY());
        if (d <= 1) {
            System.out.println(name + " герой атакует врага " + enemyHero.getName());
            int heroDamage = 20;
            enemyHero.setHealth(enemyHero.getHealth() - heroDamage);
            if (enemyHero.getHealth() <= 0) {
                System.out.println(name + " герой уничтожил врага " + enemyHero.getName());
                enemyHero.setHealth(0);
                gold += 10;
                System.out.println(name + " получает +10 золота за убийство героя " + enemyHero.getName());
            }
            movesLeft = 0;
            map.printToConsole();
        } else {
            System.out.println("Враг не в пределах атаки героя (расстояние " + d + ").");
        }
    }

    private void moveHeroStep(int tx, int ty) {
        int dx = Integer.compare(tx, x);
        int dy = Integer.compare(ty, y);
        int nx = x + dx;
        int ny = y + dy;
        if (map.getBaseGrid()[nx][ny].equals("#")) {
            System.out.println("Клетка (" + nx + "," + ny + ") заблокирована препятствием.");
            return;
        }
        if (map.isWalkable(nx, ny, false)) {
            moveHero(nx, ny);
        } else {
            boolean success = false;
            for (int tries = 0; tries < 4; tries++) {
                Random rr = new Random();
                int dir = rr.nextInt(4);
                int altx = x;
                int alty = y;
                switch (dir) {
                    case 0 -> alty = y - 1;
                    case 1 -> alty = y + 1;
                    case 2 -> altx = x - 1;
                    case 3 -> altx = x + 1;
                }
                if (!map.getBaseGrid()[altx][alty].equals("#") && map.isWalkable(altx, alty, false)) {
                    moveHero(altx, alty);
                    success = true;
                    break;
                }
            }
            if (!success) {
                System.out.println("Герой " + name + ": Путь к замку заблокирован, альтернативы не найдены!");
            }
        }
    }

    private void moveHeroToEnemyCastle() {
        int px = map.getPlayer().getCastle().getX();
        int py = map.getPlayer().getCastle().getY();
        System.out.println(name + " герой двигается к замку врага (" + px + "," + py + ")");
        moveHeroStep(px, py);
    }
    private Unit findClosestEnemy(Unit uu) {
        ArrayList<Unit> enemyUnits = (this == map.getPlayer()) ? map.getComputer().getUnits() : map.getPlayer().getUnits();
        Unit closestEnemy = null;
        int minDistance = Integer.MAX_VALUE;

        for (Unit enemy : enemyUnits) {
            int distance = Math.abs(uu.getX() - enemy.getX()) + Math.abs(uu.getY() - enemy.getY());
            if (distance < minDistance) {
                minDistance = distance;
                closestEnemy = enemy;
            }
        }

        return closestEnemy;
    }
    public void aiLogic() {
        System.out.println("Компьютер принимает решение...");

        attackAllUnits();  // Сначала атакуем

        moveAllUnits();    // Затем перемещаемся
        moveHeroToEnemyCastle();
        attackHero();

    }

    private boolean isEnemyNear() {
        ArrayList<Unit> enemyUnits = (this == map.getPlayer()) ? map.getComputer().getUnits() : map.getPlayer().getUnits();
        for (Unit e : enemyUnits) {
            int d = Math.abs(x - e.getX()) + Math.abs(y - e.getY());
            if (d <= 3) return true;
        }
        return false;
    }

    private boolean isWeaker() {
        int myp = calcPower(units);
        ArrayList<Unit> enemyUnits = (this == map.getPlayer()) ? map.getComputer().getUnits() : map.getPlayer().getUnits();
        int enp = calcPower(enemyUnits);
        return myp < enp;
    }

    private int calcPower(ArrayList<Unit> us) {
        int tot = 0;
        for (Unit uu : us) {
            tot += uu.getHealth() + uu.getDamage() * 5;
        }
        return tot;
    }

    public void moveHero(int nx, int ny) {
        int dist = Math.abs(x - nx) + Math.abs(y - ny);
        if (dist == 0) {
            System.out.println("Герой " + name + " уже находится на целевой клетке (" + nx + "," + ny + ").");
            return;
        }
        if (map.getBaseGrid()[nx][ny].equals("#")) {
            System.out.println("Герой " + name + " не может переместиться на (" + nx + "," + ny + ") – препятствие.");
            return;
        }
        int finalCost = calcMoveCost(dist, nx, ny);
        System.out.println(name + " пытается переместить героя с (" + x + "," + y + ") на (" + nx + "," + ny + "). Стоимость: " + finalCost);
        if (movesLeft >= finalCost) {
            if (map.isWalkable(nx, ny, false)) {
                map.getGrid()[x][y] = map.getBaseGrid()[x][y];
                x = nx;
                y = ny;
                map.getGrid()[nx][ny] = symbol;
                movesLeft -= finalCost;
                totalDistance += finalCost;
                boolean iAmPlayer = (this == map.getPlayer());
                if (map.checkVictory(nx, ny, iAmPlayer)) {
                    System.out.println(name + " герой достиг зоны врага!");
                    game.checkWin();
                }
                System.out.println(name + " переместил героя. Осталось ходов: " + movesLeft);
                map.printToConsole();
            } else {
                System.out.println("Герой " + name + " не может переместиться на (" + nx + "," + ny + ")");
            }
        } else {
            System.out.println("Герою " + name + " недостаточно ходов для перемещения.");
        }
    }
}