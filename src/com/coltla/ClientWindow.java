package com.coltla;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientWindow extends JFrame {

    private Client client;

    private String name;
    private String address;
    private int port;

    private JPanel contentPane;

    private JTextArea history;
    private DefaultCaret caret;
    private JTextField txtMessage;

    public ClientWindow(Client client,String name, String address, int port) {
        this.client = client;
        this.name = name;
        this.address = address;
        this.port = port;

        setTitle("Cherno Chat Client");

        createWindow();
        console("Attempting a connection to " + this.address + ":" + this.port + ", user: " + this.name);
    }

    public void setTxtMessage(String text) {
        txtMessage.setText("");
    }

    public void console(String message) {
        history.append(message + "\n\r");
        history.setCaretPosition(history.getDocument().getLength());
    }

    private void createWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(880, 550);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 16, 827, 30, 7 }; // SUM = 880
        gbl_contentPane.rowHeights = new int[] { 35, 475, 40 }; // SUM = 550
        contentPane.setLayout(gbl_contentPane);

        history = new JTextArea();
        history.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        history.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(history);
        caret = (DefaultCaret) history.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        GridBagConstraints scrollConstraints = new GridBagConstraints();
        scrollConstraints.insets = new Insets(0, 0, 5, 5);
        scrollConstraints.fill = GridBagConstraints.BOTH;
        scrollConstraints.gridx = 0;
        scrollConstraints.gridy = 0;
        scrollConstraints.gridwidth = 3;
        scrollConstraints.gridheight = 2;
        scrollConstraints.weightx = 1;
        scrollConstraints.weighty = 1;
        scrollConstraints.insets = new Insets(0, 5, 0, 0);
        contentPane.add(scrollPane, scrollConstraints);

        txtMessage = new JTextField();
        txtMessage.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    client.handleSend(txtMessage.getText());
                }
            }
        });
        txtMessage.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        GridBagConstraints gbc_txtMessage = new GridBagConstraints();
        gbc_txtMessage.insets = new Insets(0, 5, 0, 5);
        gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
        gbc_txtMessage.gridx = 0;
        gbc_txtMessage.gridy = 2;
        gbc_txtMessage.gridwidth = 2;
        gbc_txtMessage.weightx = 1;
        gbc_txtMessage.weighty = 0;
        contentPane.add(txtMessage, gbc_txtMessage);
        txtMessage.setColumns(10);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                client.handleSend(txtMessage.getText());
            }
        });
        btnSend.setFont(new Font("Arial", Font.PLAIN, 16));
        GridBagConstraints gbc_btnSend = new GridBagConstraints();
        gbc_btnSend.insets = new Insets(0, 0, 0, 0);
        gbc_btnSend.gridx = 2;
        gbc_btnSend.gridy = 2;
        gbc_btnSend.weightx = 0;
        gbc_btnSend.weighty = 0;
        contentPane.add(btnSend, gbc_btnSend);

        setVisible(true);

        txtMessage.requestFocusInWindow();
    }
}
