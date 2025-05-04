import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class Arbalet extends Unit {
    public Arbalet(int x, int y) {
        super("Arbalet", "A", 60, 25, 2, 20, 3, Color.GREEN);
        setX(x);
        setY(y);
    }
}