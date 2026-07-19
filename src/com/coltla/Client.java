package com.coltla;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import java.awt.GridBagConstraints;

public class Client extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private String name;
	private String address;
	private int port;
	
	public Client(String name, String address, int port) {
		setTitle("Cherno Chat Client");
		this.name = name;
		this.address = address;
		this.port = port;
		
		createWindow();
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
		gbl_contentPane.columnWidths = new int[] { 16, 857, 7 }; 	// SUM = 880
		gbl_contentPane.rowHeights = new int[] { 35, 475, 40 }; 		// SUM = 550
		contentPane.setLayout(gbl_contentPane);
		
		JTextArea history = new JTextArea();
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.fill = GridBagConstraints.BOTH;
		gbc_textArea.gridx = 1;
		gbc_textArea.gridy = 1;
		contentPane.add(history, gbc_textArea);
		setVisible(true);
	}

}
