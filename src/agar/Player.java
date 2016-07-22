package agar;

import agar.gameobjects.Circle;
import agar.gameobjects.MovingCircle;
import agar.gameobjects.ObjectType;
import agar.gameobjects.PowerUp;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {


    private boolean isSpeedup, isgodMODE;
    private BufferedImage image;
    private MovingCircle biggestCirle;
    private String name;
    private ArrayList<MovingCircle> circles;
    private Color playerColor;
    private long splitTime;
    private boolean splitFLAG, cutterFLAG;
    private double velX, velY;
    private boolean flag;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    private Spawner spawner;
    private int tick;
    private int playerID;
    private PowerUp powerUp1, powerUp2, powerUp3;
    private int powerup1START;
    private int powerup2START;
    private int powerup3START;

    public Player(String name, int x, int y, Color color, BufferedImage image, int playerID) {
        circles = new ArrayList<>();
        this.image = image;
        this.playerID = playerID;
        splitTime = System.nanoTime();
        splitFLAG = false;
        powerup1START = 0;
        powerup2START = 0;
        powerup3START = 0;
        tick = 0;
        cutterFLAG = false;
        MovingCircle circle = new MovingCircle(x, y, 12.0, ObjectType.PLAYER, color, spawner, playerID);
        biggestCirle = circle;
        circles.add(circle);
        playerColor = color;
        this.name = name;
    }

    public boolean update() {
        tick++;
        if (circles.size() <= 0) {
            return false;
        }
        if (splitFLAG) {
            if (!cutterFLAG) {
                if (tick - splitTime > 300) { // 5sec
                    joinTwoCircles();
                    splitTime = tick;
                }
            } else {
                if (tick - splitTime > 600) {
                    splitTime = tick;
                    joinTwoCircles();
                }
            }
            if (circles.size() == 1)
                splitFLAG = false;
        }

        if (tick - powerup2START > 300) {
            isgodMODE = false;
            for (int i = 0; i < circles.size(); i++) {
                circles.get(i).setGod(false);
            }
        }
        if (tick - powerup3START > 300) {
            isSpeedup = false;
            for (int i = 0; i < circles.size(); i++) {
                circles.get(i).setSpeed(false);
            }
        }
        if (Game.startspawner) {
            for (int i = 0; i < circles.size(); i++) {
                if (circles.get(i).getBounds().intersects(spawner.getSaw().getBounds())) {
                    addNewCircles(true);
                }
                if (circles.get(i).getBounds().intersects(spawner.getPowerUp().getBounds()) && spawner.getPowerUp() != null) {
                    if (spawner.getPowerUp().getType() == ObjectType.DESTROYER) {
                        isSpeedup = false;
                        isgodMODE = false;
                        circles.get(i).setSpeed(false);
                        circles.get(i).setGod(false);
                    } else if (spawner.getPowerUp().getType() == ObjectType.HALFENER) {
                        addNewCircles(false);
                        circles.remove(i);
                    } else if (spawner.getPowerUp().getType() == ObjectType.JOIN_ALL) {
                        powerUp1 = spawner.getPowerUp();
                    } else if (spawner.getPowerUp().getType() == ObjectType.GOD_MODE) {
                        powerUp2 = spawner.getPowerUp();
                    } else if (spawner.getPowerUp().getType() == ObjectType.SPEED_UP) {
                        powerUp3 = spawner.getPowerUp();
                    }
                    spawner.getPowerUp().setEaten(true);
                }
                boolean remove = circles.get(i).update();
                if (!remove) {
                    circles.remove(i);
                    i--;
                }
            }
        }
        if (isFlag())
            return false;

        return true;
    }


    public void render(Graphics2D g) {
        if (circles.size() <= 0) {
            return;
        }
        g.setColor(playerColor.darker());
        g.drawString(name, (int) circles.get(0).getX(), (int) circles.get(0).getY());
        if (image == null)
            for (int i = 0; i < circles.size(); i++) {
                circles.get(i).render(g);
            }
        else {
            for (int i = 0; i < circles.size(); i++) {
                g.drawImage(image, (int) circles.get(i).getX(), (int) circles.get(i).getY(), (int) circles.get(i).getWidth(), (int) circles.get(i).getWidth(), null);
            }
        }
    }

    public void joinTwoCircles() {
        if (circles.size() < 2) return;
        Circle c1 = circles.remove(circles.size() - 1);
        Circle c2 = circles.remove(circles.size() - 1);
        double surface = c1.getSurface() + c2.getSurface();
        MovingCircle c = new MovingCircle(c1.getX(), c1.getY(), Math.sqrt(surface / Math.PI), ObjectType.PLAYER, playerColor, spawner, playerID);
        c.setVelY(c1.getVelY());
        c.setVelX(c1.getVelX());
        circles.add(c);
    }

    public void addNewCircles(boolean isCutter) {
        int temp = circles.size();
        for (int i = 0; i < temp; i++) {
            if (isCutter && circles.get(i).getSurface() > spawner.getSaw().getSurface()) {
                addNewCirclest(isCutter, i);
            } else if (!isCutter) addNewCirclest(isCutter, i);
        }
    }

    public void addNewCirclest(boolean isCutter, int i) {
        Circle c = circles.get(i);
        if (c.getRadius() < Circle.MINIMUM_RADIUS) return;
        splitTime = tick;
        circles.remove(i);
        splitFLAG = true;
        cutterFLAG = isCutter;
        MovingCircle c1, c2;
        double c1x = ((c.getX() + c.getWidth()) - (c.getRadius() * (Math.sqrt(2.0) / 2.0)));
        double c1y = ((c.getY() + c.getWidth()) - (c.getRadius() * (Math.sqrt(2.0) / 2.0)));
        double c2x = (c.getX() + c.getWidth());
        double c2y = c1y;
        c1 = new MovingCircle(c1x, c1y, c.getRadius() * (Math.sqrt(2) / 2), ObjectType.PLAYER, playerColor, spawner, playerID);
        c2 = new MovingCircle(c2x, c2y, c.getRadius() * (Math.sqrt(2) / 2), ObjectType.PLAYER, playerColor, spawner, playerID);
        c1.setVelX(c.getVelX());
        c2.setVelX(c.getVelX());
        c1.setVelY(c.getVelY());
        c2.setVelY(c.getVelY());
        circles.add(c1);
        circles.add(c2);
    }


    public void mergeall() {
        MovingCircle c = null;
        double total_Surface = 0;
        for (int i = 0; i < circles.size(); i++) {
            c = circles.get(i);
            total_Surface += c.getSurface();
        }
        circles.clear();
        double velx = c.getVelX();
        double vely = c.getVelY();
        c = new MovingCircle(c.getX(), c.getY(), Math.sqrt(total_Surface / Math.PI), ObjectType.PLAYER, playerColor, spawner, playerID);
        c.setVelX(velx);
        c.setVelY(vely);
        circles.add(c);
    }

    public static String imagetoString(int[][] image, int width, int height) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                result.append(image[i][j]);
                result.append(",");
            }
        }
        return result.toString();
    }

    public static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        int[][] result = new int[image.getWidth()][image.getHeight()];
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                result[i][j] = image.getRGB(i, j);
            }
        }

        return result;
    }


    public BufferedImage getImage() {
        return image;
    }


    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public MovingCircle getBiggestCirle() {
        return biggestCirle;
    }

    public void setBiggestCirle(MovingCircle biggestCirle) {
        this.biggestCirle = biggestCirle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MovingCircle> getCircles() {
        return circles;
    }

    public void setCircles(ArrayList<MovingCircle> circles) {
        this.circles = circles;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public long getSplitTime() {
        return splitTime;
    }


    public void setSplitTime(long splitTime) {
        this.splitTime = splitTime;
    }

    public boolean isSplitFLAG() {
        return splitFLAG;
    }

    public void setSplitFLAG(boolean splitFLAG) {
        this.splitFLAG = splitFLAG;
    }

    public boolean isCutterFLAG() {
        return cutterFLAG;
    }

    public void setCutterFLAG(boolean cutterFLAG) {
        this.cutterFLAG = cutterFLAG;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public boolean isSpeedup() {
        return isSpeedup;
    }

    public void setSpeedup(boolean speedup) {
        isSpeedup = speedup;
    }

    public boolean isgodMODE() {
        return isgodMODE;
    }

    public void setIsgodMODE(boolean isgodMODE) {
        this.isgodMODE = isgodMODE;
    }

    public PowerUp getPowerUp2() {
        return powerUp2;
    }

    public void setPowerUp2(PowerUp powerUp2) {
        this.powerUp2 = powerUp2;
    }

    public PowerUp getPowerUp3() {
        return powerUp3;
    }

    public void setPowerUp3(PowerUp powerUp3) {
        this.powerUp3 = powerUp3;
    }

    public PowerUp getPowerUp1() {

        return powerUp1;
    }

    public void setPowerUp1(PowerUp powerUp1) {
        this.powerUp1 = powerUp1;
    }

    public void setSpawner(Spawner spawner) {
        this.spawner = spawner;
        for (int i = 0; i < circles.size(); i++) {
            circles.get(i).setSpawner(spawner);
        }
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public int getPowerup3START() {
        return powerup3START;
    }

    public void setPowerup3START(int powerup3START) {
        this.powerup3START = powerup3START;
    }

    public int getPowerup2START() {
        return powerup2START;
    }

    public void setPowerup2START(int powerup2START) {
        this.powerup2START = powerup2START;
    }

    public int getPowerup1START() {
        return powerup1START;
    }

    public void setPowerup1START(int powerup1START) {
        this.powerup1START = powerup1START;
    }
}
