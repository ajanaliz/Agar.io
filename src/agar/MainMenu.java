package agar;

import agar.network.GameServer;
import agar.network.packets.HostPacket;
import agar.network.packets.LoginPacket;
import agar.network.packets.SignUpPacket;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static agar.Game.socketClient;

public class MainMenu extends JFrame {


    public static Color p1_color;
    public static Color p2_color;
    public static MediaPlayer mediaPlayer;
    public static BufferedImage p1_picture;
    public static BufferedImage p2_picture;
    public static String p1_name;
    public static String p2_name;
    public static int maxSpeed;
    public static int maxNumberofFood;
    public static int gearSizeFactor;
    public static int mapWidth;
    public static int mapHeight;
    public static boolean isMultiplayer;
    public static boolean isKeyBoard;

    public MainMenu(boolean isMultiplayer) {
        super("Agario - by Parpar");
        if (!isMultiplayer) {
            JFrame frame = this;
            this.isMultiplayer = isMultiplayer;
            isKeyBoard = false;
            setSize(300, 510);
            ImageIcon bg = new ImageIcon(getClass().getClassLoader().getResource("images/mainmenu.jpg"));
            JPanel panel = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(bg.getImage(), 0, 0, null);
                }
            };
            JFXPanel fxpanel = new JFXPanel();
            Media media = new Media(getClass().getClassLoader().getResource("musics/01.mp3").toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.stop();
            JTextField text_x = new JTextField("1800");
            text_x.setSize(40, 30);
            text_x.setLocation(200, 70);
            JTextField text_y = new JTextField("900");
            text_y.setSize(40, 30);
            text_y.setLocation(245, 70);
            JLabel label_title = new JLabel("MyAgario");
            label_title.setSize(300, 50);
            label_title.setLocation(10, 10);
            label_title.setFont(label_title.getFont().deriveFont(30f));
            JLabel label_mapSize = new JLabel("Map size: ");
            label_mapSize.setSize(100, 30);
            label_mapSize.setLocation(10, 70);
            label_mapSize.setFont(label_mapSize.getFont().deriveFont(15f));
            JLabel label_maxNumberOfDots = new JLabel("Maximum number of dots: ");
            label_maxNumberOfDots.setSize(200, 30);
            label_maxNumberOfDots.setLocation(10, 100);
            label_maxNumberOfDots.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JTextField text_maxNumberOfDots = new JTextField("20");
            text_maxNumberOfDots.setSize(40, 30);
            text_maxNumberOfDots.setLocation(200, 100);
            JLabel label_gearSizeFactor = new JLabel("Gear size factor: ");
            label_gearSizeFactor.setSize(200, 30);
            label_gearSizeFactor.setLocation(10, 130);
            label_gearSizeFactor.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JTextField text_gearSizeFactor = new JTextField("4");
            text_gearSizeFactor.setSize(40, 30);
            text_gearSizeFactor.setLocation(200, 130);
            JLabel label_maxSpeed = new JLabel("Maximum speed: ");
            label_maxSpeed.setSize(200, 30);
            label_maxSpeed.setLocation(10, 160);
            label_maxSpeed.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JTextField text_maxSpeed = new JTextField("20");
            text_maxSpeed.setSize(40, 30);
            text_maxSpeed.setLocation(200, 160);
            JLabel label_player1Name = new JLabel("[PLAYER1] Name: ");
            label_player1Name.setSize(200, 30);
            label_player1Name.setLocation(10, 210);
            label_player1Name.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JTextField text_player1Name = new JTextField("player1");
            text_player1Name.setSize(85, 30);
            text_player1Name.setLocation(200, 210);
            JLabel label_player1Color = new JLabel("[PLAYER1] Color: ");
            label_player1Color.setSize(200, 30);
            label_player1Color.setLocation(10, 240);
            label_player1Color.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JButton btn_player1File = new JButton("browse");
            btn_player1File.setSize(85, 20);
            btn_player1File.setLocation(200, 275);
            btn_player1File.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG Images", "jpg");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            p1_picture = ImageIO.read(chooser.getSelectedFile());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            JLabel label_player1Pic = new JLabel("[PLAYER1] Picture: ");
            label_player1Pic.setSize(200, 30);
            label_player1Pic.setLocation(10, 270);
            label_player1Pic.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JButton btn_player1Color = new JButton("■■■");
            btn_player1Color.setSize(85, 20);
            btn_player1Color.setLocation(200, 245);
            btn_player1Color.setForeground(Color.RED);
            btn_player1Color.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    p1_color = JColorChooser.showDialog(frame, "Choose a color", Color.RED);
                    if (p1_color != null) {
                        btn_player1Color.setForeground(p1_color);
                    }
                }
            });
            JLabel label_player2Name = new JLabel("[PLAYER2] Name: ");
            label_player2Name.setSize(200, 30);
            label_player2Name.setLocation(10, 310);
            label_player2Name.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JTextField text_player2Name = new JTextField("player2");
            text_player2Name.setSize(85, 30);
            text_player2Name.setLocation(200, 310);
            JLabel label_player2Color = new JLabel("[PLAYER2] Color: ");
            label_player2Color.setSize(200, 30);
            label_player2Color.setLocation(10, 340);
            label_player2Color.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JButton btn_player2File = new JButton("browse");
            btn_player2File.setSize(85, 20);
            btn_player2File.setLocation(200, 375);
            btn_player2File.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG Images", "jpg");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            p2_picture = ImageIO.read(chooser.getSelectedFile());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            JLabel label_player2Pic = new JLabel("[PLAYER2] Picture: ");
            label_player2Pic.setSize(200, 30);
            label_player2Pic.setLocation(10, 370);
            label_player2Pic.setFont(label_maxNumberOfDots.getFont().deriveFont(15f));
            JButton btn_player2Color = new JButton("■■■");
            btn_player2Color.setSize(85, 20);
            btn_player2Color.setLocation(200, 345);
            btn_player2Color.setForeground(Color.BLUE);
            btn_player2Color.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    p2_color = JColorChooser.showDialog(frame, "Choose a color", Color.BLUE);
                    if (p2_color != null) {
                        btn_player2Color.setForeground(p2_color);
                    }
                }
            });
            JButton btn_play = new JButton("PLAY!");
            btn_play.setFont(btn_play.getFont().deriveFont(18F));
            btn_play.setSize(100, 40);
            btn_play.setLocation(110, 420);
            btn_play.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mapWidth = Integer.parseInt(text_x.getText());
                    mapHeight = Integer.parseInt(text_y.getText());
                    p1_color = btn_player1Color.getForeground();
                    p2_color = btn_player2Color.getForeground();
                    maxSpeed = Integer.parseInt(text_maxSpeed.getText());
                    maxNumberofFood = Integer.parseInt(text_maxNumberOfDots.getText());
                    gearSizeFactor = Integer.parseInt(text_gearSizeFactor.getText());
                    p1_name = text_player1Name.getText();
                    p2_name = text_player2Name.getText();
                    Player player1;
                    Player player2;
                    player1 = new Player(p1_name, (int) (Math.random() * (mapWidth - 50)), (int) (Math.random() * (mapHeight - 50)), p1_color, p1_picture, 1);
                    player2 = new Player(p2_name, (int) (Math.random() * (mapWidth - 50)), (int) (Math.random() * (mapHeight - 50)), p2_color, p2_picture, 2);
                    Game game = new Game(mapWidth, mapHeight, maxNumberofFood, player1, player2, isMultiplayer);
                    game.setPreferredSize(new Dimension(mapWidth, mapHeight));
                    game.initGameWindow();
                    game.start();
                    dispose();
                }
            });
            ImageIcon icon_music_stop = new ImageIcon(getClass().getClassLoader().getResource("images/stop.png"));
            ImageIcon icon_music_play = new ImageIcon(getClass().getClassLoader().getResource("images/play.png"));
            JButton btn_music = new JButton(icon_music_stop);
            btn_music.setSize(40, 40);
            btn_music.setLocation(60, 420);
            btn_music.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                        btn_music.setIcon(icon_music_play);
                    } else {
                        mediaPlayer.play();
                        btn_music.setIcon(icon_music_stop);
                    }
                }
            });
            panel.add(btn_play);
            panel.add(btn_music);
            panel.add(label_player1Name);
            panel.add(text_player1Name);
            panel.add(label_player1Color);
            panel.add(label_player1Pic);
            panel.add(btn_player1Color);
            panel.add(btn_player1File);
            panel.add(label_player2Name);
            panel.add(text_player2Name);
            panel.add(label_player2Color);
            panel.add(label_player2Pic);
            panel.add(btn_player2Color);
            panel.add(btn_player2File);
            panel.add(label_title);
            panel.add(text_x);
            panel.add(text_y);
            panel.add(label_mapSize);
            panel.add(label_maxSpeed);
            panel.add(text_maxSpeed);
            panel.add(label_maxNumberOfDots);
            panel.add(text_maxNumberOfDots);
            panel.add(text_gearSizeFactor);
            panel.add(label_gearSizeFactor);
            add(panel);

            panel.setVisible(true);
            frame.setVisible(true);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setResizable(false);
            setVisible(true);
        } else {

        }

    }


    public static void main(String[] args) {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                try {
                    UIManager.setLookAndFeel(info.getClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        boolean isMultiplayer = false;
        if (JOptionPane.showConfirmDialog(null, "Do you want to play online?") == 0) {
            isMultiplayer = true;
            Game game = new Game(mapWidth, mapHeight, maxNumberofFood, null, null, isMultiplayer);
            if (JOptionPane.showConfirmDialog(null, "Do you want to host?") == 0) {
                Game.socketServer = new GameServer(game);
                Game.socketServer.start();
                HostMenu hostMenu = new HostMenu(game);
            }else {
                ServerList serverList = new ServerList(game);
            }
        } else {
            new MainMenu(isMultiplayer);
        }
    }


    public static class SignUpMenu {

        private JFrame frame;
        private JTextField txtUsername;
        private JTextField txtPassword;
        private MediaPlayer mediaPlayer;
        private BufferedImage p1_picture;
        private JTextField textField;

        private Color p1_color;

        public SignUpMenu() {
            initialize();
            p1_color = Color.RED;
        }


        private void initialize() {
            JFXPanel fxpanel = new JFXPanel();
            Media media = new Media(getClass().getClassLoader().getResource("musics/01.mp3").toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.stop();
            frame = new JFrame();
            frame.setBounds(100, 100, 307, 379);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(null);

            JLabel lblMyAgario = new JLabel("My Agario");
            lblMyAgario.setFont(new Font("Tahoma", Font.PLAIN, 18));
            lblMyAgario.setBounds(21, 21, 98, 42);
            frame.getContentPane().add(lblMyAgario);

            JLabel lblNewLabel = new JLabel("Player Username:");
            lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblNewLabel.setBounds(21, 74, 110, 23);
            frame.getContentPane().add(lblNewLabel);

            JLabel lblPlayerPassword = new JLabel("Player Password:");
            lblPlayerPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPlayerPassword.setBounds(21, 135, 110, 23);
            frame.getContentPane().add(lblPlayerPassword);

            JLabel lblPlayerColor = new JLabel("Player Color:");
            lblPlayerColor.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPlayerColor.setBounds(21, 169, 110, 23);
            frame.getContentPane().add(lblPlayerColor);

            JLabel lblPlayerPicture = new JLabel("Player Picture:");
            lblPlayerPicture.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPlayerPicture.setBounds(21, 203, 110, 23);
            frame.getContentPane().add(lblPlayerPicture);

            JButton btnPlay = new JButton("Done");
            btnPlay.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LoginPacket loginPacket = new LoginPacket(txtUsername.getText(), p1_color, 1, p1_picture, (int) (Math.random() * (Game.width - 50)), (int) (Math.random() * (Game.height - 50)), 0, 0, txtPassword.getText());
                    Game.username = txtUsername.getText();
                    loginPacket.writeData(socketClient);
                    if (textField.getText().equalsIgnoreCase("yes")){
                        isKeyBoard = true;
                    }
                    frame.dispose();
                }
            });
            btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 13));
            btnPlay.setBounds(144, 279, 98, 35);
            frame.getContentPane().add(btnPlay);

            JButton btnMusic = new JButton("Music");
            btnMusic.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer.play();
                    }
                }
            });
            btnMusic.setFont(new Font("Tahoma", Font.PLAIN, 13));
            btnMusic.setBounds(21, 285, 89, 23);
            frame.getContentPane().add(btnMusic);

            JButton btnBrowse = new JButton("browse");
            btnBrowse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG Images", "jpg");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            p1_picture = ImageIO.read(chooser.getSelectedFile());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            btnBrowse.setBounds(175, 204, 89, 23);
            frame.getContentPane().add(btnBrowse);

            JButton btnNewButton = new JButton("■■■");
            btnNewButton.setForeground(Color.RED);
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    p1_color = JColorChooser.showDialog(frame, "Choose a color", Color.RED);
                    if (p1_color != null) {
                        btnNewButton.setForeground(p1_color);
                    }
                }
            });
            btnNewButton.setBounds(175, 170, 89, 23);
            frame.getContentPane().add(btnNewButton);

            txtUsername = new JTextField();
            txtUsername.setText("Username");
            txtUsername.setBounds(165, 69, 116, 35);
            frame.getContentPane().add(txtUsername);
            txtUsername.setColumns(10);

            txtPassword = new JTextField();
            txtPassword.setText("Password");
            txtPassword.setColumns(10);
            txtPassword.setBounds(165, 123, 116, 35);
            frame.getContentPane().add(txtPassword);

            JLabel lblPlayWithKeyboard = new JLabel("Play with KeyBoard?");
            lblPlayWithKeyboard.setBounds(21, 237, 110, 28);
            frame.getContentPane().add(lblPlayWithKeyboard);

            textField = new JTextField();
            textField.setBounds(145, 238, 97, 30);
            frame.getContentPane().add(textField);
            textField.setColumns(10);

            frame.setVisible(true);
        }

    }

    public static class NoSignUpMenu {

        private JFrame frame;
        private JTextField txtUsername;
        private MediaPlayer mediaPlayer;
        private BufferedImage p1_picture;
        private JTextField textField;

        private Color p1_color;

        public NoSignUpMenu() {
            initialize();
            p1_color = Color.RED;
        }


        private void initialize() {
            JFXPanel fxpanel = new JFXPanel();
            Media media = new Media(getClass().getClassLoader().getResource("musics/01.mp3").toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.stop();
            frame = new JFrame();
            frame.setBounds(100, 100, 307, 379);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().setLayout(null);

            JLabel lblMyAgario = new JLabel("My Agario");
            lblMyAgario.setFont(new Font("Tahoma", Font.PLAIN, 18));
            lblMyAgario.setBounds(21, 21, 98, 42);
            frame.getContentPane().add(lblMyAgario);

            JLabel lblNewLabel = new JLabel("Player Username:");
            lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblNewLabel.setBounds(21, 74, 110, 23);
            frame.getContentPane().add(lblNewLabel);

            JLabel lblPlayerPassword = new JLabel("Player Password:");
            lblPlayerPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPlayerPassword.setBounds(21, 135, 110, 23);

            JLabel lblPlayerColor = new JLabel("Player Color:");
            lblPlayerColor.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPlayerColor.setBounds(21, 169, 110, 23);
            frame.getContentPane().add(lblPlayerColor);

            JLabel lblPlayerPicture = new JLabel("Player Picture:");
            lblPlayerPicture.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPlayerPicture.setBounds(21, 203, 110, 23);
            frame.getContentPane().add(lblPlayerPicture);

            JButton btnPlay = new JButton("Done");
            btnPlay.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    LoginPacket loginPacket = new LoginPacket(txtUsername.getText(), p1_color, 1, p1_picture, (int) (Math.random() * (Game.width - 50)), (int) (Math.random() * (Game.height - 50)), 0, 0, "XXX");
                    Game.username = txtUsername.getText();
                    loginPacket.writeData(socketClient);
                    SignUpPacket signUpPacket = new SignUpPacket(txtUsername.getText(), "XXX", 10000);
                    signUpPacket.writeData(socketClient);
                    if (textField.getText().trim().equalsIgnoreCase("yes")){
                        isKeyBoard = true;
                    }else {
                        isKeyBoard = false;
                    }
                    frame.dispose();
                }
            });
            btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 13));
            btnPlay.setBounds(144, 279, 98, 35);
            frame.getContentPane().add(btnPlay);

            JButton btnMusic = new JButton("Music");
            btnMusic.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer.play();
                    }
                }
            });
            btnMusic.setFont(new Font("Tahoma", Font.PLAIN, 13));
            btnMusic.setBounds(21, 285, 89, 23);
            frame.getContentPane().add(btnMusic);

            JButton btnBrowse = new JButton("browse");
            btnBrowse.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG Images", "jpg");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        try {
                            p1_picture = ImageIO.read(chooser.getSelectedFile());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
            btnBrowse.setBounds(175, 204, 89, 23);
            frame.getContentPane().add(btnBrowse);

            JButton btnNewButton = new JButton("■■■");
            btnNewButton.setForeground(Color.RED);
            btnNewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    p1_color = JColorChooser.showDialog(frame, "Choose a color", Color.RED);
                    if (p1_color != null) {
                        btnNewButton.setForeground(p1_color);
                    }
                }
            });
            btnNewButton.setBounds(175, 170, 89, 23);
            frame.getContentPane().add(btnNewButton);

            txtUsername = new JTextField();
            txtUsername.setText("Username");
            txtUsername.setBounds(165, 69, 116, 35);
            frame.getContentPane().add(txtUsername);
            txtUsername.setColumns(10);


            JLabel lblPlayWithKeyboard = new JLabel("Play with KeyBoard?");
            lblPlayWithKeyboard.setBounds(21, 237, 110, 28);
            frame.getContentPane().add(lblPlayWithKeyboard);

            textField = new JTextField();
            textField.setBounds(145, 238, 97, 30);
            frame.getContentPane().add(textField);
            textField.setColumns(10);

            frame.setVisible(true);
        }

    }

    public static class LoginMenu {

        private JFrame frmMyAgario;
        private JTextField txtPassword;
        private JTextField txtUsername;
        private MediaPlayer mediaPlayer;
        private Game game;

        public LoginMenu(Game game) {
            initialize();
            this.game = game;
        }

        private void initialize() {
            JFXPanel fxpanel = new JFXPanel();
            Media media = new Media(getClass().getClassLoader().getResource("musics/01.mp3").toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.stop();
            final SignUpMenu[] signUpMenu = new SignUpMenu[1];
            frmMyAgario = new JFrame();
            frmMyAgario.setTitle("My Agario");
            frmMyAgario.setBounds(100, 100, 298, 294);
            frmMyAgario.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frmMyAgario.getContentPane().setLayout(null);

            JPanel panel = new JPanel();
            panel.setBounds(0, 0, 282, 255);
            frmMyAgario.getContentPane().add(panel);
            panel.setLayout(null);

            JButton btnSignUp = new JButton("Sign up");
            btnSignUp.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    signUpMenu[0] = new SignUpMenu();
                }
            });
            btnSignUp.setBounds(21, 179, 89, 23);
            panel.add(btnSignUp);

            JButton btnLogin = new JButton("Login");
            btnLogin.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    SignUpPacket signUpPacket = new SignUpPacket(txtUsername.getText(), txtPassword.getText(), 10000);
                    signUpPacket.writeData(socketClient);
                    Game.username = txtUsername.getText();
                    frmMyAgario.dispose();
                }
            });
            btnLogin.setBounds(153, 179, 89, 23);
            panel.add(btnLogin);

            JLabel lblUsername = new JLabel("Username:");
            lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblUsername.setBounds(21, 50, 89, 23);
            panel.add(lblUsername);

            JLabel lblPassword = new JLabel("Password:");
            lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblPassword.setBounds(21, 105, 89, 23);
            panel.add(lblPassword);

            txtPassword = new JTextField();
            txtPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
            txtPassword.setText("password");
            txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
            txtPassword.setBounds(138, 102, 118, 31);
            panel.add(txtPassword);
            txtPassword.setColumns(10);

            txtUsername = new JTextField();
            txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
            txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
            txtUsername.setText("username");
            txtUsername.setColumns(10);
            txtUsername.setBounds(138, 47, 118, 31);
            panel.add(txtUsername);

            JButton btnLoginAsGuest = new JButton("Login as Guest");
            btnLoginAsGuest.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new NoSignUpMenu();
                    frmMyAgario.dispose();
                }
            });
            btnLoginAsGuest.setBounds(76, 221, 118, 23);
            panel.add(btnLoginAsGuest);

            JButton btnMusic = new JButton("Music");
            btnMusic.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                    } else {
                        mediaPlayer.play();
                    }
                }
            });
            btnMusic.setBounds(21, 139, 89, 23);
            panel.add(btnMusic);
            frmMyAgario.setVisible(true);
        }

    }


    public static class HostMenu {

        private JFrame frmServerMenu;
        private JTextField textField;
        private JTextField textField_1;
        private JTextField textField_2;
        private JTextField textField_3;
        private JTextField textField_4;
        private Game game;

        public HostMenu(Game game) {
            initialize();
            this.game = game;
        }


        private void initialize() {
            frmServerMenu = new JFrame();
            frmServerMenu.setTitle("Server Menu");
            frmServerMenu.setBounds(100, 100, 321, 383);
            frmServerMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frmServerMenu.getContentPane().setLayout(null);

            JLabel lblNewLabel = new JLabel("Map Size:");
            lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblNewLabel.setBounds(22, 40, 76, 21);
            frmServerMenu.getContentPane().add(lblNewLabel);

            textField = new JTextField();
            textField.setText("1800");
            textField.setBounds(89, 34, 91, 34);
            frmServerMenu.getContentPane().add(textField);
            textField.setColumns(10);

            textField_1 = new JTextField();
            textField_1.setText("800");
            textField_1.setColumns(10);
            textField_1.setBounds(204, 34, 91, 34);
            frmServerMenu.getContentPane().add(textField_1);

            JLabel lblMaxFood = new JLabel("Max Food");
            lblMaxFood.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblMaxFood.setBounds(22, 127, 76, 21);
            frmServerMenu.getContentPane().add(lblMaxFood);

            JLabel lblGearSizeScale = new JLabel("Gear Size Scale:");
            lblGearSizeScale.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblGearSizeScale.setBounds(10, 169, 120, 21);
            frmServerMenu.getContentPane().add(lblGearSizeScale);

            JLabel lblMaxSpeed = new JLabel("Max Speed:");
            lblMaxSpeed.setFont(new Font("Tahoma", Font.PLAIN, 13));
            lblMaxSpeed.setBounds(22, 216, 76, 21);
            frmServerMenu.getContentPane().add(lblMaxSpeed);

            textField_2 = new JTextField();
            textField_2.setText("20");
            textField_2.setColumns(10);
            textField_2.setBounds(135, 121, 91, 34);
            frmServerMenu.getContentPane().add(textField_2);

            textField_3 = new JTextField();
            textField_3.setText("4");
            textField_3.setColumns(10);
            textField_3.setBounds(145, 163, 91, 34);
            frmServerMenu.getContentPane().add(textField_3);

            textField_4 = new JTextField();
            textField_4.setText("20");
            textField_4.setColumns(10);
            textField_4.setBounds(135, 210, 91, 34);
            frmServerMenu.getContentPane().add(textField_4);

            JButton btnStartServer = new JButton("Start Server");
            btnStartServer.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    HostPacket hostPacket = new HostPacket(Integer.parseInt(textField.getText()), Integer.parseInt(textField_1.getText()), Integer.parseInt(textField_2.getText()), Integer.parseInt(textField_3.getText()), Integer.parseInt(textField_4.getText()));
                    hostPacket.writeData(Game.socketServer);
                    Game.width = Integer.parseInt(textField.getText());
                    Game.height = Integer.parseInt(textField_1.getText());
                    Game.numberoFFood = Integer.parseInt(textField_2.getText());
                    game.getSpawner().setHeight(Game.height);
                    game.getSpawner().setWidth(Game.width);
                    game.getSpawner().setMaxNumofFood(game.getNumberoFFood());
                    MainMenu.gearSizeFactor = Integer.parseInt(textField_3.getText());
                    game.getSpawner().init();
                    frmServerMenu.dispose();
                    ServerList serverList = new ServerList(game);
                }
            });
            btnStartServer.setBounds(91, 285, 108, 23);
            frmServerMenu.getContentPane().add(btnStartServer);
            frmServerMenu.setVisible(true);
        }

    }

}
