package agar;


import agar.gameobjects.MovingCircle;
import agar.network.GameClient;
import agar.network.GameServer;
import agar.network.packets.DisconnectPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.*;
import java.util.ArrayList;

public class Game extends Canvas/*#4*/ implements Runnable {

    private static final long serialVersionUID = 1L;
    public static int width;
    public static int height;
    private Listeners listener;
    public static int numberoFFood;
    private Thread thread;
    public static boolean isMultiplayer = false, startspawner = false;
    private boolean running = false;
    private JFrame frame;
    private boolean ispaused;
    public static GameServer socketServer;
    public static GameClient socketClient;


    public static String username;

    private Spawner spawner;
    private static ArrayList<Player> players;
    private static ArrayList<PlayerMP> playerMPs;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Game(int width, int height, int numberoFFood, Player player1, Player player2, boolean isMultiplayer) {
        this.width = width;
        flag = false;
        this.height = height;
        this.isMultiplayer = isMultiplayer;
        ispaused = false;
        this.numberoFFood = numberoFFood;
        listener = new Listeners(this, isMultiplayer);
        spawner = new Spawner(numberoFFood, width, height);
        frame = new JFrame();
        this.ispaused = false;
        if (isMultiplayer) {
            socketClient = new GameClient(this, "localhost");
            socketClient.start();
            playerMPs = new ArrayList<>();
        } else {
            players = new ArrayList<>();
            players.add(player1);
            players.add(player2);
            spawner.init();
            startspawner = true;
            flag = true;
            player1.setSpawner(spawner);
            player2.setSpawner(spawner);
        }
    }


    public synchronized void start() {
        running = true;
        thread = new Thread(this, "Display");
        thread.start();
    }


    public void save() {
        FileOutputStream fout;
        try {
            fout = new FileOutputStream("player.sv");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(players);
            oos.close();
            fout = new FileOutputStream("spawner.sv");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(spawner);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("game saved");
    }

    public void load() {
        FileInputStream fin;
        try {
            fin = new FileInputStream("player.sv");
            ObjectInputStream ois = new ObjectInputStream(fin);
            players = (ArrayList<Player>) ois.readObject();
            fin = new FileInputStream("spawner.sv");
            ois = new ObjectInputStream(fin);
            spawner = (Spawner) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("game loaded");
    }

    public synchronized void stop() {
        running = false;
        if (isMultiplayer) {
        } else {
            if (players.get(0).getPlayerID() == 1) {
                JOptionPane.showMessageDialog(null, "Mouse PLayer Wins!");
            } else {
                JOptionPane.showMessageDialog(null, "Keyboard Player Wins!");
            }
        }

    }

    /*#3 essentially when we start our application and call the start method its going to run the run() method-->so inside this run method,is
     * going to be essentially the code that gets executed when we actually start out our gameso what we need to have in this run method
     * is our game loop(so that we dont exit out of our program)*/
    @Override
    public void run() {/*16*/
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000.0D / 60.0D;// how many nano-seconds are in one tick --> each tick is the amount of time we give to an update
        int ticks = 0;// number of updates--->for calculating UPS-->updates per second
        int frames = 0;// number of frames--->for calculating FPS-->frames per second

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;
            boolean shouldRender = true;
            while (delta >= 1) {// this will only happen 60 times a second
                // because of nsPerTick
                ticks++;
                update();
                delta--;// so we get it back to zero
                shouldRender = true;
            }
            // we're gonna
            // limit the frames that we're going to render
            if (shouldRender) {
                frames++;
                render();
            }
            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                frames = 0;
                ticks = 0;
            }
        }
    }

    public static void setIsMultiplayer(boolean isMultiplayer) {
        Game.isMultiplayer = isMultiplayer;
    }

    public static boolean isStartspawner() {
        return startspawner;
    }

    public static void setStartspawner(boolean startspawner) {
        Game.startspawner = startspawner;
    }

    public void update() {
        if (ispaused) return;

        if (isMultiplayer) {
            if (flag) {
                for (int i = 0; i < playerMPs.size(); i++) {
                    boolean remove = playerMPs.get(i).update();
                    if (!remove) {
                        System.out.println("removing");
                        if (socketServer != null) {
                            if (playerMPs.get(i).getName().equalsIgnoreCase(getName())) {
//                                LosePacket losePacket = new LosePacket(playerMPs.get(i).getName());
//                                losePacket.writeData(socketServer);
                                stop();
                                frame.dispose();
                            }
                        }
                        playerMPs.remove(i);
                        i--;
                        if (Game.socketServer != null) {
                            if (playerMPs.size() == 1) {
                                if (playerMPs.get(0).getName().equalsIgnoreCase(getName())) {
                                    JOptionPane.showMessageDialog(null, "you win!");
                                } else {
                                    JOptionPane.showMessageDialog(null, (playerMPs.get(0).getName() + " has won!"));
                                }
                                stop();
                                frame.dispose();
                            }
                        }
                    }
                }
                if (startspawner)
                    spawner.update();
            }
        } else {
            for (int i = 0; i < players.size(); i++) {
                boolean remove = players.get(i).update();
                if (!remove) {
                    players.remove(i);
                    i--;
                    stop();
                }
            }
            spawner.update();
        }
    }

    public static boolean checkforCollision(MovingCircle gameObject) {
        if (Game.isMultiplayer) {
            for (int i = 0; i < playerMPs.size(); i++) {
                for (int j = 0; j < playerMPs.get(i).getCircles().size(); j++) {
                    playerMPs.get(i).getCircles().get(j).setWidth(2 * playerMPs.get(i).getCircles().get(j).getRadius());
                    if (gameObject.getBounds().intersects(playerMPs.get(i).getCircles().get(j).getBounds()))
                        if (gameObject.getSurface() < playerMPs.get(i).getCircles().get(j).getSurface() && !playerMPs.get(i).getCircles().get(j).isGod()) {
                            playerMPs.get(i).getCircles().get(j).setSurface(playerMPs.get(i).getCircles().get(j).getSurface() + gameObject.getSurface());
                            playerMPs.get(i).getCircles().get(j).setRadiusfromSurface();
                            playerMPs.get(i).getCircles().get(j).setWidth(playerMPs.get(i).getCircles().get(j).getRadius() * 2);
                            return true;
                        }
                }
            }
        } else {
            for (int i = 0; i < players.size(); i++) {
                for (int j = 0; j < players.get(i).getCircles().size(); j++) {
                    if (gameObject.getBounds().intersects(players.get(i).getCircles().get(j).getBounds()) && gameObject.getPlayerID() != players.get(i).getCircles().get(j).getPlayerID())
                        if (gameObject.getSurface() < players.get(i).getCircles().get(j).getSurface() && !players.get(i).getCircles().get(j).isGod()) {
                            players.get(i).getCircles().get(j).setSurface(players.get(i).getCircles().get(j).getSurface() + gameObject.getSurface());
                            players.get(i).getCircles().get(j).setRadiusfromSurface();
                            players.get(i).getCircles().get(j).setWidth(players.get(i).getCircles().get(j).getRadius() * 2);
                            return true;
                        }
                }
            }
        }
        return false;
    }


    public void render() {
        BufferStrategy bs = getBufferStrategy();/*#8*/
        /*#9*/
        if (bs == null) {
            createBufferStrategy(3);/*10*/
            return;
        }/*#11*/

        Graphics g = bs.getDrawGraphics();/*#12*/
        g.setColor(new Color(200, 189, 187));
        g.fillRect(0, 0, getWidth(), getHeight());/*#15*/
        Graphics2D graphics2D = (Graphics2D) g;
        if (isMultiplayer) {
            if (flag) {
                for (int i = 0; i < playerMPs.size(); i++) {
                    playerMPs.get(i).render(graphics2D);
                }
                if (startspawner) {
                    spawner.render(graphics2D);
                }
            }
        } else {
            for (int i = 0; i < players.size(); i++) {
                players.get(i).render(graphics2D);
            }
            spawner.render(graphics2D);
        }
        /*#13*/
        g.dispose();
        bs.show();/*#14*/
    }


    public void initGameWindow() {
        frame.setResizable(false);
        frame.setTitle("Circles - by Parpar.");
        frame.add(this);
        frame.pack();/*#6*/
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                LookAndFeel previousLF = UIManager.getLookAndFeel();
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    JOptionPane.showMessageDialog(null, "have a good day!");
                    if (isMultiplayer) {
                        if (playerMPs.get(0) != null) {
                            DisconnectPacket ds = new DisconnectPacket(playerMPs.get(0).getName());
                            ds.writeData(socketClient);
                        }
                    }
                    System.exit(0);

                    UIManager.setLookAndFeel(previousLF);
                } catch (ClassNotFoundException exception) {
                    exception.printStackTrace();
                } catch (InstantiationException exception) {
                    exception.printStackTrace();
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                } catch (UnsupportedLookAndFeelException exception) {
                    exception.printStackTrace();
                }
            }

        });
    }

    public static ArrayList<PlayerMP> getPlayerMPs() {
        return playerMPs;
    }

    public static void setPlayerMPs(ArrayList<PlayerMP> playerMPs) {
        Game.playerMPs = playerMPs;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static GameClient getSocketClient() {
        return socketClient;
    }

    public static void setSocketClient(GameClient socketClient) {
        Game.socketClient = socketClient;
    }

    public static GameServer getSocketServer() {
        return socketServer;
    }

    public static void setSocketServer(GameServer socketServer) {
        Game.socketServer = socketServer;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
    }


    public void moveObject(String username, double x, double y) {
        double dx, dy, angle;
        System.out.println("here");
        for (int j = 0; j < getPlayerMPs().size(); j++) {
            if (playerMPs.get(j).getName().equalsIgnoreCase(username)) {
                for (int i = 0; i < getPlayerMPs().get(j).getCircles().size(); i++) {
                    dx = x - getPlayerMPs().get(j).getCircles().get(i).getX();
                    dy = y - getPlayerMPs().get(j).getCircles().get(i).getY();
                    angle = Math.atan2(dy, dx);
                    System.out.println("nd here");
                    getPlayerMPs().get(j).getCircles().get(i).setVelX(Math.cos(angle) * 3500);
                    getPlayerMPs().get(j).getCircles().get(i).setVelY(Math.sin(angle) * 3500);
                }
            }
        }
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public void setWidth(int width) {
        this.width = width;
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public Listeners getListener() {
        return listener;
    }


    public void setListener(Listeners listener) {
        this.listener = listener;
    }

    public int getNumberoFFood() {
        return numberoFFood;
    }

    public void setNumberoFFood(int numberoFFood) {
        this.numberoFFood = numberoFFood;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
    }

    public boolean ispaused() {
        return ispaused;
    }

    public void setIspaused(boolean ispaused) {
        this.ispaused = ispaused;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}

