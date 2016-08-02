package agar.network;

import agar.Game;
import agar.MainMenu;
import agar.PlayerMP;
import agar.ServerList;
import agar.gameobjects.Food;
import agar.gameobjects.ObjectType;
import agar.gameobjects.PowerUp;
import agar.gameobjects.Saw;
import agar.network.packets.*;
import sun.applet.Main;
import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.net.InetAddress;


import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;


public class GameClient implements Runnable {

    private InetAddress ip;
    private int serverPort;
    private DatagramSocket socket;
    private Game game;//just incase we need anything from our game
    private boolean running;
    private Thread thread;
    private boolean ctrl1, ctrl2, ctrl3, ctrl4, ctrl5, ctrl6;

    public GameClient(Game game, String ipAddress) {
        this.game = game;
        ctrl6 = true;
        boolean connect = openConnection(ipAddress);
        if (!connect) {
            System.err.println("Connection Failed!");
        }
    }

    public void start() {
        running = true;
        thread = new Thread(this, "Game Client");
        thread.start();

    }


    public void stop() {
        running = false;
    }

    private boolean openConnection(String ipAddress) {
        try {
            socket = new DatagramSocket();//we're constructing a datagram socket
            ip = InetAddress.getByName(ipAddress);
            socket.setBroadcast(true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return false;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void findAllBroadcasts() {
        ArrayList<String> result = new ArrayList<>();
        byte[] sendData = "03".getBytes();

        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp())
                    continue;
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null)
                        continue;

                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8192);
                    socket.send(sendPacket);
                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < result.size(); i++) {
            ServerList.addtoServerList(result.get(i));
        }
    }

    @Override
    public void run() {
        while (running) {
            byte[] data = new byte[8192];//the actual array of bytes of data that we're going to be sending to and from the server
            DatagramPacket packet = new DatagramPacket(data, data.length);//the actual packet thats going to be sent to and from the server and we're just putting our data into the packets
            try {//now we have to accept the data
                socket.receive(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
                }
            })).start();

            System.out.println("SERVER > " + new String(packet.getData()));
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet;
        if (type == Packet.PacketTypes.SIGNUP || type == Packet.PacketTypes.FOOD || type == Packet.PacketTypes.POWERUP || type == Packet.PacketTypes.SAW || type == Packet.PacketTypes.HOST) {
            if (type == Packet.PacketTypes.SIGNUP) ctrl1 = true;
            if (type == Packet.PacketTypes.FOOD && game.getSpawner().getFoods().size() > 15) ctrl2 = true;
            if (type == Packet.PacketTypes.POWERUP) ctrl3 = true;
            if (type == Packet.PacketTypes.SAW) ctrl4 = true;
            if (type == Packet.PacketTypes.HOST) ctrl5 = true;
            if (ctrl1 && ctrl2 && ctrl3 && ctrl4 && ctrl5 && ctrl6) {
                game.setFlag(true);
                Dimension size = new Dimension(game.getWidth(), game.getHeight());
                game.setPreferredSize(size);
                game.initGameWindow();
                game.start();
                ctrl6 = false;

            }
        }
        switch (type) {//so what we need to do here is to construct the packet
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new LoginPacket(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((LoginPacket) packet).getUserName() + " has joined the game...");
                PlayerMP player = new PlayerMP(((LoginPacket) packet).getUserName(), (int) ((LoginPacket) packet).getX(), (int) ((LoginPacket) packet).getY(), ((LoginPacket) packet).getColor(), ((LoginPacket) packet).getImage(), ((LoginPacket) packet).getPlayerID(), address, port);
                //then we add this new player to the arraylist of players that we have in our game
                player.setSpawner(game.getSpawner());
                System.out.println(game.getSpawner());
                player.setPassword(((LoginPacket) packet).getPassword());
                if (game.socketServer == null){
                    for (int i = 0; i < game.getPlayerMPs().size(); i++)
                        if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(((LoginPacket) packet).getUserName()))
                            game.getPlayerMPs().remove(i);
                    game.getPlayerMPs().add(player);
                }
                break;
            case DISCONNECT:
                packet = new DisconnectPacket(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getUserName() + " has left the game...");
                handleDisconnectPacket((DisconnectPacket) packet);
                break;
            case MOVE:
                Game.startspawner = true;
                game.setFlag(true);
                packet = new MovePacket(data);
                handleMovePacket((MovePacket) packet);
                break;
            case DISCOVERY:
                handleDiscoveryPacket(address, port);
                break;
            case FOOD:
                packet = new FoodPacket(data);
                handleFoodyPacket((FoodPacket) packet);
                break;
            case POWERUP:
                packet = new PowerUpPacket(data);
                handlePowerUpPacket((PowerUpPacket) packet);
                if (game.getSpawner().getFoods().size() > 18)
                    game.setStartspawner(true);
                break;
            case SAW:
                packet = new SawPacket(data);
                handleSawPacket((SawPacket) packet);
                break;
            case POWERUPSTARTGODMODE:
                packet = new PowerUpStartPacketGODMODE(data);
                handlePowerUpStartPacketGODMODE((PowerUpStartPacketGODMODE) packet);
                break;
            case POWERUPSTARTJOINALL:
                packet = new PowerUpStartPacketJOINALL(data);
                handlePowerUpStartPacketJOINALL((PowerUpStartPacketJOINALL) packet);
                break;
            case POWERUPSTARTSPEEDUP:
                packet = new PowerUpStartPacketSPEEDUP(data);
                handlePowerUpStartPacketSPEEDUP((PowerUpStartPacketSPEEDUP) packet);
                break;
            case SPLIT:
                packet = new SplitPacket(data);
                handleSplitPacket((SplitPacket) packet);
                break;
            case WIN:
//                JOptionPane.showConfirmDialog(null, "you won!");
//                game.getFrame().dispose();
//                game.stop();
                break;
            case LOSE:
//                if (game.getPlayerMPs().size() > 0) {
//                    if (game.getPlayerMPs().get(0).getName().equalsIgnoreCase(game.getName())){
//                        game.getPlayerMPs().remove(0);
//                        JOptionPane.showConfirmDialog(null, "you lose!");
//                    }
//                }
                break;
            case SIGNUP:
                packet = new SignUpPacket(data);
                for (int i = 0; i < game.getPlayerMPs().size(); i++) {
                    game.getPlayerMPs().get(i).setSpawner(game.getSpawner());
                }
//                if (((SignUpPacket) packet).getConfirm() == 0){
//                    new MainMenu.LoginMenu(game);
//                }

                break;
            case HOST:
                packet = new HostPacket(data);
                handleHostPacket((HostPacket) packet);
                break;
            case CUSTOMPOWERUP:
                packet = new CustomPowerUp(data);
                handleCustomPowerUp((CustomPowerUp) packet);
                break;
        }
    }

    private void handleCustomPowerUp(CustomPowerUp packet) {
    }


    private void handleHostPacket(HostPacket packet) {
        game.setHeight(packet.getMapHeight());
        game.setWidth(packet.getMapWidth());
        MainMenu.gearSizeFactor = packet.getGearSizeFactor();
        game.setNumberoFFood(packet.getMaxFood());
        game.getSpawner().setMaxNumofFood(packet.getMaxFood());
        game.getSpawner().setWidth(packet.getMapWidth());
        game.getSpawner().setHeight(packet.getMapHeight());
        MainMenu.maxSpeed = packet.getMaxSpeed();
    }

    private void handleDisconnectPacket(DisconnectPacket packet) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(packet.getUserName())) {
                game.getPlayerMPs().get(i).setFlag(true);

            }
        }
    }

    private void handlePowerUpStartPacketGODMODE(PowerUpStartPacketGODMODE packet) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(packet.getUserName())) {
                game.getPlayerMPs().get(i).setIsgodMODE(true);
                game.getPlayerMPs().get(i).setPowerUp2(null);
                for (int j = 0; j < game.getPlayerMPs().get(i).getCircles().size(); j++)
                    game.getPlayerMPs().get(i).getCircles().get(j).setGod(true);
                game.getPlayerMPs().get(i).setPowerup2START(game.getPlayerMPs().get(i).getTick());
            }
        }
    }

    private void handlePowerUpStartPacketSPEEDUP(PowerUpStartPacketSPEEDUP packet) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(packet.getUserName())) {
                game.getPlayerMPs().get(i).setSpeedup(true);
                game.getPlayerMPs().get(i).setPowerUp3(null);
                for (int j = 0; j < game.getPlayerMPs().get(i).getCircles().size(); j++)
                    game.getPlayerMPs().get(i).getCircles().get(j).setSpeed(true);
                game.getPlayerMPs().get(i).setPowerup3START(game.getPlayerMPs().get(i).getTick());
            }
        }
    }

    private void handlePowerUpStartPacketJOINALL(PowerUpStartPacketJOINALL packet) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(packet.getUserName())) {
                game.getPlayerMPs().get(i).mergeall();
                game.getPlayerMPs().get(i).setPowerUp1(null);
                game.getPlayerMPs().get(i).setPowerup1START(game.getPlayers().get(1).getTick());
            }
        }
    }

    private void handleSplitPacket(SplitPacket packet) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(packet.getUserName())) {
                game.getPlayerMPs().get(i).addNewCircles(false);
            }
        }
    }


    private void handleDiscoveryPacket(InetAddress address, int port) {
        this.ip = address;
        this.serverPort = port;
        ServerList.addtoServerList(address.getHostAddress());
    }

    private void handleFoodyPacket(FoodPacket packet) {
        Food food = new Food(packet.getX(), packet.getY(), ObjectType.ORDINARY_FOOD, packet.getColor());
        food.setRadius(packet.getRadius());
        food.setWidth(packet.getRadius() * 2);
        food.setSurfacefromRadius();
        if (game.getSpawner().getFoods().size() < game.getSpawner().getMaxNumofFood())
            game.getSpawner().getFoods().add(food);
    }

    private void handlePowerUpPacket(PowerUpPacket packet) {
        int id = packet.getId();
        PowerUp powerUp = new PowerUp(packet.getX(), packet.getY(), getPowerUpType(id), new Color(0, 0, 0));
        game.getSpawner().setPowerUp(powerUp);
    }

    private ObjectType getPowerUpType(int type) {
        if (type == 1) {
            return ObjectType.DESTROYER;
        } else if (type == 2) {
            return ObjectType.HALFENER;
        } else if (type == 3) {
            return ObjectType.SPEED_UP;
        } else if (type == 4) {
            return ObjectType.JOIN_ALL;
        } else if (type == 5) {
            return ObjectType.GOD_MODE;
        }
        return ObjectType.GOD_MODE;
    }

    private void handleSawPacket(SawPacket packet) {
        Saw saw = new Saw(packet.getX(), packet.getY(), ObjectType.SAW, new Color(0, 0, 0));
        saw.setRadius(packet.getRadius());
        game.getSpawner().setSaw(saw);
    }

    private void handleMovePacket(MovePacket packet) {
        game.moveObject(packet.getUsername(), packet.getX(), packet.getY());
    }


    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void sendData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, serverPort);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
