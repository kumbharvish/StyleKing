package com.shopbilling.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import net.java.dev.designgridlayout.DesignGridLayout;
import net.java.dev.designgridlayout.LabelAlignment;

import org.fuin.utils4swing.layout.scalable.DefaultScalableLayoutRegistry;
import org.fuin.utils4swing.layout.scalable.ScalableLayoutUtils;

import com.shopbilling.dto.StatusDTO;
import com.shopbilling.dto.Supplier;
import com.shopbilling.dto.UserDetails;
import com.shopbilling.services.SupplierServices;
import com.shopbilling.utils.PDFUtils;

public class ManageSupplierUI extends JInternalFrame {
	
	private JPanel contentPane;
	private UserDetails userDetails;
	private JTextField supplierName;
	private JTextField supplierId;
	private JTextField emailId;
	private JTextField supplierMobile;
	private JTextField supplierAddress;
	private JTextField city;
	private JTextField phoneNumber;
	private JTextField panNo;
	private JTextField MVAT;
	private JTextField comments;
	DefaultTableModel model;
	private JTable table;
	/**
	 * Create the frame.
	 */
	public ManageSupplierUI() {

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 85, 1107, 698);
		setTitle("Manage Suppliers");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(107, 11, 877, 574);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JPanel panelSupplierFileds = new JPanel();
		panelSupplierFileds.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelSupplierFileds.setBounds(69, 36, 739, 167);
		panel.add(panelSupplierFileds);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(68, 221, 741, 341);
		panel.add(scrollPane);
		setResizable(false);
		DesignGridLayout layout = new DesignGridLayout(panelSupplierFileds);
		layout.labelAlignment(LabelAlignment.RIGHT);
		supplierName= new JTextField();
		supplierId= new JTextField();
		emailId= new JTextField();
		supplierMobile= new JTextField();
		supplierAddress= new JTextField();
		city= new JTextField();
		phoneNumber= new JTextField();
		panNo= new JTextField();
		MVAT= new JTextField();
		comments= new JTextField();
		supplierName.setFont(new Font("Tahoma", Font.BOLD, 12));
		supplierId.setFont(new Font("Tahoma", Font.BOLD, 12));
		emailId.setFont(new Font("Tahoma", Font.BOLD, 12));
		supplierMobile.setFont(new Font("Tahoma", Font.BOLD, 12));
		supplierAddress.setFont(new Font("Tahoma", Font.BOLD, 12));
		city.setFont(new Font("Tahoma", Font.BOLD, 12));
		phoneNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
		panNo.setFont(new Font("Tahoma", Font.BOLD, 12));
		MVAT.setFont(new Font("Tahoma", Font.BOLD, 12));
		comments.setFont(new Font("Tahoma", Font.BOLD, 12));
		 
 		JButton deleteButton = new JButton("Delete");
 		JButton updateButton = new JButton("Update");
 		JButton saveButton = new JButton("Add");
 		JButton resetButton = new JButton("Reset");
		layout.row().grid(new JLabel("Supplier Name *:"))	.add(supplierName)	.grid(new JLabel("VAT No :"))	.add(MVAT).grid(new JLabel("PAN No :"))	.add(panNo);
		layout.row().grid(new JLabel("Phone No :"))	.add(phoneNumber).grid(new JLabel("Address :"))	.add(supplierAddress);
		layout.row().grid(new JLabel("City :"))	.add(city).grid(new JLabel("Email Id :"))	.add(emailId);
		layout.row().grid(new JLabel("Mobile No *:"))	.add(supplierMobile).grid(new JLabel("Comments :"))	.add(comments);
		layout.emptyRow();
 		layout.row().right().add(saveButton).add(updateButton).add(deleteButton).add(resetButton);
 		model = new DefaultTableModel(){
			 boolean[] columnEditables = new boolean[] {
						false, false, false, false,false, false, false, false,false, false
					};
					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
		 };
       model.setColumnIdentifiers(new String[] {
				"Supplier Id","Supplier Name", "Mobile No", "City", "comments","Address","Phone No","Email","PAN","MVAT"
			}
       );
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
	        	supplierId.setText(table.getModel().getValueAt(row, 0).toString());
				supplierName.setText(table.getModel().getValueAt(row, 1).toString());
				supplierMobile.setText(table.getModel().getValueAt(row, 2).toString());
				city.setText(table.getModel().getValueAt(row, 3).toString());
				comments.setText(table.getModel().getValueAt(row, 4).toString());
				supplierAddress.setText(table.getModel().getValueAt(row, 5).toString());
				if(!table.getModel().getValueAt(row, 6).toString().equals("0"))
					phoneNumber.setText(table.getModel().getValueAt(row, 6).toString());
				emailId.setText(table.getModel().getValueAt(row, 7).toString());
				panNo.setText(table.getModel().getValueAt(row, 8).toString());
				MVAT.setText(table.getModel().getValueAt(row, 9).toString());
				
			}
		});
		table.setModel(model);
		scrollPane.setViewportView(table);
		//Table Row Height 
		 PDFUtils.setTableRowHeight(table);
		 
		table.getColumnModel().getColumn(0).setMinWidth(0);
		 table.getColumnModel().getColumn(0).setMaxWidth(0);
		 table.getColumnModel().getColumn(0).setWidth(0);
		 
		 table.getColumnModel().getColumn(5).setMinWidth(0);
		 table.getColumnModel().getColumn(5).setMaxWidth(0);
		 table.getColumnModel().getColumn(5).setWidth(0);
		 
		 table.getColumnModel().getColumn(6).setMinWidth(0);
		 table.getColumnModel().getColumn(6).setMaxWidth(0);
		 table.getColumnModel().getColumn(6).setWidth(0);
		 
		 table.getColumnModel().getColumn(7).setMinWidth(0);
		 table.getColumnModel().getColumn(7).setMaxWidth(0);
		 table.getColumnModel().getColumn(7).setWidth(0);
		 
		 table.getColumnModel().getColumn(8).setMinWidth(0);
		 table.getColumnModel().getColumn(8).setMaxWidth(0);
		 table.getColumnModel().getColumn(8).setWidth(0);
		 
		 table.getColumnModel().getColumn(9).setMinWidth(0);
		 table.getColumnModel().getColumn(9).setMaxWidth(0);
		 table.getColumnModel().getColumn(9).setWidth(0);
		fillSuppliersTableData(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//Reset Button Action
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetSupplierTextFields();
			}
		});
		
		//Save Button Action
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveSupplierAction();
			}
		});
		//Update Button Action
		updateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateSupplierAction();
			}
		});
		
		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSupplierAction();
			}
		});
		
		supplierMobile.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});
		phoneNumber.addKeyListener(new KeyAdapter() {
			   public void keyTyped(KeyEvent e) {
			      char c = e.getKeyChar();
			      if ( ((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
			         e.consume();  // ignore event
			      }
			   }
			});

		ScalableLayoutUtils.installScalableLayoutAndKeys(new DefaultScalableLayoutRegistry(), this, 0.1);
	}
	
	protected void resetSupplierTextFields() {
		supplierId.setText("");
		supplierName.setText("");
		supplierAddress.setText("");
		supplierMobile.setText("");
		emailId.setText("");
		panNo.setText("");
		MVAT.setText("");
		city.setText("");
		phoneNumber.setText("");
		comments.setText("");
		
	}
	public void fillSuppliersTableData(DefaultTableModel model){
		List<Supplier> supplierList= SupplierServices.getAllSuppliers();
		model.setRowCount(0);
		if(supplierList.isEmpty()){
			JOptionPane.showMessageDialog(contentPane, "No Suppliers found!");
		}else{
			for(Supplier sp : supplierList){
				 model.addRow(new Object[]{sp.getSupplierID(),sp.getSupplierName(),sp.getSupplierMobile(),sp.getCity(),sp.getComments(),sp.getSupplierAddress(),sp.getPhoneNumber(),sp.getEmailId(),sp.getPanNo(),sp.getMVAT()});
			}
		}
	}

	private void saveSupplierAction() {
		if(supplierId.getText().equals("")){
			if(PDFUtils.isMandatoryEntered(supplierName) && PDFUtils.isMandatoryEntered(supplierMobile)){
				Supplier sp = new Supplier();
				sp.setSupplierName(supplierName.getText());
				sp.setCity(city.getText());
				sp.setSupplierAddress(supplierAddress.getText());
				sp.setEmailId(emailId.getText());
				sp.setComments(comments.getText());
				sp.setSupplierMobile(Long.parseLong(supplierMobile.getText()));
				sp.setPanNo(panNo.getText());
				sp.setMVAT(MVAT.getText());
				if(!phoneNumber.getText().equals(""))
					sp.setPhoneNumber(Long.parseLong(phoneNumber.getText()));
				boolean flag = SupplierServices.addSupplier(sp);
				if(flag){
					resetSupplierTextFields();
					fillSuppliersTableData(model);
					JOptionPane.showMessageDialog(contentPane, "Supplier added successfully !");
				}else{
					JOptionPane.showMessageDialog(contentPane, "Error occurred");
				}
				
			}else{
				JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
			}
		}else{
			JOptionPane.showMessageDialog(contentPane, "Please reset fields!");
		}
	}

	private void updateSupplierAction() {
		Supplier sp = new Supplier();
		if(supplierId.getText().equals("")){
			JOptionPane.showMessageDialog(contentPane, "Please select Supplier!");
		}else{
			if(PDFUtils.isMandatoryEntered(supplierName) && PDFUtils.isMandatoryEntered(supplierMobile)){
				sp.setSupplierName(supplierName.getText());
				sp.setCity(city.getText());
				sp.setSupplierAddress(supplierAddress.getText());
				sp.setEmailId(emailId.getText());
				sp.setComments(comments.getText());
				sp.setSupplierMobile(Long.parseLong(supplierMobile.getText()));
				sp.setPanNo(panNo.getText());
				sp.setMVAT(MVAT.getText());
				if(!phoneNumber.getText().equals(""))
					sp.setPhoneNumber(Long.parseLong(phoneNumber.getText()));
				sp.setSupplierID(Integer.valueOf(supplierId.getText()));
				
			boolean flag = SupplierServices.updateSupplier(sp);
			if(flag){
				resetSupplierTextFields();
				fillSuppliersTableData(model);
				JOptionPane.showMessageDialog(contentPane, "Supplier details updated sucessfully !");
			}else{
				JOptionPane.showMessageDialog(contentPane, "Error occurred");
			}
			
			}else{
				JOptionPane.showMessageDialog(contentPane, "Please Enter Mandatory fields!");
			}
		}
	}

	private void deleteSupplierAction() {
		if(supplierId.getText().equals("")){
			JOptionPane.showMessageDialog(contentPane, "Please select Supplier!");
		}else{
			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog (null, "Are you sure?","Warning",dialogButton);
			if(dialogResult == JOptionPane.YES_OPTION){
				StatusDTO status = SupplierServices.isSupplierEntryAvailable(Integer.parseInt(supplierId.getText()));
				if(status.getStatusCode()==0){
					JOptionPane.showMessageDialog(contentPane, "Delete operation not allowed. Purchase entry present for this supplier!");
				}else{
					SupplierServices.deleteSupplier(Integer.parseInt(supplierId.getText()));
					resetSupplierTextFields();
					fillSuppliersTableData(model);
					JOptionPane.showMessageDialog(contentPane, "Supplier deleted successfully !");
				}
			}else{
				resetSupplierTextFields();
			}
			
		}
	}
}
