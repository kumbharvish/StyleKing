package com.shopbilling.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.shopbilling.services.DBBackupService;
import com.shopbilling.utils.PDFUtils;

public class RestoreDataBase extends JFrame {

	private JPanel contentPane;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RestoreDataBase frame = new RestoreDataBase();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public RestoreDataBase() {
		setTitle("Data Backup");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 824, 369);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblChooseDataBackup = new JLabel("Choose Data Backup File :");
		lblChooseDataBackup.setHorizontalAlignment(SwingConstants.RIGHT);
		lblChooseDataBackup.setBounds(10, 95, 147, 25);
		contentPane.add(lblChooseDataBackup);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField.setBounds(167, 95, 418, 25);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Restore Database");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(338, 34, 132, 25);
		contentPane.add(lblNewLabel);
		
		JButton btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(getContentPane());
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    textField.setText(selectedFile.getAbsolutePath());
				    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				}
			}
		});
		btnBrowse.setBounds(595, 95, 89, 25);
		contentPane.add(btnBrowse);
		
		JButton btnRestore = new JButton("Restore");
		btnRestore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(PDFUtils.isMandatoryEntered(textField)){
					DBBackupService.importDBDump(textField.getText());
				}else{
					JOptionPane.showMessageDialog(null, "Please choose Backup file!", "Warning", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		btnRestore.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRestore.setBounds(353, 181, 89, 25);
		contentPane.add(btnRestore);
		
	}
}
