package agar.network.packets;


import agar.network.GameClient;
import agar.network.GameServer;

/**
 * Created by Ali J on 5/24/2015.
 */
public abstract class Packet {

    public static enum PacketTypes {
        INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), DISCOVERY(03), FOOD(04), SAW(05), POWERUP(06), SPLIT(07), POWERUPSTARTGODMODE(55), POWERUPSTARTSPEEDUP(66), POWERUPSTARTJOINALL(77),
        WIN(12), LOSE(13), SIGNUP(14), HOST(15);

        private int packetID;

        private PacketTypes(int packetID) {
            this.packetID = packetID;
        }

        public int getPacketID() {
            return packetID;
        }
    }


    public byte packetID;

    public Packet(int packetID) {
        this.packetID = (byte) packetID;
    }


    public abstract byte[] getData();/*what this function is going to do is its going to be the actual byte array that we're sending back and forth from the client*/

    public abstract void writeData(GameClient client);

    public abstract void writeData(GameServer server);
    /*the difference between these two functions is when we send something to gameclient,its going to send whatever data from this packet to the server
    * from this client and when we send it to the server,its going to send it to all the clients within this server*/


    public String readData(byte[] data) {
        String message = new String(data).trim();
        return message.substring(2);/*the first two characters are going to be for the id*/
    }

    public static PacketTypes lookupPacket(int id) {//i made a static function for getting the type of packets-->for this we're going to send in the id of the packet which is an int,then we use that to check what packet we're referring to
        for (PacketTypes p : PacketTypes.values()) {//loop through all the different packet types in our enum
            if (p.getPacketID() == id) {
                return p;
            }
        }
        return PacketTypes.INVALID;
    }

    public static PacketTypes lookupPacket(String packetID) {
        try {
            return lookupPacket(Integer.parseInt(packetID));
        } catch (NumberFormatException e) {//this will just verify that the actual packet is an actually valid packet(for security reasons...for example if someones trying to get into our system)
            return PacketTypes.INVALID;
        }
    }
}
