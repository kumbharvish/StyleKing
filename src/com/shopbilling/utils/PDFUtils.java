package com.shopbilling.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.math.RoundingMode;
import java.security.Key;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.shopbilling.properties.AppProperties;

public class PDFUtils {

	private static final String JDBC_DRIVER = "JDBC.DRIVER";
	private static final String DB_URL = "DB.URL";
	private static final String USER = "DB.USERNME";
	private static final String PASS = "DB.PASSWORD";
	private static final String PDF_CONST = "SLAES";
    private static final String PDF_RANDOM = "Invoice1Hbfh667adfDEJ78";
	
	private static final String APP_DATA = "SELECT VALUE_STRING FROM "
			+ "APP_DATA WHERE DATA_NAME=?";

	private final static Logger logger = Logger.getLogger(PDFUtils.class);
	
	public static Connection getConnection() {
		Connection conn = null;

		// load Properties
		Properties prop = AppProperties.getProperties();

		if (prop != null) {
			try {
				// STEP 2: Register JDBC driver
				Class.forName(prop.getProperty(JDBC_DRIVER));
				// STEP 3: Open a connection
				conn = DriverManager.getConnection(prop.getProperty(DB_URL),
						prop.getProperty(USER), prop.getProperty(PASS));
			} catch (Exception e) {
				logger.error("Get Connection Exception :",e);
				e.printStackTrace();
			}
		} else {
			System.out.println("Please set system properties");
			logger.error("Please set system properties");
		}

		return conn;
	}

	public static int getRecordsCount(ResultSet resultset) throws SQLException {
		if (resultset.last()) {
			return resultset.getRow();
		} else {
			return 0;
		}
	}

	public static void closeConnectionAndStatment(Connection conn,
			Statement stmt) {

		try {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}

		} catch (SQLException se2) {
		}
	}

	public static boolean isMandatoryEntered(JTextField field) {

		boolean flag = false;

		if (field.getText().equals(""))
			flag = false;
		else
			flag = true;
		return flag;

	}
	public static boolean isMandatorySelected(JComboBox jCombox) {

		boolean flag = false;

		if (jCombox.getSelectedIndex()==0)
			flag = false;
		else
			flag = true;
		return flag;

	}

	// This method returns data values from app_data for given data name.
	public static List<String> getAppDataValues(String dataName) {

		List<String> dataList = new LinkedList<String>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = PDFUtils.getConnection();
			stmt = conn.prepareStatement(APP_DATA);
			stmt.setString(1, dataName);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				dataList.add(rs.getString("VALUE_STRING"));
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Get App Data Values :"+e);
			e.printStackTrace();
		} finally {
			PDFUtils.closeConnectionAndStatment(conn, stmt);
		}
		return dataList;

	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(getAppDataValues("MEASURES")); }
	 */

	public static int getRandomCode() {
		int min = 10000;
		int max = 99999;
		int randonCode = (int) Math.floor(Math.random() * (max - min + 1))
				+ min;
		return randonCode;
	}

	public static int getBillNumber() {
		int min = 1000000;
		int max = 9999999;
		int randonCode = (int) Math.floor(Math.random() * (max - min + 1))
				+ min;
		return randonCode;
	}
	
	public static long getBarcode() {
		long min = 70000000;
		long max = 79999999;
		long randonCode = (long) Math.floor(Math.random() * (max - min + 1))
				+ min;
		return randonCode;
	}

	public static double getDecimalRoundUp(Double value) {
		DecimalFormat df = new DecimalFormat("#");
		df.setRoundingMode(RoundingMode.HALF_UP);
		if (value != null)
			return Double.valueOf(df.format(value));
		return 0.0;
	}
	
	public static double getDecimalRoundUp2Decimal(Double value) {
		DecimalFormat df = new DecimalFormat("#0.00");
		df.setRoundingMode(RoundingMode.HALF_UP);
		if (value != null)
			return Double.valueOf(df.format(value));
		return 0.0;
	}

	public static String getDecimalFormat(Double value) {
		DecimalFormat df = new DecimalFormat("#0.00");
		if (value != null)
			return df.format(value);
		return "0.00";
	}
	
	public static String getAmountFormat(Double value) {
		com.ibm.icu.text.DecimalFormat df = new com.ibm.icu.text.DecimalFormat("##,##,##0.00");
		if (value != null)
			return df.format(value);
		return "0.00";
	}

	public static void removeItemFromList(List<Integer> list, Integer removeItem) {
		Iterator<Integer> it = list.iterator();

		while (it.hasNext()) {
			int value = (Integer) it.next();
			if (value == removeItem)
				it.remove();

		}
	}
	//OverLoaded
	public static void removeItemFromList(List<String> list, String removeItem) {
		Iterator<String> it = list.iterator();

		while (it.hasNext()) {
			String value = (String) it.next();
			if (removeItem.equals(value))
				it.remove();

		}
	}

	public static String getFormattedDate(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		return sdf.format(dt);
	}
	
	public static String getFormattedDateWithTime(Date dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		return sdf.format(dt);
	}
	
	public static Date getFormattedDate(String dt) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date =  sdf.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String enc(String value) throws Exception {
		Key params = generate();
		Cipher cipher = Cipher.getInstance(PDFUtils.PDF_CONST.substring(2));
		cipher.init(Cipher.ENCRYPT_MODE, params);
		byte[] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
		String encryptedValue64 = new BASE64Encoder()
				.encode(encryptedByteValue);
		return encryptedValue64;

	}

	public static String dec(String value) throws Exception {
		Key params = generate();
		Cipher cipher = Cipher.getInstance(PDFUtils.PDF_CONST.substring(2));
		cipher.init(Cipher.DECRYPT_MODE, params);
		byte[] decryptedValue64 = new BASE64Decoder().decodeBuffer(value);
		byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
		String decryptedValue = new String(decryptedByteValue, "utf-8");
		return decryptedValue;

	}

	private static Key generate() throws Exception {
		Key params = new SecretKeySpec(PDFUtils.PDF_RANDOM.substring(7).getBytes(), PDFUtils.PDF_CONST.substring(2));
		return params;
	}

	public static void populateDropdown(JComboBox<String> combobox,String dataName){
		
		for(String s :getAppDataValues(dataName)){
			combobox.addItem(s);
		}
	}
	
	public static void setEnableRec(Component container, boolean enable){
	    container.setEnabled(enable);

	    try {
	        Component[] components= ((Container) container).getComponents();
	        for (int i = 0; i < components.length; i++) {
	            setEnableRec(components[i], enable);
	        }
	    } catch (ClassCastException e) {

	    }
	}
	
	public static void main(String[] args) {
		System.out.println(getAmountFormat(54.809));		
	}
	
	public static void openWindowsDocument(String filePath){
		try {
			if ((new File(filePath)).exists()) {
				Process p = Runtime
				   .getRuntime()
				   .exec("rundll32 url.dll,FileProtocolHandler "+filePath);
				p.waitFor();
			} else {
				logger.error("openWindowsDocument File does not exists: "+filePath);
			}
	  	  } catch (Exception ex) {
			ex.printStackTrace();
			logger.error("openWindowsDocument Exception: ",ex);
		  }
		
	}
	public static void setTableRowHeight(JTable table){
		//Table Row Height 
		 table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 table.setRowHeight(20);
		 //Header
		 JTableHeader header = table.getTableHeader();
		 header.setFont(new Font("Dialog", Font.BOLD, 12));
		 header.setBackground(Color.GRAY);
		 header.setForeground(Color.WHITE);
	}
	
	public static ImageIcon resizeImage( byte[] imgPath,JLabel label){
        ImageIcon MyImage = new ImageIcon(imgPath);
        Image img = MyImage.getImage();
        Image newImage = img.getScaledInstance(label.getWidth(), label.getHeight(),Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(newImage);
        return image;
    }
	
}
