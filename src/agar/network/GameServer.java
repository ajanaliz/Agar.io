package agar.network;


import agar.Game;
import agar.MainMenu;
import agar.PlayerMP;
import agar.gameobjects.ObjectType;
import agar.network.packets.*;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;


public class GameServer implements Runnable {

    private DatagramSocket socket;
    private Game game;//just incase we need anything from our game
    private boolean running;
    private Thread thread;
    private ArrayList<PlayerMP> connectedPlayers;//a list of all the players currently connected to the server
    private ArrayList<PlayerMP> users;


    public GameServer(Game game) {
        this.game = game;
        connectedPlayers = new ArrayList<>();
        users = new ArrayList<>();
        boolean connect = openConnection();
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


    public void initPlayerMap(String name, InetAddress address, int port) {
        for (int i = 0; i < 5; i++) {
            HostPacket hostPacket = new HostPacket(game.getWidth(), game.getHeight(), game.getNumberoFFood(), MainMenu.gearSizeFactor, 20);
            sendData(hostPacket.getData(), address, port);
        }
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            LoginPacket loginPacket = new LoginPacket(game.getPlayerMPs().get(i).getName(), game.getPlayerMPs().get(i).getPlayerColor(), game.getPlayerMPs().get(i).getPlayerID(), game.getPlayerMPs().get(i).getImage(),
                    game.getPlayerMPs().get(i).getCircles().get(0).getX(), game.getPlayerMPs().get(i).getCircles().get(0).getY(), game.getPlayerMPs().get(i).getCircles().get(0).getVelX(),
                    game.getPlayerMPs().get(i).getCircles().get(0).getVelY(), game.getPlayerMPs().get(i).getPassword());
            loginPacket.writeData(this);

        }
        SawPacket sawPacket = new SawPacket(game.getSpawner().getSaw().getX(), game.getSpawner().getSaw().getY(), game.getSpawner().getSaw().getRadius());
        sendData(sawPacket.getData(), address, port);
        for (int i = 0; i < game.getSpawner().getMaxNumofFood(); i++) {
            FoodPacket foodPacket = new FoodPacket(game.getSpawner().getFoods().get(i).getX(), game.getSpawner().getFoods().get(i).getY(), game.getSpawner().getFoods().get(i).getRadius(), game.getSpawner().getFoods().get(i).getColor());
            sendData(foodPacket.getData(), address, port);
            System.out.println(new String(foodPacket.getData()));
        }
        PowerUpPacket powerUpPacket = new PowerUpPacket(game.getSpawner().getPowerUp().getX(), game.getSpawner().getPowerUp().getY(), getPowerUpType(game.getSpawner().getPowerUp().getType()));
        sendData(powerUpPacket.getData(), address, port);
    }

    public static int getPowerUpType(ObjectType type) {
        if (type == ObjectType.DESTROYER) {
            return 1;
        } else if (type == ObjectType.HALFENER) {
            return 2;
        } else if (type == ObjectType.SPEED_UP) {
            return 3;
        } else if (type == ObjectType.JOIN_ALL) {
            return 4;
        } else if (type == ObjectType.GOD_MODE) {
            return 5;
        }
        return 1;
    }

    private boolean openConnection() {
        try {
            int port = 8192;
            socket = new DatagramSocket(port);//we're constructing a datagram socket that listens on the port we've set it to
            socket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
            parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
            String message = new String(packet.getData());
            System.out.println("CLIENT > " + message);
            if (message.trim().equalsIgnoreCase("ping")) {
                sendData("pong".getBytes(), packet.getAddress(), packet.getPort());
            }
        }
    }

    private void parsePacket(byte[] data, InetAddress address, int port) {
        String message = new String(data).trim();
        Packet.PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
        Packet packet;
        switch (type) {//so what we need to do here is to construct the packet
            default:
            case INVALID:
                break;
            case LOGIN:
                packet = new LoginPacket(data);
                PlayerMP player = new PlayerMP(((LoginPacket) packet).getUserName(), (int) ((LoginPacket) packet).getX(), (int) ((LoginPacket) packet).getY(), ((LoginPacket) packet).getColor(), ((LoginPacket) packet).getImage(), ((LoginPacket) packet).getPlayerID(), address, port);
                player.setPassword(((LoginPacket) packet).getPassword());
                player.setPort(port);
                player.setIpAddress(address);
                player.setSpawner(game.getSpawner());
                for (int i = 0; i < connectedPlayers.size(); i++) {
                    if (connectedPlayers.get(i).getName().equalsIgnoreCase(((LoginPacket) packet).getUserName())) {
                        connectedPlayers.remove(i);
                    }
                }
                connectedPlayers.add(player);
                this.addConnection(player, (LoginPacket) packet, address, port);
                break;
            case DISCONNECT:
                packet = new DisconnectPacket(data);
                System.out.println("[" + address.getHostAddress() + ":" + port + "] " + ((DisconnectPacket) packet).getUserName() + " has left...");
                this.removeConnection((DisconnectPacket) packet);
                break;
            case MOVE:
                packet = new MovePacket(data);
                System.out.println(((MovePacket) packet).getUsername() + " has moved to (" + ((MovePacket) packet).getX() + "," + ((MovePacket) packet).getY() + ")");
                this.handleMove(((MovePacket) packet));
                break;
            case DISCOVERY:
                packet = new DiscoveryPacket(data);
                sendData(packet.getData(), address, port);
                HostPacket hostPacket = new HostPacket(game.getWidth(), game.getHeight(), game.getNumberoFFood(), MainMenu.gearSizeFactor, 20);
                sendData(hostPacket.getData(), address, port);
                break;
            case FOOD:
                packet = new FoodPacket(data);
                packet.writeData(this);
                game.setStartspawner(true);
                break;
            case POWERUP:
                packet = new PowerUpPacket(data);
                packet.writeData(this);
                break;
            case POWERUPSTARTGODMODE:
                packet = new PowerUpStartPacketGODMODE(data);
                packet.writeData(this);
                break;
            case SPLIT:
                packet = new SplitPacket(data);
                packet.writeData(this);
                break;
            case SAW:
                packet = new SawPacket(data);
                packet.writeData(this);
                break;
            case POWERUPSTARTJOINALL:
                packet = new PowerUpStartPacketJOINALL(data);
                packet.writeData(this);
                break;
            case POWERUPSTARTSPEEDUP:
                packet = new PowerUpStartPacketSPEEDUP(data);
                packet.writeData(this);
                break;
            case WIN:
                packet = new WinPacket(data);
                packet.writeData(this);
                stop();
                break;
            case LOSE:
                packet = new LosePacket(data);
                packet.writeData(this);
                break;
            case SIGNUP:
                packet = new SignUpPacket(data);
                handleSignUpPacket((SignUpPacket) packet, address, port);
                break;
            case HOST:
                packet = new HostPacket(data);
                handleHostPacket((HostPacket) packet);
        }
    }

    private void handleHostPacket(HostPacket packet) {
        game.setWidth(packet.getMapWidth());
        game.setHeight(packet.getMapHeight());
        game.setNumberoFFood(packet.getMaxFood());
        MainMenu.gearSizeFactor = packet.getGearSizeFactor();
        game.getSpawner().setMaxNumofFood(packet.getMaxFood());
        game.getSpawner().setHeight(packet.getMapHeight());
        game.getSpawner().setWidth(packet.getMapWidth());
        packet.writeData(this);
    }

    private void handleSignUpPacket(SignUpPacket packet, InetAddress address, int port) {
        initPlayerMap(packet.getUserName(), address, port);
        for (int i = 0; i < users.size(); i++) {
            if (packet.getUserName().equalsIgnoreCase(users.get(i).getName())) {
                if (packet.getPassword().equalsIgnoreCase(users.get(i).getPassword())) {
                    packet.setConfirm(1);//good
                }
            }
        }
        if (packet.getConfirm() != 1) {

        }
        PlayerMP player = new PlayerMP(packet.getUserName(), 0, 0, null, null, 0, address, port);
        player.setPassword(packet.getPassword());
        users.add(player);
        for (int i = 0; i < connectedPlayers.size(); i++) {
            for (int j = 0; j < users.size(); j++) {
                if (connectedPlayers.get(i).getName().equalsIgnoreCase(users.get(j).getName())) {
                    game.getPlayerMPs().add(connectedPlayers.get(j));
                }
            }
        }

        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            LoginPacket loginPacket = new LoginPacket(game.getPlayerMPs().get(i).getName(), game.getPlayerMPs().get(i).getPlayerColor(), game.getPlayerMPs().get(i).getPlayerID(), game.getPlayerMPs().get(i).getImage()
                    , game.getPlayerMPs().get(i).getCircles().get(0).getX(), game.getPlayerMPs().get(i).getCircles().get(0).getY(),
                    game.getPlayerMPs().get(i).getCircles().get(0).getVelX(), game.getPlayerMPs().get(i).getCircles().get(0).getVelY(),
                    game.getPlayerMPs().get(i).getPassword());
            sendData(loginPacket.getData(), address, port);
        }
        sendData(packet.getData(), address, port);
    }

    public void addConnection(PlayerMP player, LoginPacket packet, InetAddress address, int port) {
        boolean flag = false;
        for (int i = 0; i < users.size(); i++) {
            if (packet.getUserName().equalsIgnoreCase(users.get(i).getName())) {
                if (packet.getPassword().equalsIgnoreCase(users.get(i).getPassword())) {
                    flag = true;
                }
            }
        }
        if (!flag) return;
        /*so first things first,when we connect to the server,we need to verify that this connection doesnt already exist in our connected players ArrayList*/
        boolean alreadyConnected = false;
        for (int i = 0; i < connectedPlayers.size(); i++) {
            if (player.getName().equalsIgnoreCase(connectedPlayers.get(i).getName())) {//we're dealing with the same player,regardless of their location,so we need to do something with that so lets go
                if (connectedPlayers.get(i).getIpAddress() == null) {
                    connectedPlayers.get(i).setIpAddress(player.getIpAddress());
                }
                if (connectedPlayers.get(i).getPort() == -1) {
                    connectedPlayers.get(i).setPort(player.getPort());
                }
                alreadyConnected = true;//this is saying that the player is already in the game and that we just need to update the player to that player just connected
            } else {/*so if the player wasnt connected we're going to send the data to them saying that we've connected*/
                sendData(packet.getData(), connectedPlayers.get(i).getIpAddress(), connectedPlayers.get(i).getPort());
            }
        }

        if (!alreadyConnected) {
            player.setPort(port);
            player.setIpAddress(address);
        }
    }

    public void removeConnection(DisconnectPacket packet) {
        /*so what we need to do right now is we need to send the data to all the clients that this players disconnected,but we dont want to send it to the player who's
        * disconnected already(which is still in the connectedplayers ArrayList)--> so we need to first remove him from the arraylist and then we'll send the data*/
//        this.connectedPlayers.remove(getPlayerMPIndex(packet.getUserName()));
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(packet.getUserName())) {
                game.getPlayerMPs().get(i).setFlag(true);
            }
        }
        packet.writeData(this);//this will call the send data to all clients function
    }

    public PlayerMP getPlayerMP(String username) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            System.out.println(game.getPlayerMPs().get(i).getName().equalsIgnoreCase(username));
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(username)) {
                return game.getPlayerMPs().get(i);
            }
        }
        return null;
    }

    public int getPlayerMPIndex(String username) {
        int index = 0;
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            if (game.getPlayerMPs().get(i).getName().equalsIgnoreCase(username)) {
                break;
            }
            index++;
        }
        return index;
    }

    public void sendData(byte[] data, InetAddress ip, int port) {
        DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDataToAllClients(byte[] data) {
        for (int i = 0; i < game.getPlayerMPs().size(); i++) {
            sendData(data, game.getPlayerMPs().get(i).getIpAddress(), game.getPlayerMPs().get(i).getPort());
        }
    }/*this is going to send the data to all the different clients and its going to call this function again*/

    private void handleMove(MovePacket packet) {/*what we need to do first is to see if this entity actually exists*/
        if (getPlayerMP(packet.getUsername()) != null) {
            //the first thing we'll need to do is to get their index first
            int index = getPlayerMPIndex(packet.getUsername());
            /*now we have to set the data we have for the entity to the ones we have just received*/
//            this.connectedPlayers.get(index).setx(packet.getX());
//            this.connectedPlayers.get(index).sety(packet.getY());
        }
        packet.writeData(this);/*this means send/write the data to all the clients*/
    }

}
