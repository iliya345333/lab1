import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Building {
    private BuildingType type;
    public Building(BuildingType type) {
        this.type = type;
    }
    public BuildingType getType() {
        return type;
    }
}
