import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public enum BuildingType {
    TAVERN(30, "Таверна", "Найм героев"),
    GUARD_TOWER(20, "Сторожевой пост", "Найм копейщиков"),
    ARCHERY_RANGE(30, "Башня арбалетчиков", "Найм арбалетчиков"),
    ARMORY(40, "Оружейная", "Найм мечников"),
    STABLE(50, "Конюшня", "Увеличивает перемещение героев"),
    ARENA(60, "Арена", "Найм кавалеристов"),
    CATHEDRAL(80, "Собор", "Найм паладинов");

    public final int cost;
    public final String name;
    public final String desc;

    BuildingType(int cost, String name, String desc) {
        this.cost = cost;
        this.name = name;
        this.desc = desc;
    }
}
