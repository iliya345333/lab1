import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Castle {
    private String symbol;
    private int x;
    private int y;
    private ArrayList<Building> buildings;
    public Castle(String symbol, int x, int y) {
        this.symbol = symbol;
        this.x = x;
        this.y = y;
        this.buildings = new ArrayList<>();
    }
    public String getSymbol() {
        return symbol;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public ArrayList<Building> getBuildings() {
        return buildings;
    }
    public void build(BuildingType type) {
        buildings.add(new Building(type));
        System.out.println(type.name + " построено!");
    }
    public boolean hasBuilding(BuildingType type) {
        for (Building b : buildings) {
            if (b.getType() == type) return true;
        }
        return false;
    }
}
