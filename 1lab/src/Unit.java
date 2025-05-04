import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Unit {
    private String name;
    private String symbol;
    private int health;
    private int damage;
    private int moveRange;
    private int cost;
    private int attackRange;
    private Color color;
    private int x;
    private int y;
    private int movesLeft;
    private boolean hasAttacked;

    public Unit(String name, String symbol, int health, int damage,
                int moveRange, int cost, int attackRange, Color color) {
        this.name = name;
        this.symbol = symbol;
        this.health = health;
        this.damage = damage;
        this.moveRange = moveRange;
        this.cost = cost;
        this.attackRange = attackRange;
        this.color = color;
        this.x = -1;
        this.y = -1;
        this.movesLeft = moveRange;
        this.hasAttacked = false;
    }

    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public int getHealth() { return health; }
    public int getDamage() { return damage; }
    public int getMoveRange() { return moveRange; }
    public int getCost() { return cost; }
    public int getAttackRange() { return attackRange; }
    public Color getColor() { return color; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getMovesLeft() { return movesLeft; }
    public void setX(int xx) { x = xx; }
    public void setY(int yy) { y = yy; }
    public void setMovesLeft(int m) { movesLeft = m; }
    public void setHealth(int h) { health = h; }
    public boolean isAlive() { return health > 0; }
    public void resetMovement() { movesLeft = moveRange; }
    public void resetAttack() { hasAttacked = false; }
    public boolean canAttack() { return !hasAttacked; }
    public void setHasAttacked(boolean b) { hasAttacked = b; }

    public void attack(Unit target) {
        target.health -= this.damage;
        System.out.println(name + " наносит " + damage + " урона " + target.name);
        if (target.health <= 0) {
            target.health = 0;
            System.out.println(target.name + " уничтожен!");
        }
    }
}
// вынести юнитов в класс с наследованием класса юнит и добавить функционал к паладину (лечение в радиусе 1 клетки )