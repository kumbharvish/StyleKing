package com.shopbilling.services;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.shopbilling.constants.AppConstants;
import com.shopbilling.dto.MailConfigDTO;
import com.shopbilling.dto.StatusDTO;
import com.shopbilling.helperclasses.EmailAttachmentSender;
import com.shopbilling.utils.MailConfigurationServices;
import com.shopbilling.utils.PDFUtils;

public class DBBackupService {

	private final static Logger logger = Logger.getLogger(DBBackupService.class);

	
	public static void main(String[] args) {
		createDBDump();
	}

	
	public static void createDBDump() {
	    try {
	    	Date currentDate = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    	String folderLocation = PDFUtils.getAppDataValues("MYSTORE_HOME").get(0)+AppConstants.DATA_BACKUP_FOLDER;
	    	String mySqlHome = PDFUtils.getAppDataValues("MYSQL_HOME").get(0);
	    	logger.error("mySqlHome : "+mySqlHome);
	    	String fileName="\\\\DataBackup_"+sdf.format(currentDate)+".sql";
	        String executeCmd = mySqlHome+"\\\\bin\\\\mysqldump -u root -ppassword billing_app -r "+folderLocation+fileName;
	        /*NOTE: Executing the command here*/
	        System.out.println(executeCmd);
	        logger.error("DB dump : "+executeCmd);
	        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	        int processComplete = runtimeProcess.waitFor();

	        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
	        if (processComplete == 0) {
	        	//JOptionPane.showMessageDialog(null, "Data Backup Successfully Completed !", "Data Backup", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	        	JOptionPane.showMessageDialog(null, "Data Backup Failed !", "Data Backup", JOptionPane.INFORMATION_MESSAGE);
	        }

	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	logger.error("Data Backup Exception",ex);
	        JOptionPane.showMessageDialog(null, "Error at Data Backup" + ex.getMessage());
	    }
	}
	
	//Create DB Dump and Send on Mail Configured
	public static void createDBDumpSendOnMail() {
	    try {
	    	MailConfigDTO mail = MailConfigurationServices.getMailConfig();
	    			
	    	Date currentDate = new Date();
	    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
	    	String folderLocation = PDFUtils.getAppDataValues("MYSTORE_HOME").get(0)+AppConstants.DATA_BACKUP_FOLDER;
	    	String mySqlHome = PDFUtils.getAppDataValues("MYSQL_HOME").get(0);
	    	logger.error("mySqlHome : "+mySqlHome);
	    	String fileName="\\\\DataBackup_"+sdf.format(currentDate)+".sql";
	        String executeCmd = mySqlHome+"\\\\bin\\\\mysqldump -u root -ppassword billing_app -r "+folderLocation+fileName;
	        /*NOTE: Executing the command here*/
	        System.out.println(executeCmd);
	        logger.error("DB dump : "+executeCmd);
	        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	        int processComplete = runtimeProcess.waitFor();

	        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
	        if (processComplete == 0) {
	        	//JOptionPane.showMessageDialog(null, "Data Backup Successfully Completed !", "Data Backup", JOptionPane.INFORMATION_MESSAGE);
	        	if("Y".equals(mail.getIsEnabled())){
	        		StatusDTO status = EmailAttachmentSender.sendEmailWithAttachments(mail, folderLocation+fileName);
	        		if(0==status.getStatusCode()){
	        			JOptionPane.showMessageDialog(null, "Data Backup Mail Sent Successfully !", "Data Backup Mail", JOptionPane.INFORMATION_MESSAGE);
	        		}else{
	        			if(status.getException().contains("Unknown SMTP host")){
	            			JOptionPane.showMessageDialog(null, "Please check your Internet Connection !", "Mail Send Error", JOptionPane.INFORMATION_MESSAGE);
	            		}
	        			if(status.getException().contains("AuthFail")){
	            			JOptionPane.showMessageDialog(null, "Your Mail From Id or Password is incorrect. Please check Mail Configurations !", "Mail Send Error", JOptionPane.INFORMATION_MESSAGE);
	            		}
	        		}
	        	}else{
	        		JOptionPane.showMessageDialog(null, "Data Backup Successfully Completed !", "Data Backup", JOptionPane.INFORMATION_MESSAGE);
	        	}
	        } else {
	        	JOptionPane.showMessageDialog(null, "Data Backup Failed !", "Data Backup", JOptionPane.WARNING_MESSAGE);
	        }

	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	logger.error("Data Backup Exception",ex);
	        JOptionPane.showMessageDialog(null, "Error at Data Backup" + ex.getMessage());
	    }
	}
	//Restore DB Dump
	public static void importDBDump(String fileLocation) {
	    try {
	    	String finalFileLocation = fileLocation.replace("\\", "\\\\");
	    	System.out.println("Path :"+finalFileLocation);
	    	String mySqlHome = PDFUtils.getAppDataValues("MYSQL_HOME").get(0);
	    	
	        String executeCmd = mySqlHome+"\\bin\\mysql -u root -ppassword billing_app "+finalFileLocation;
	        System.out.println("executeCmd :"+executeCmd);
	        /*NOTE: Executing the command here*/
	        Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
	        int processComplete = runtimeProcess.waitFor();

	        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
	        if (processComplete == 0) {
	        	JOptionPane.showMessageDialog(null, "Data Restored Successfully!", "Restore Data", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	        	JOptionPane.showMessageDialog(null, "Data Restore Failed !", "Restore Data", JOptionPane.INFORMATION_MESSAGE);
	        }

	    } catch (Exception ex) {
	    	ex.printStackTrace();
	    	logger.error("Restore Backup Exception :",ex);
	        JOptionPane.showMessageDialog(null, "Error at Backup restore" + ex.getMessage());
	    }
	}
}
