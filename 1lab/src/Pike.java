import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
public class Pike extends Unit {
    public Pike(int x, int y) {
        super("Pike", "P", 80, 15, 2, 10, 1, Color.BLUE);
        setX(x);
        setY(y);
    }
}