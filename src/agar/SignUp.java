package agar;

/**
 * Created by Ali J on 7/11/2016.
 */

import agar.network.packets.SignUpPacket;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SignUp {

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
    }

    public JLabel getLblPassword() {
        return lblPassword;
    }

    public void setLblPassword(JLabel lblPassword) {
        this.lblPassword = lblPassword;
    }

    public JTextField getTextField_1() {
        return textField_1;
    }

    public void setTextField_1(JTextField textField_1) {
        this.textField_1 = textField_1;
    }

    private JFrame frame;
    private JTextField textField;
    private JLabel lblPassword;
    private JTextField textField_1;

    public SignUp() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 381, 186);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 365, 147);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblUsername.setBounds(21, 23, 91, 20);
        panel.add(lblUsername);

        textField = new JTextField();
        textField.setBounds(167, 24, 145, 20);
        panel.add(textField);
        textField.setColumns(10);

        lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblPassword.setBounds(21, 54, 68, 14);
        panel.add(lblPassword);

        textField_1 = new JTextField();
        textField_1.setBounds(167, 55, 145, 20);
        panel.add(textField_1);
        textField_1.setColumns(10);

        JButton btnConfirm = new JButton("Confirm");
        btnConfirm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SignUpPacket signUpPacket = new SignUpPacket(textField.getText(),textField.getText(),10000);
                signUpPacket.writeData(Game.socketClient);
            }
        });
        btnConfirm.setBounds(114, 95, 89, 23);
        panel.add(btnConfirm);
    }
}
