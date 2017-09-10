package com.shopbilling.ui;

import java.awt.Font;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.shopbilling.dto.Customer;
import com.shopbilling.dto.ItemDetails;
import com.shopbilling.services.ReportServices;
import com.shopbilling.utils.PDFUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettlementCustListUI extends JDialog {

	private JPanel contentPane;
	private JTable table;
	private DefaultTableModel itemsModel;

	/**
	 * Create the frame.
	 */
	public SettlementCustListUI(JFrame frame,final CashCounterUI cash) {
		super(frame,"Settlement By Customers",true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage("shop32X32.png"));
		setBounds(400, 200, 460, 346);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnClose.setBounds(182, 283, 89, 23);
		contentPane.add(btnClose);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(18, 11, 417, 261);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Customer Name", "Amount"
			}
		));
		table.setEnabled(false);
		PDFUtils.setTableRowHeight(table);
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		 DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		 rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		List<Customer> custList = ReportServices.getSettledCustomerList(cash.dateChooser.getDate()==null?null:new java.sql.Date(cash.dateChooser.getDate().getTime()));
		System.out.println("List Size : "+custList.size());
		for(Customer cust : custList){
			 itemsModel = (DefaultTableModel)table.getModel();
			 itemsModel.addRow(new Object[]{cust.getCustName(),PDFUtils.getDecimalFormat(cust.getAmount())});
		 }
		
	}
}
