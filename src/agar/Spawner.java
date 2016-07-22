package agar;

import agar.gameobjects.Food;
import agar.gameobjects.ObjectType;
import agar.gameobjects.PowerUp;
import agar.gameobjects.Saw;
import agar.network.GameServer;
import agar.network.packets.FoodPacket;
import agar.network.packets.PowerUpPacket;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class Spawner implements Serializable {

    private ArrayList<Food> foods;
    private int maxNumofFood;
    private int width, height;
    private PowerUp powerUp;
    private Saw saw;


    public Spawner(int maxNumofFood, int width, int height) {
        this.maxNumofFood = maxNumofFood;
        this.width = width - 40;
        this.height = height - 40;
        foods = new ArrayList<>();
    }


    public void init() {
        saw = new Saw(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.SAW, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        int rand = (int) (Math.random() * 5);
        if (rand == 1) {
            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.SPEED_UP, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        } else if (rand == 2) {
            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.GOD_MODE, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        } else if (rand == 3) {
            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.HALFENER, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        } else if (rand == 4) {
            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.DESTROYER, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        } else if (rand == 0) {
            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.JOIN_ALL, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
        }
        for (int i = 0; i < maxNumofFood; i++) {
            foods.add(new Food(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.ORDINARY_FOOD, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255))));
        }
    }

    public void update() {
        if (foods.size() < maxNumofFood)
            for (int i = foods.size() - 1; i < maxNumofFood; i++) {
                if (!Game.isMultiplayer) {
                    Food food = new Food(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.ORDINARY_FOOD, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                    foods.add(food);
                } else {
                    if (Game.socketServer != null) {
                        Food food = new Food(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.ORDINARY_FOOD, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        FoodPacket packet = new FoodPacket(food.getX(), food.getY(), food.getRadius(), food.getColor());
                        packet.writeData(Game.socketServer);
                    }
                }
            }
        if (foods != null)
            for (int i = 0; i < foods.size(); i++) {
                boolean remove = foods.get(i).update();
                if (!remove) {
                    foods.remove(i);
                    i--;
                }
            }
        if (saw != null)
            saw.update();
        if (powerUp != null) {
            boolean remove = powerUp.update();
            if (!remove) {
                if (!Game.isMultiplayer) {
                    int rand = (int) (Math.random() * 5);
                    if (rand == 1) {
                        powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.SPEED_UP, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                    } else if (rand == 2) {
                        powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.GOD_MODE, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                    } else if (rand == 3) {
                        powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.HALFENER, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                    } else if (rand == 4) {
                        powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.DESTROYER, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                    } else if (rand == 0) {
                        powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.JOIN_ALL, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                    }
                } else {
                    if (Game.socketServer != null) {
                        int rand = (int) (Math.random() * 5);
                        if (rand == 1) {
                            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.SPEED_UP, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        } else if (rand == 2) {
                            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.GOD_MODE, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        } else if (rand == 3) {
                            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.HALFENER, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        } else if (rand == 4) {
                            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.DESTROYER, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        } else if (rand == 0) {
                            powerUp = new PowerUp(Math.random() * (width - 40), Math.random() * (height - 40), ObjectType.JOIN_ALL, new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                        }
                        PowerUpPacket powerUpPacket = new PowerUpPacket(powerUp.getX(), powerUp.getY(), GameServer.getPowerUpType(powerUp.getType()));
                        powerUpPacket.writeData(Game.socketServer);
                    }
                }

            }
        }
    }

    public void render(Graphics2D g) {
        if (foods != null)
            for (int i = 0; i < foods.size(); i++) {
                foods.get(i).render(g);
            }
        if (powerUp != null) {
            powerUp.render(g);
        }
        if (saw != null) {
            saw.render(g);
        }
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }


    public int getMaxNumofFood() {
        return maxNumofFood;
    }

    public void setMaxNumofFood(int maxNumofFood) {
        this.maxNumofFood = maxNumofFood;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public Saw getSaw() {
        return saw;
    }

    public void setSaw(Saw saw) {
        this.saw = saw;
    }
}
