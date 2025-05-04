import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Sword extends Unit {
    public Sword(int x, int y) {
        super("Sword", "M", 100, 20, 3, 30, 1, Color.RED);
        setX(x);
        setY(y);
    }
}