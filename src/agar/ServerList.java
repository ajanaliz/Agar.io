package agar;

import agar.network.packets.LoginPacket;
import agar.network.packets.SignUpPacket;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Ali J on 7/10/2016.
 */
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import static agar.Game.socketClient;

public class ServerList {

    private JFrame frame;
    private final JPanel panel = new JPanel();
    public static ArrayList<String> serverList = new ArrayList<>();
    public static InetAddress serverAddress;
    private Game game;

    public ServerList(Game game) {
        this.game = game;
        socketClient.findAllBroadcasts();
        initialize();
    }


    public static void addtoServerList(String s) {
        serverList.add(s);
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JPanel getPanel() {
        return panel;
    }

    public static ArrayList<String> getServerList() {
        return serverList;
    }

    public static void setServerList(ArrayList<String> serverList) {
        ServerList.serverList = serverList;
    }

    public static InetAddress getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(InetAddress serverAddress) {
        ServerList.serverAddress = serverAddress;
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 172);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        panel.setBounds(0, 0, 434, 133);
        frame.getContentPane().add(panel);
        panel.setLayout(null);
        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(192, 39, 197, 20);
        panel.add(comboBox);
        frame.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                comboBox.removeAllItems();
                for (String s : serverList) {
                    comboBox.addItem(s);
                }
            }
        });

        for (String s : serverList) {
            comboBox.addItem(s);
        }
        JLabel lblSelectServer = new JLabel("Select Server:");
        lblSelectServer.setBounds(47, 42, 74, 14);
        panel.add(lblSelectServer);

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {
                    serverAddress = InetAddress.getByName((String) comboBox.getSelectedItem());
                    socketClient.setIp(serverAddress);
                    frame.dispose();
                    Game.socketClient.setIp(getServerAddress());
                    Game.socketClient.setServerPort(8192);
                    MainMenu.LoginMenu loginMenu = new MainMenu.LoginMenu(game);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        btnConfirm.setBounds(146, 88, 89, 23);
        panel.add(btnConfirm);
        frame.setVisible(true);
    }
}
