import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainLauncher extends JFrame {
    private static final int SIZE = 15;
    private GamePanel panel;
    private GameMap map;
    private Hero player;
    private Hero computer;
    private boolean playerTurn = true;

    public MainLauncher() {
        setTitle("Heroes of IU3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(800,600);

        Castle pc = new Castle("P_Castle", SIZE/6, SIZE/6);
        Castle cc = new Castle("C_Castle", 5*SIZE/6, 5*SIZE/6);

        map = new GameMap(SIZE, pc, cc);
        panel = new GamePanel(map, this);
        add(panel, BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(3,1));
        JPanel builds = new JPanel();
        builds.setBorder(BorderFactory.createTitledBorder("Строительства"));
        for (BuildingType bt : BuildingType.values()) {
            JButton b = new JButton(bt.name + " (" + bt.cost + ")");
            b.addActionListener(ev -> buildBT(bt));
            builds.add(b);
        }
        right.add(builds);

        JPanel units = new JPanel();
        units.setBorder(BorderFactory.createTitledBorder("Найм юнитов"));
        JButton pikeB = new JButton("Pike(10)");
        pikeB.addActionListener(ev -> hireUnit("Pike", 10));
        units.add(pikeB);
        JButton arbB = new JButton("Arbalet(20)");
        arbB.addActionListener(ev -> hireUnit("Arbalet", 20));
        units.add(arbB);
        JButton swB = new JButton("Sword(30)");
        swB.addActionListener(ev -> hireUnit("Sword", 30));
        units.add(swB);
        JButton cavB = new JButton("Cavalry(40)");
        cavB.addActionListener(ev -> hireUnit("Cavalry", 40));
        units.add(cavB);
        JButton palB = new JButton("Paladin(50)");
        palB.addActionListener(ev -> hireUnit("Paladin", 50));
        units.add(palB);
        JButton pudgeB = new JButton("Pudge(60)");
        pudgeB.addActionListener(ev -> hireUnit("Pudge", 60));
        units.add(pudgeB);
        right.add(units);

        JPanel tavernPanel = new JPanel();
        tavernPanel.setBorder(BorderFactory.createTitledBorder("Таверна"));
        JButton tavernHire = new JButton("Найм героя (50)");
        tavernHire.addActionListener(ev -> hireHeroFromTavern(50));
        tavernPanel.add(tavernHire);
        right.add(tavernPanel);

        add(right, BorderLayout.EAST);

        JButton endTurn = new JButton("End Turn");
        endTurn.addActionListener(ev -> endPlayerTurn());
        add(endTurn, BorderLayout.SOUTH);

        initGame(pc, cc);
        System.out.println("Игра инициализирована.");
        setLocationRelativeTo(null);
    }

    public void checkWin(){
        if(!player.hasArmy() && player.getUnits().isEmpty()){
            System.out.println("Победа компьютера: у игрока нет армии.");
            JOptionPane.showMessageDialog(this, "Игрок проиграл (нет армии)!");
            System.exit(0);
        }
        if(!computer.hasArmy() && computer.getUnits().isEmpty()){
            System.out.println("Победа игрока: у компьютера нет армии.");
            JOptionPane.showMessageDialog(this, "Компьютер проиграл (нет армии)!");
            System.exit(0);
        }
        // Проверяем только героев
        if(map.checkVictory(player.getX(), player.getY(), true)){
            System.out.println("Победа игрока: герой игрока захватил замок!");
            JOptionPane.showMessageDialog(this, "Игрок захватил замок противника!");
            System.exit(0);
        }
        if(map.checkVictory(computer.getX(), computer.getY(), false)){
            System.out.println("Победа компьютера: герой компьютера захватил замок!");
            JOptionPane.showMessageDialog(this, "Компьютер захватил ваш замок!");
            System.exit(0);
        }
        // Удален цикл проверки юнитов компьютера
    }

    private void buildBT(BuildingType bt) {
        // Проверка, находится ли герой в замке
        if (player.getX() != player.getCastle().getX() || player.getY() != player.getCastle().getY()) {
            System.out.println("Строительство не выполнено: герой должен находиться в замке.");
            return;
        }

        if (player.getGold() < bt.cost) {
            System.out.println("Строительство не выполнено: недостаточно золота.");
            return;
        }

        if (bt == BuildingType.STABLE) {
            player.setGold(player.getGold() - bt.cost);
            player.getCastle().build(bt);
            player.changeMoveRange(2);
            System.out.println("Конюшня построена, дальность увеличена.");
        } else {
            player.setGold(player.getGold() - bt.cost);
            player.getCastle().build(bt);
        }
        map.printToConsole();
    }

    private void hireUnit(String type, int cost) {
        if (player.getX() != player.getCastle().getX() || player.getY() != player.getCastle().getY()) {
            System.out.println("Найм не выполнен: герой должен находиться в замке.");
            return;
        }

        if (player.getGold() < cost) {
            System.out.println("Найм не выполнен: недостаточно золота.");
            return;
        }

        Unit nu = null;
        int cX = player.getCastle().getX();
        int cY = player.getCastle().getY();

        switch (type) {
            case "Pike":
                if (player.getCastle().hasBuilding(BuildingType.GUARD_TOWER)) {
                    nu = new Pike(cX, cY);
                }
                break;
            case "Arbalet":
                if (player.getCastle().hasBuilding(BuildingType.ARCHERY_RANGE)) {
                    nu = new Arbalet(cX, cY);
                }
                break;
            case "Sword":
                if (player.getCastle().hasBuilding(BuildingType.ARMORY)) {
                    nu = new Sword(cX, cY);
                }
                break;
            case "Cavalry":
                if (player.getCastle().hasBuilding(BuildingType.ARENA)) {
                    nu = new Cavalry(cX, cY);
                }
                break;
            case "Paladin":
                if (player.getCastle().hasBuilding(BuildingType.CATHEDRAL)) {
                    nu = new Paladin(cX, cY);
                }
                break;
            case "Pudge":
                if (player.getCastle().hasBuilding(BuildingType.TAVERN)) {
                    nu = new Pudge(cX, cY);
                }
                break;
        }

        if (nu == null) {
            System.out.println("Найм не выполнен: нет нужного здания.");
            return;
        }

        player.getUnits().add(nu);
        player.setGold(player.getGold() - cost);
        map.getGrid()[nu.getX()][nu.getY()] = nu.getSymbol();
        System.out.println("Найм юнита " + nu.getName() + " выполнен.");
        map.printToConsole();
    }

    private void hireHeroFromTavern(int cost) {
        // Проверка, находится ли герой в замке
        if (player.getX() != player.getCastle().getX() || player.getY() != player.getCastle().getY()) {
            System.out.println("Найм героя не выполнен: герой должен находиться в замке.");
            return;
        }

        if (player.getGold() < cost) {
            System.out.println("Найм героя не выполнен: недостаточно золота.");
            return;
        }

        if (!player.getCastle().hasBuilding(BuildingType.TAVERN)) {
            System.out.println("Найм героя не выполнен: нужна Таверна.");
            return;
        }

        int cX = player.getCastle().getX();
        int cY = player.getCastle().getY();
        boolean anyUnitsThere = false;
        for (Unit un : player.getUnits()) {
            if (un.getX() == cX && un.getY() == cY) {
                anyUnitsThere = true;
                break;
            }
        }

        if (anyUnitsThere) {
            System.out.println("Найм героя не выполнен: в замке уже есть юниты.");
            return;
        }

        Hero newHero = new Hero("ExtraHero", "H", player.getCastle(), map, this);
        newHero.setGold(0);
        newHero.setHealth(50);
        player.setGold(player.getGold() - cost);
        System.out.println("Найм героя из Таверны выполнен.");
        map.getGrid()[cX][cY] = "H";
        map.printToConsole();
    }

    public GamePanel getPanel(){ return panel; }
    public boolean isPlayerTurn(){ return playerTurn; }

    private void endPlayerTurn() {
        if (playerTurn) {
            player.attackAllUnits();

            // Лечение от паладинов
            for (Unit unit : player.getUnits()) {
                if (unit instanceof Paladin) {
                    ((Paladin) unit).healNearbyUnits(map);
                }
            }

            playerTurn = false;
            System.out.println("Ход завершён. Ход компьютера начинается.");
            player.resetTurn();
            for (Unit u : player.getUnits()) u.resetMovement();
            computer.resetTurn();
            aiTurn();
        }
    }

    private void aiTurn() {
        computer.resetTurn();
        new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                while (computer.getMovesLeft() > 0) {
                    int oldMoves = computer.getMovesLeft();
                    computer.aiLogic();
                    publish();
                    if (computer.getMovesLeft() == oldMoves) {
                        System.out.println("Компьютер не может выполнить ход, завершаем его ход.");
                        break;
                    }
                    try {
                        Thread.sleep(300);
                    } catch (Exception e) {
                        System.out.println("Ошибка ожидания: " + e.getMessage());
                    }
                }
                return null;
            }

            protected void process(java.util.List<Void> c) {
                panel.repaint();
            }

            protected void done() {
                playerTurn = true;
                System.out.println("Ход компьютера завершён. Ход игрока.");
                checkWin();
                panel.repaint();
            }
        }.execute();
    }

    private void initGame(Castle pc, Castle cc){
        player = new Hero("Player", "H", pc, map, this);
        computer = new Hero("Comp", "AI", cc, map, this);
        computer.setGold(1000);
        map.setPlayer(player);
        map.setComputer(computer);
        map.getGrid()[player.getX()][player.getY()] = "H";
        map.getGrid()[computer.getX()][computer.getY()] = "AI";
        player.getCastle().build(BuildingType.GUARD_TOWER);
        computer.getCastle().build(BuildingType.GUARD_TOWER);
        Unit pu = new Unit("Pike", "P", 80, 15, 2,10, 1, Color.BLUE);
        pu.setX(player.getX());
        pu.setY(player.getY());
        player.getUnits().add(pu);
        Unit cu = new Unit("Pike", "P", 80, 15, 2, 10, 1, Color.BLUE);
        cu.setX(computer.getX());
        cu.setY(computer.getY());
        computer.getUnits().add(cu);
        map.getGrid()[pu.getX()][pu.getY()] = pu.getSymbol();
        map.getGrid()[cu.getX()][cu.getY()] = cu.getSymbol();
        System.out.println("Игра запущена. Начальное состояние карты:");
        map.printToConsole();
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            MainLauncher ml = new MainLauncher();
            if(args.length > 0 && args[0].equalsIgnoreCase("test")){
                ml.player.setTestMode(true);
                ml.computer.setTestMode(true);
                System.out.println("Запущен тестовый режим.");
            }
            ml.setVisible(true);
        });
    }
}
