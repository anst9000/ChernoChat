package com.coltla;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JTextArea history;
	private DefaultCaret caret;
	private JTextField txtMessage;

	private String name;
	private String address;
	private int port;

	private DatagramSocket socket;
	private InetAddress inetAddress;

	private Thread sendThread;

	public Client(String name, String address, int port) {
		setTitle("Cherno Chat Client");
		this.name = name;
		this.address = address;
		this.port = port;

		boolean connect = openConnection(this.address);
		if (!connect) {
			System.err.println("Connection failed!");
			console("Connection failed!");
			return;
		}

		createWindow();
		console("Attempting a connection to " + this.address + ":" + this.port + ", user: " + this.name);
		
		String connectionMessage = "c" + this.name;
		sendToServer(connectionMessage.getBytes());		
	}

	private boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException uhex) {
			uhex.printStackTrace();
			return false;
		} catch (SocketException soex) {
			soex.printStackTrace();
			return false;
		}

		return true;
	}

	private String receiveFromServer() {
		byte[] data = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(data, data.length);

		try {
			socket.receive(receivePacket);
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}

		String message = new String(receivePacket.getData());

		return message;
	}

	private void sendToServer(final byte[] data) {
		sendThread = new Thread("Send Thread") {
			public void run() {
				DatagramPacket sendPacket = new DatagramPacket(data, data.length, inetAddress, port);
				try {
					socket.send(sendPacket);
				} catch (IOException ioex) {
					ioex.printStackTrace();
				}
			}
		};

		sendThread.start();
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
					send(txtMessage.getText());
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
				send(txtMessage.getText());
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
	
	private void send(String message) {
		if (message.equals(""))
			return;

		message = name + ": " + message;
		console(message);
		txtMessage.setText("");

		message = "m" + message;
		sendToServer(message.getBytes());
	}

	public void console(String message) {
		history.append(message + "\n\r");
		history.setCaretPosition(history.getDocument().getLength());
	}
}
