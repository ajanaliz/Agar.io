package agar;

import agar.gameobjects.ObjectType;
import agar.network.packets.*;

import java.awt.event.*;

public class Listeners implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

    private boolean isMultiplayer;
    private Game game;

    public Listeners(Game game, boolean isMultiplayer) {
        this.game = game;
        this.isMultiplayer = isMultiplayer;
        game.addKeyListener(this);
        game.addMouseListener(this);
        game.addMouseMotionListener(this);
        game.addMouseWheelListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!Game.isMultiplayer) {
            if (game.getPlayers().get(0).getPlayerID() == 2) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(0).getCircles().get(i).setVelY(-3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(0).getCircles().get(i).setVelY(3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(0).getCircles().get(i).setVelX(3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(0).getCircles().get(i).setVelX(-3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    game.getPlayers().get(0).addNewCircles(false);
                }

                if (e.getKeyCode() == KeyEvent.VK_G && game.getPlayers().get(0).getPowerUp2() != null) {
                    if (game.getPlayers().get(0).getPowerUp2().getType() == ObjectType.GOD_MODE) {
                        game.getPlayers().get(0).setIsgodMODE(true);
                        game.getPlayers().get(0).setPowerUp2(null);
                        for (int i = 0; i < game.getPlayers().get(0).getCircles().size(); i++)
                            game.getPlayers().get(0).getCircles().get(i).setGod(true);
                        game.getPlayers().get(0).setPowerup2START(game.getPlayers().get(0).getTick());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_F && game.getPlayers().get(0).getPowerUp3() != null) {
                    if (game.getPlayers().get(0).getPowerUp3().getType() == ObjectType.SPEED_UP) {
                        game.getPlayers().get(0).setSpeedup(true);
                        game.getPlayers().get(0).setPowerUp3(null);
                        for (int i = 0; i < game.getPlayers().get(0).getCircles().size(); i++)
                            game.getPlayers().get(0).getCircles().get(i).setSpeed(true);
                        game.getPlayers().get(0).setPowerup3START(game.getPlayers().get(0).getTick());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_H && game.getPlayers().get(0).getPowerUp1() != null) {
                    if (game.getPlayers().get(0).getPowerUp1().getType() == ObjectType.JOIN_ALL) {
                        game.getPlayers().get(0).mergeall();
                        game.getPlayers().get(0).setPowerUp1(null);
                        game.getPlayers().get(0).setPowerup1START(game.getPlayers().get(1).getTick());
                    }

                }
            } else {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(1).getCircles().get(i).setVelY(-3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(1).getCircles().get(i).setVelY(3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(1).getCircles().get(i).setVelX(3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++)
                        game.getPlayers().get(1).getCircles().get(i).setVelX(-3500);
                }
                if (e.getKeyCode() == KeyEvent.VK_C) {
                    game.getPlayers().get(1).addNewCircles(false);
                }
                if (e.getKeyCode() == KeyEvent.VK_G && game.getPlayers().get(1).getPowerUp2() != null) {
                    if (game.getPlayers().get(1).getPowerUp2().getType() == ObjectType.GOD_MODE) {
                        game.getPlayers().get(1).setIsgodMODE(true);
                        game.getPlayers().get(1).setPowerUp2(null);
                        game.getPlayers().get(1).setPowerup2START(game.getPlayers().get(1).getTick());
                        for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++) {
                            game.getPlayers().get(1).getCircles().get(i).setGod(true);
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_F && game.getPlayers().get(1).getPowerUp3() != null) {
                    if (game.getPlayers().get(1).getPowerUp3().getType() == ObjectType.SPEED_UP) {
                        game.getPlayers().get(1).setSpeedup(true);
                        game.getPlayers().get(1).setPowerUp3(null);
                        game.getPlayers().get(1).setPowerup3START(game.getPlayers().get(1).getTick());
                        for (int i = 0; i < game.getPlayers().get(1).getCircles().size(); i++) {
                            game.getPlayers().get(1).getCircles().get(i).setSpeed(true);
                        }
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_H && game.getPlayers().get(1).getPowerUp1() != null) {
                    if (game.getPlayers().get(1).getPowerUp1().getType() == ObjectType.JOIN_ALL) {
                        game.getPlayers().get(1).mergeall();
                        game.getPlayers().get(1).setPowerUp1(null);
                        game.getPlayers().get(1).setPowerup1START(game.getPlayers().get(1).getTick());
                    }
                }


                if (e.getKeyCode() == KeyEvent.VK_T && game.getPlayers().get(0).getPowerUp3() != null) {
                    if (game.getPlayers().get(0).getPowerUp3().getType() == ObjectType.SPEED_UP) {
                        game.getPlayers().get(0).setSpeedup(true);
                        game.getPlayers().get(0).setPowerUp3(null);
                        for (int i = 0; i < game.getPlayers().get(0).getCircles().size(); i++)
                            game.getPlayers().get(0).getCircles().get(i).setSpeed(true);
                        game.getPlayers().get(0).setPowerup3START(game.getPlayers().get(0).getTick());
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_R && game.getPlayers().get(0).getPowerUp1() != null) {
                    if (game.getPlayers().get(0).getPowerUp1().getType() == ObjectType.JOIN_ALL) {
                        game.getPlayers().get(0).mergeall();
                        game.getPlayers().get(0).setPowerUp1(null);
                        game.getPlayers().get(0).setPowerup1START(game.getPlayers().get(1).getTick());
                    }
                }

            }


            if (e.getKeyCode() == KeyEvent.VK_S)

            {
                //save game
                game.save();
            }

            if (e.getKeyCode() == KeyEvent.VK_L)

            {
                // load game
                game.load();
            }

            if (e.getKeyCode() == KeyEvent.VK_P)

            {
                // load game
                game.setIspaused(!game.ispaused());
            }

        } else {
            if (e.getKeyCode() == KeyEvent.VK_T && game.getPlayerMPs().get(0).getPowerUp3() != null) {
                if (game.getPlayerMPs().get(0).getPowerUp3().getType() == ObjectType.SPEED_UP) {
                    PowerUpStartPacketSPEEDUP powerUpStartPacketSPEEDUP = new PowerUpStartPacketSPEEDUP(game.getUsername());
                    powerUpStartPacketSPEEDUP.writeData(Game.socketClient);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_R && game.getPlayerMPs().get(0).getPowerUp1() != null) {
                if (game.getPlayerMPs().get(0).getPowerUp1().getType() == ObjectType.JOIN_ALL) {
                    PowerUpStartPacketJOINALL powerUpStartPacketJOINALL = new PowerUpStartPacketJOINALL(game.getUsername());
                    powerUpStartPacketJOINALL.writeData(Game.socketClient);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double dx, dy, angle;
        if (!isMultiplayer) {
            if (e.getButton() == MouseEvent.BUTTON1 && game.getPlayers().get(0).getPlayerID() == 1)
                for (int i = 0; i < game.getPlayers().get(0).getCircles().size(); i++) {
                    dx = e.getX() - game.getPlayers().get(0).getCircles().get(i).getX();
                    dy = e.getY() - game.getPlayers().get(0).getCircles().get(i).getY();
                    angle = Math.atan2(dy, dx);
                    game.getPlayers().get(0).getCircles().get(i).setVelX(Math.cos(angle) * 3500);
                    game.getPlayers().get(0).getCircles().get(i).setVelY(Math.sin(angle) * 3500);
                }
            if (e.getButton() == MouseEvent.BUTTON2)
                game.getPlayers().get(0).addNewCircles(false);
            if (e.getButton() == MouseEvent.BUTTON3 && game.getPlayers().get(0).getPowerUp2() != null) {
                if (game.getPlayers().get(0).getPowerUp2().getType() == ObjectType.GOD_MODE) {
                    game.getPlayers().get(0).setIsgodMODE(true);
                    game.getPlayers().get(0).setPowerUp2(null);
                    for (int i = 0; i < game.getPlayers().get(0).getCircles().size(); i++)
                        game.getPlayers().get(0).getCircles().get(i).setGod(true);
                    game.getPlayers().get(0).setPowerup2START(game.getPlayers().get(0).getTick());
                }

            }
        } else {
            if (e.getButton() == MouseEvent.BUTTON1) {
                MovePacket movePacket = new MovePacket(game.getUsername(), e.getX(), e.getY());
                movePacket.writeData(Game.socketClient);
            } else if (e.getButton() == MouseEvent.BUTTON2) {
                SplitPacket splitPacket = new SplitPacket(game.getUsername());
                splitPacket.writeData(Game.socketClient);
            } else if (e.getButton() == MouseEvent.BUTTON3 && game.getPlayerMPs().get(0).getPowerUp2() != null) {
                PowerUpStartPacketGODMODE powerUpStartPacket = new PowerUpStartPacketGODMODE(game.getUsername());
                powerUpStartPacket.writeData(Game.socketClient);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
