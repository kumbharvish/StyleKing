package com.shopbilling.ui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class BackgroundImage extends JInternalFrame {
	/**
	 * Create the frame.
	 */
	public BackgroundImage() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		getContentPane().setLayout(null);
		 JLabel lblNewLabel = new JLabel("");
		 lblNewLabel.setIcon(new ImageIcon(BackgroundImage.class.getResource("/images/Patanjali.png")));
		 lblNewLabel.setBounds(330, 228, 430, 211);
		 getContentPane().add(lblNewLabel);
		 
	}
	
}
