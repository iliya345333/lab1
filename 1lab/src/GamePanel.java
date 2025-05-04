import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel {
    private GameMap map;
    private MainLauncher game;
    private Unit selectedUnit;

    private Image imgPlayerLand;
    private Image imgComputerLand;
    private Image imgObstacle;
    private Image imgPlayerCastle;
    private Image imgComputerCastle;
    private Image imgGrass;
    private Image imgRoad;
    private Image imgFloor;
    private Image imgPike;
    private Image imgAi;
    private Image imgPikeAi;
    private Image imgSword;
    private Image imgSwordAi;
    private Image imgCav;
    private Image imgCavAi;
    private Image imgPaladin;
    private Image imgPaladinAi;
    private Image imgPudge;
    private Image imgPudgeAi;
    private Image imgArbalet;



    public GamePanel(GameMap map, MainLauncher game) {
        this.map = map;
        this.game = game;
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleClick(e);
            }
        });

        imgPlayerLand = new ImageIcon("мент.png").getImage();
        imgObstacle = new ImageIcon("image/дуб.jpeg").getImage();
        imgPlayerCastle = new ImageIcon("image/player_castle.png").getImage();
        imgComputerCastle = new ImageIcon("image/ai_castle.png").getImage();
        imgGrass = new ImageIcon("image/grass.png").getImage();
        imgRoad = new ImageIcon("image/road.png").getImage();
        imgFloor = new ImageIcon("image/floor.png").getImage();
        imgPike = new ImageIcon("image/pike.gif").getImage();
        imgAi = new ImageIcon("image/ai.png").getImage();
        imgPikeAi = new ImageIcon("image/pike_ai.png").getImage();
        imgSword = new ImageIcon("image/sword.gif").getImage();
        imgSwordAi = new ImageIcon("image/sword_ai.png").getImage();
        imgCav = new ImageIcon("image/cav.png").getImage();
        imgCavAi = new ImageIcon("image/cav_ai.gif").getImage();
        imgPaladin = new ImageIcon("image/paladin.gif").getImage();
        imgPaladinAi = new ImageIcon("image/paladin_ai.png").getImage();
        imgPudge = new ImageIcon("пудж.jpg").getImage();
        imgPudgeAi = new ImageIcon("пудж2.jpg").getImage();
        imgArbalet = new ImageIcon("arba.gif").getImage();

    }


    private void handleClick(MouseEvent e) {
        int cs = Math.min(getWidth()/map.getSize(), getHeight()/map.getSize());
        int xx = e.getX()/cs;
        int yy = e.getY()/cs;
        System.out.println("Обнаружен клик в (" + xx + "," + yy + ")");
        if(game.isPlayerTurn()){
            if(selectedUnit == null){
                if(map.getPlayer().getX() == xx && map.getPlayer().getY() == yy){
                    selectedUnit = new Unit("Hero", "H", 100, 20, 4, 0, 1, Color.GREEN);
                    selectedUnit.setX(xx);
                    selectedUnit.setY(yy);
                    System.out.println("Выбран герой игрока.");
                } else {
                    for(Unit u : map.getPlayer().getUnits()){
                        if(u.getX() == xx && u.getY() == yy){
                            selectedUnit = u;
                            System.out.println("Выбран юнит " + u.getName() + ".");
                            break;
                        }
                    }
                }
            } else {
                if(selectedUnit.getName().equals("Hero")){
                    System.out.println("Герой перемещается на (" + xx + "," + yy + ")");
                    map.getPlayer().moveHero(xx, yy);
                }
                else {
                    int dist = Math.abs(selectedUnit.getX()-xx) + Math.abs(selectedUnit.getY()-yy);
                    System.out.println("Юнит " + selectedUnit.getName() + " пытается переместиться на (" + xx + "," + yy + ") на расстояние " + dist);
                    if(dist <= selectedUnit.getMoveRange() && selectedUnit.getMovesLeft() >= dist){
                        if(map.isWalkable(xx, yy, true)){
                            map.getPlayer().moveUnit(selectedUnit, xx, yy);
                            selectedUnit.setMovesLeft(selectedUnit.getMovesLeft()-dist);
                        }
                    } else if(dist <= selectedUnit.getAttackRange()){
                        for(Unit en : map.getComputer().getUnits()){
                            if(en.getX() == xx && en.getY() == yy){

                                if (selectedUnit.canAttack()) { // Проверяем, может ли юнит атаковать
                                    System.out.println("Юнит " + selectedUnit.getName() + " атакует юнита " + en.getName());
                                    selectedUnit.attack(en);
                                    selectedUnit.setHasAttacked(true); // Устанавливаем флаг после атаки

                                    if(!en.isAlive()){
                                        System.out.println("Юнит " + en.getName() + " уничтожен.");
                                        map.getComputer().getUnits().remove(en);
                                        map.getGrid()[xx][yy] = map.getBaseGrid()[xx][yy];
                                        map.getPlayer().setGold(map.getPlayer().getGold()+5);
                                        System.out.println("Игрок получил +5 золота.");
                                    }

                                    map.printToConsole();
                                }

                                else {
                                    System.out.println("Юнит " + selectedUnit.getName() + " уже атаковал в этом ходу.");
                                }
                                break;
                            }
                        }
                    }
                }
                selectedUnit = null;
                repaint();
            }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (map == null) return;
        int cs = Math.min(getWidth() / map.getSize(), getHeight() / map.getSize());
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                String sym = map.getGrid()[i][j];
                Color col = pickColor(sym);
                g.setColor(col);
                g.fillRect(i * cs, j * cs, cs, cs);
                Image imgToDraw = null;
                switch (sym) {
                    case "H":
                        imgToDraw = imgPlayerLand;
                        break;
                    case "AI":
                        imgToDraw = imgAi;
                        break;
                    case "#":
                        imgToDraw = imgObstacle;
                        break;
                    case ".":
                        imgToDraw = imgGrass; // Отрисовка нейтральной зоны
                        break;
                    case "R":
                        imgToDraw = imgRoad;
                        break;
                    case "P":
                        imgToDraw = imgFloor;
                        break;
                    case "C":
                        imgToDraw = imgFloor;
                        break;
                }
                if (imgToDraw != null) {
                    g.drawImage(imgToDraw, i * cs, j * cs, cs, cs, this);
                }
                g.setColor(Color.BLACK);
                g.drawRect(i * cs, j * cs, cs, cs);

                // Отрисовка юнитов игрока
                for (Unit uu : map.getPlayer().getUnits()) {
                    if (uu.getX() == i && uu.getY() == j) {
                        if (uu.getName().equals("Hero")) {

                            g.drawImage(imgPlayerLand, i * cs, j * cs, cs, cs, this);
                        }else if (uu.getName().equals("Pike")) {

                            g.drawImage(imgPike, i * cs, j * cs, cs, cs, this);
                        }else if (uu.getName().equals("Sword")) {
                            g.drawImage(imgSword, i*cs, j*cs, cs,cs,this);}
                        else if (uu.getName().equals("Cavalry")) {
                            g.drawImage(imgCav, i*cs, j*cs, cs,cs,this);
                        }
                        else if (uu.getName().equals("Paladin")) {
                            g.drawImage(imgPaladin, i*cs, j*cs, cs,cs,this);
                        }
                        else if (uu.getName().equals("Pudge")) {
                            g.drawImage(imgPudge, i*cs, j*cs, cs,cs,this);
                        }
                        else if (uu.getName().equals("Arbalet")) {
                            g.drawImage(imgArbalet, i*cs, j*cs, cs,cs,this);
                        }
                        else {

                            g.setColor(uu.getColor());
                            g.fillRect(i * cs, j * cs, cs, cs);
                        }
                    }
                }

                for (Unit uu : map.getComputer().getUnits()) {
                    if (uu.getX() == i && uu.getY() == j) {
                        if (uu.getName().equals("Pike")) {
                            // Рисуем картинку pike_ai
                            g.drawImage(imgPikeAi, i * cs, j * cs, cs, cs, this);
                        } else if (uu.getName().equals("Sword")) {
                            g.drawImage(imgSwordAi, i*cs, j*cs, cs,cs,this);
                        } else if (uu.getName().equals("Cavalry")) {
                            g.drawImage(imgCavAi, i*cs, j*cs, cs,cs,this);
                        }
                        else if (uu.getName().equals("Paladin")) {
                            g.drawImage(imgPaladinAi, i*cs, j*cs, cs,cs,this);
                        }
                        else if (uu.getName().equals("Pudge")) {
                            g.drawImage(imgPudgeAi, i*cs, j*cs, cs,cs,this);
                        }
                        else {
                            // Для остальных юнитов компьютера можно оставить заливку или
                            // сделать отдельные картинки
                            g.setColor(uu.getColor().darker());
                            g.fillRect(i * cs, j * cs, cs, cs);
                        }
                    }
                }



                if (map.getBaseGrid()[i][j].equals("P_Castle")) {
                    g.drawImage(imgPlayerCastle, i * cs, j * cs, cs, cs, this);
                } else if (map.getBaseGrid()[i][j].equals("C_Castle")) {
                    g.drawImage(imgComputerCastle, i * cs, j * cs, cs, cs, this);
                }



                if (sym.equals("AI")) {
                    g.drawImage(imgAi, i * cs, j * cs, cs, cs, this);
                }
            }
        }
    }

    private Color pickColor(String s){
        switch(s){
            case "P": return new Color(173,216,230);
            case "C": return new Color(255,182,193);
            case "H": return new Color(133,130,107);
            case "AI": return Color.YELLOW;
            case "R": return Color.DARK_GRAY;
            case "#": return Color.BLACK;
            default: return Color.LIGHT_GRAY;
        }
    }
}