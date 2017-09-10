package com.shopbilling.ui;

import java.awt.Color;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import com.shopbilling.dto.AppConfigurations;
import com.shopbilling.services.AppConfigServices;

public class AppConfigurationsUI extends JInternalFrame {

	private JPanel contentPane;
	private JRadioButton rb_Yes_1;
	private JRadioButton rb_No_1;
	private HashMap<String,AppConfigurations> configMap;
	

	public AppConfigurationsUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Configurations");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel configPanel = new JPanel();
		configPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		configPanel.setBounds(294, 11, 529, 574);
		contentPane.add(configPanel);
		configPanel.setLayout(null);
		setResizable(false);
		configMap = new HashMap<String, AppConfigurations>();
		createConfigMap();
		DesignGridLayout layout = new DesignGridLayout(configPanel);
		layout.labelAlignment(LabelAlignment.RIGHT);
		JButton updateButton = new JButton("Update");
		//DATA_BACKUP_ON_MAIL
		AppConfigurations config1 = configMap.get("DATA_BACKUP_ON_MAIL");
		ButtonGroup bg1 = new ButtonGroup();
		rb_Yes_1 = new JRadioButton("Yes");
		rb_No_1 = new JRadioButton("No");
		bg1.add(rb_Yes_1);
		bg1.add(rb_No_1);
		layout.row().grid(new JLabel(config1.getConfigDescription())).add(rb_Yes_1);
			
		layout.emptyRow();
		layout.row().center().add(updateButton);
	}


	private void createConfigMap() {
		for (AppConfigurations config : AppConfigServices.getAppConfigList()) {
			configMap.put(config.getConfigID(), config);
		}
	}
	
}
