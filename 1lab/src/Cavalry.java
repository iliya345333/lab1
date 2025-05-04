import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
public class Cavalry extends Unit {
    public Cavalry(int x, int y) {
        super("Cavalry", "C", 90, 30, 4, 40, 2, Color.MAGENTA);
        setX(x);
        setY(y);
    }
}