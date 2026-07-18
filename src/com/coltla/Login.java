package com.coltla;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JButton;

public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JLabel lblIpAddress;
	private JTextField txtAddress;
	private JLabel lblPort;
	private JTextField txtPort;
	private JLabel lblExampleaddress;
	private JLabel lblExampleport;

	/**
	 * Create the frame.
	 */
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		setResizable(false);
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 400);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtName.setBounds(59, 50, 165, 28);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblName.setBounds(119, 25, 45, 16);
		contentPane.add(lblName);
		
		lblIpAddress = new JLabel("IP Address:");
		lblIpAddress.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblIpAddress.setBounds(101, 102, 81, 16);
		contentPane.add(lblIpAddress);
		
		txtAddress = new JTextField();
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtAddress.setColumns(10);
		txtAddress.setBounds(59, 129, 165, 28);
		contentPane.add(txtAddress);
		
		lblPort = new JLabel("Port:");
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPort.setBounds(124, 184, 36, 16);
		contentPane.add(lblPort);
		
		txtPort = new JTextField();
		txtPort.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtPort.setColumns(10);
		txtPort.setBounds(59, 209, 165, 28);
		contentPane.add(txtPort);
		
		lblExampleaddress = new JLabel("(eg. 192.168.0.2)");
		lblExampleaddress.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExampleaddress.setBounds(89, 156, 105, 16);
		contentPane.add(lblExampleaddress);
		
		lblExampleport = new JLabel("(eg. 8192)");
		lblExampleport.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExampleport.setBounds(106, 237, 72, 16);
		contentPane.add(lblExampleport);
		
		JButton btnNewButton = new JButton("Login");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton.setBounds(97, 312, 89, 23);
		contentPane.add(btnNewButton);

	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
