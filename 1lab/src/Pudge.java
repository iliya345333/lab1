import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Pudge extends Unit {
    public Pudge(int x, int y) {
        super("Pudge", "U", 150, 25, 10, 60, 2, Color.DARK_GRAY);
        setX(x);
        setY(y);
    }
}