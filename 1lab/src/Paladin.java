import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
public class Paladin extends Unit {
    public Paladin(int x, int y) {
        super("Paladin", "K", 120, 35, 3, 50, 1, Color.ORANGE);
        setX(x);
        setY(y);
    }

    public void healNearbyUnits(GameMap map) {
        for (int i = getX() - 1; i <= getX() + 1; i++) {
            for (int j = getY() - 1; j <= getY() + 1; j++) {
                if (i >= 0 && i < map.getSize() && j >= 0 && j < map.getSize()) {
                    for (Unit unit : map.getPlayer().getUnits()) {
                        if (unit.getX() == i && unit.getY() == j && unit != this) {
                            unit.setHealth(unit.getHealth() + 10); // Лечение на 10 HP
                            System.out.println("Paladin вылечил " + unit.getName() + " на 10 HP.");
                        }
                    }
                }
            }
        }
    }
}