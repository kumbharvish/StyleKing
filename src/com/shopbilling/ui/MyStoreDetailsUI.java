package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.MyStoreDetails;
import com.shopbilling.services.MyStoreServices;
import com.shopbilling.utils.PDFUtils;

public class MyStoreDetailsUI extends JInternalFrame {
	private JTextField tf_StoreName;
	private JTextField tf_StoreId;
	private JTextField tf_Address;
	private JTextField tf_Address2;
	private JTextField tf_City;
	private JTextField tf_District;
	private JTextField tf_State;
	private JTextField tf_Phone;
	private JTextField tf_CstNo;
	private JTextField tf_PANNo;
	private JTextField tf_VATNo;
	private JTextField tf_ElectricityNo;
	private JTextField tf_OwnerName;
	private JTextField tf_MobileNo;
	private JPanel panel_2;
	private JPanel panel_1;
	private JTextField tf_imagePath;

	public MyStoreDetailsUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("My Store Details");
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(194, 25, 703, 41);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblMyStoreDetails = new JLabel("My Store Details");
		lblMyStoreDetails.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblMyStoreDetails.setHorizontalAlignment(SwingConstants.CENTER);
		lblMyStoreDetails.setBounds(180, 5, 342, 30);
		panel.add(lblMyStoreDetails);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(76, 84, 467, 515);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblStoreName = new JLabel("Store Name *: ");
		lblStoreName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblStoreName.setBounds(10, 32, 121, 25);
		panel_1.add(lblStoreName);
		tf_StoreId = new JTextField();
		
		tf_StoreName = new JTextField();
		tf_StoreName.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_StoreName.setBounds(141, 32, 302, 25);
		panel_1.add(tf_StoreName);
		tf_StoreName.setColumns(10);
		tf_StoreName.setDisabledTextColor(Color.black);
		
		JLabel lblAddress = new JLabel("Address : ");
		lblAddress.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress.setBounds(10, 104, 121, 25);
		panel_1.add(lblAddress);
		
		tf_Address = new JTextField();
		tf_Address.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_Address.setColumns(10);
		tf_Address.setBounds(141, 104, 302, 25);
		tf_Address.setDisabledTextColor(Color.black);
		panel_1.add(tf_Address);
		
		JLabel lblAddress_1 = new JLabel("Address 2 : ");
		lblAddress_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAddress_1.setBounds(10, 176, 121, 25);
		panel_1.add(lblAddress_1);
		
		tf_Address2 = new JTextField();
		tf_Address2.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_Address2.setColumns(10);
		tf_Address2.setBounds(141, 176, 302, 25);
		tf_Address2.setDisabledTextColor(Color.black);
		panel_1.add(tf_Address2);
		
		JLabel lblTaluka = new JLabel("City *: ");
		lblTaluka.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTaluka.setBounds(10, 248, 121, 25);
		panel_1.add(lblTaluka);
		
		tf_City = new JTextField();
		tf_City.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_City.setColumns(10);
		tf_City.setBounds(141, 248, 302, 25);
		tf_City.setDisabledTextColor(Color.black);
		panel_1.add(tf_City);
		
		JLabel lblDistrict = new JLabel("District : ");
		lblDistrict.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDistrict.setBounds(10, 320, 121, 25);
		panel_1.add(lblDistrict);
		
		tf_District = new JTextField();
		tf_District.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_District.setColumns(10);
		tf_District.setBounds(141, 320, 302, 25);
		tf_District.setDisabledTextColor(Color.black);
		panel_1.add(tf_District);
		
		JLabel lblState = new JLabel("State : ");
		lblState.setHorizontalAlignment(SwingConstants.RIGHT);
		lblState.setBounds(10, 392, 121, 25);
		panel_1.add(lblState);
		
		tf_State = new JTextField();
		tf_State.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_State.setColumns(10);
		tf_State.setBounds(141, 392, 302, 25);
		tf_State.setDisabledTextColor(Color.black);
		panel_1.add(tf_State);
		
		tf_Phone = new JTextField();
		tf_Phone.setColumns(10);
		tf_Phone.setBounds(141, 464, 302, 25);
		tf_Phone.setDisabledTextColor(Color.black);
		tf_Phone.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_1.add(tf_Phone);
		
		JLabel lblPhone = new JLabel("Phone : ");
		lblPhone.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPhone.setBounds(10, 464, 121, 25);
		panel_1.add(lblPhone);
		
		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(563, 84, 467, 463);
		getContentPane().add(panel_2);
		
		JLabel lblCstNumber = new JLabel("CST Number : ");
		lblCstNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCstNumber.setBounds(10, 32, 121, 25);
		panel_2.add(lblCstNumber);
		
		tf_CstNo = new JTextField();
		tf_CstNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_CstNo.setColumns(10);
		tf_CstNo.setBounds(141, 32, 302, 25);
		tf_CstNo.setDisabledTextColor(Color.black);
		panel_2.add(tf_CstNo);
		
		JLabel lblPanNumber = new JLabel("PAN Number : ");
		lblPanNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblPanNumber.setBounds(10, 90, 121, 25);
		panel_2.add(lblPanNumber);
		
		tf_PANNo = new JTextField();
		tf_PANNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_PANNo.setColumns(10);
		tf_PANNo.setBounds(141, 90, 302, 25);
		tf_PANNo.setDisabledTextColor(Color.black);
		panel_2.add(tf_PANNo);
		
		JLabel lblVatNumber = new JLabel("VAT Number : ");
		lblVatNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblVatNumber.setBounds(10, 150, 121, 25);
		panel_2.add(lblVatNumber);
		
		tf_VATNo = new JTextField();
		tf_VATNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_VATNo.setColumns(10);
		tf_VATNo.setBounds(141, 150, 302, 25);
		tf_VATNo.setDisabledTextColor(Color.black);
		panel_2.add(tf_VATNo);
		
		JLabel lblElectricityNumber = new JLabel("Electricity Number : ");
		lblElectricityNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblElectricityNumber.setBounds(10, 220, 121, 25);
		panel_2.add(lblElectricityNumber);
		
		tf_ElectricityNo = new JTextField();
		tf_ElectricityNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_ElectricityNo.setColumns(10);
		tf_ElectricityNo.setBounds(141, 220, 302, 25);
		tf_ElectricityNo.setDisabledTextColor(Color.black);
		panel_2.add(tf_ElectricityNo);
		
		JLabel lblMobileNumber = new JLabel("Owner Name : ");
		lblMobileNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblMobileNumber.setBounds(10, 290, 121, 25);
		panel_2.add(lblMobileNumber);
		
		tf_OwnerName = new JTextField();
		tf_OwnerName.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_OwnerName.setColumns(10);
		tf_OwnerName.setBounds(141, 290, 302, 25);
		tf_OwnerName.setDisabledTextColor(Color.black);
		panel_2.add(tf_OwnerName);
		
		JLabel lblOwnerMobileNumber = new JLabel("Mobile Number : ");
		lblOwnerMobileNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblOwnerMobileNumber.setBounds(10, 360, 121, 25);
		panel_2.add(lblOwnerMobileNumber);
		
		tf_MobileNo = new JTextField();
		tf_MobileNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_MobileNo.setColumns(10);
		tf_MobileNo.setBounds(141, 360, 302, 25);
		tf_MobileNo.setDisabledTextColor(Color.black);
		panel_2.add(tf_MobileNo);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBounds(563, 551, 467, 48);
		getContentPane().add(panel_3);
		panel_3.setLayout(null);
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PDFUtils.setEnableRec(panel_1, true);
				PDFUtils.setEnableRec(panel_2, true);
			}
		});
		btnEdit.setBounds(66, 11, 89, 23);
		panel_3.add(btnEdit);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tf_StoreName.isEnabled()){
					if(PDFUtils.isMandatoryEntered(tf_StoreName)&&PDFUtils.isMandatoryEntered(tf_City)){
						updateStoreDetails();
					}else{
						JOptionPane.showMessageDialog(getContentPane(), "Please Enter Mandatory Fields!");
					}
				}else{
					JOptionPane.showMessageDialog(getContentPane(), "Please Edit the fields!");
				}
			}
		});
		btnUpdate.setBounds(302, 11, 89, 23);
		panel_3.add(btnUpdate);
		setStoreDetails();
		
		JLabel lblUploadLogo = new JLabel("Upload Logo : ");
		lblUploadLogo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblUploadLogo.setBounds(10, 427, 121, 25);
		panel_2.add(lblUploadLogo);
		
		tf_imagePath = new JTextField();
		tf_imagePath.setFont(new Font("Tahoma", Font.BOLD, 12));
		tf_imagePath.setHorizontalAlignment(SwingConstants.RIGHT);
		tf_imagePath.setDisabledTextColor(Color.BLACK);
		tf_imagePath.setColumns(10);
		tf_imagePath.setBounds(141, 427, 210, 25);
		panel_2.add(tf_imagePath);
		
		JButton btnNewButton = new JButton("Select");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
		         fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		         FileNameExtensionFilter filter = new FileNameExtensionFilter("*.images", "jpg","gif","png","jpeg","jpe");
		         fileChooser.addChoosableFileFilter(filter);
		         int result = fileChooser.showSaveDialog(null);
		         if(result == JFileChooser.APPROVE_OPTION){
		             File selectedFile = fileChooser.getSelectedFile();
		             String path = selectedFile.getAbsolutePath();
		             tf_imagePath.setText(path);
		            }
			}
		});
		btnNewButton.setBounds(361, 428, 89, 23);
		panel_2.add(btnNewButton);
		
		PDFUtils.setEnableRec(panel_1, false);
		PDFUtils.setEnableRec(panel_2, false);
		
		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}

	private void setStoreDetails() {
		MyStoreDetails myStoreDetails = MyStoreServices.getMyStoreDetails();
		tf_StoreId.setText(String.valueOf(myStoreDetails.getMyStoreId()));
		tf_StoreName.setText(myStoreDetails.getStoreName());
		tf_Address.setText(myStoreDetails.getAddress());
		tf_Address2.setText(myStoreDetails.getAddress2());
		tf_City.setText(myStoreDetails.getCity());
		tf_District.setText(myStoreDetails.getDistrict());
		tf_State.setText(myStoreDetails.getState());
		if(0==myStoreDetails.getPhone()){
			tf_Phone.setText("");
		}else{
			tf_Phone.setText(String.valueOf(myStoreDetails.getPhone()));
		}
		if(0==myStoreDetails.getCstNo()){
			tf_CstNo.setText("");
		}else{
			tf_CstNo.setText(String.valueOf(myStoreDetails.getCstNo()));
		}
		if(0==myStoreDetails.getVatNo()){
			tf_VATNo.setText("");
		}else{
			tf_VATNo.setText(String.valueOf(myStoreDetails.getVatNo()));
		}
		if(0==myStoreDetails.getElectricityNo()){
			tf_ElectricityNo.setText("");
		}else{
			tf_ElectricityNo.setText(String.valueOf(myStoreDetails.getElectricityNo()));
		}
		if(0==myStoreDetails.getMobileNo()){
			tf_MobileNo.setText("");
		}else{
			tf_MobileNo.setText(String.valueOf(myStoreDetails.getMobileNo()));
		}
		tf_PANNo.setText(myStoreDetails.getPanNo());
		tf_OwnerName.setText(myStoreDetails.getOwnerName());
	}

	protected void updateStoreDetails() {
		MyStoreDetails myStoreDetails = new MyStoreDetails();
		myStoreDetails.setMyStoreId(Integer.valueOf(tf_StoreId.getText()));
		myStoreDetails.setStoreName(tf_StoreName.getText());
		myStoreDetails.setAddress(tf_Address.getText());
		myStoreDetails.setAddress2(tf_Address2.getText());
		myStoreDetails.setCity(tf_City.getText());
		myStoreDetails.setDistrict(tf_District.getText());
		myStoreDetails.setState(tf_State.getText());
		myStoreDetails.setPhone(!tf_Phone.getText().equals("")?Long.valueOf(tf_Phone.getText()):0);
		myStoreDetails.setCstNo(!tf_CstNo.getText().equals("")?Long.valueOf(tf_CstNo.getText()):0);
		myStoreDetails.setPanNo(tf_PANNo.getText());
		myStoreDetails.setVatNo(!tf_VATNo.getText().equals("")?Long.valueOf(tf_VATNo.getText()):0);
		myStoreDetails.setElectricityNo(!tf_ElectricityNo.getText().equals("")?Long.valueOf(tf_ElectricityNo.getText()):0);
		myStoreDetails.setOwnerName(tf_OwnerName.getText());
		myStoreDetails.setMobileNo(!tf_MobileNo.getText().equals("")?Long.valueOf(tf_MobileNo.getText()):0);
		if(PDFUtils.isMandatoryEntered(tf_imagePath)){
			try {
				InputStream is = new FileInputStream(new File(tf_imagePath.getText()));
				myStoreDetails.setImagePath(is);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(getContentPane(),"Selected file does not exists ","Error",JOptionPane.WARNING_MESSAGE);
				e.printStackTrace();
			}
		}
		
		boolean isSuccess = MyStoreServices.updateStoreDetails(myStoreDetails);
		if(isSuccess){
			JOptionPane.showMessageDialog(getContentPane(), "Store Details updated successfully!");
			PDFUtils.setEnableRec(panel_1, false);
			PDFUtils.setEnableRec(panel_2, false);
		}else{
			JOptionPane.showMessageDialog(getContentPane(),"Error occured !","Error",JOptionPane.WARNING_MESSAGE);
		}
	}
}
