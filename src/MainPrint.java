import java.util.Date;

import com.shopbilling.services.PrinterService;

public class MainPrint {
 
	public static void main(String[] args) {
 
		PrinterService printerService = new PrinterService();
		
		System.out.println(printerService.getPrinters());
		String Header = 
	            "   ****Super Market****       \n"
	            + "Date: "+new Date()+"     Time: \n"
	            + "---------------------------------\n"
	            + "Name          Qty    Rate     Amt\n"
	            + "---------------------------------\n";
		//print some stuff
		printerService.printString("doPDF 8", "\n\n testing testing 1 2 3eeeee \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
 
		// cut that paper!
		byte[] cutP = new byte[] { 0x1d, 'V', 1 };
 
		printerService.printBytes("doPDF 8", cutP);
	
	}
 
	/*public void printThisBill()
	  {

	      DefaultTableModel mod = (DefaultTableModel) jTable1.getModel();
	      DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	      DateFormat timeFormat = new SimpleDateFormat("HH:mm");
	      //get current date time with Date()
	      Date date = new Date();
	      Date time = new Date();
	      String Date = dateFormat.format(date);
	      String Time = timeFormat.format(time);
	      String Header = 
	            "   ****Super Market****       \n"
	            + "Date: "+Date+"     Time: "+Time+"\n"
	            + "---------------------------------\n"
	            + "Name          Qty    Rate     Amt\n"
	            + "---------------------------------\n";

	      String amt  =    
	            "\n \n \nTotal Amount = "+  amt()   +"\n"
	            + "Tax ="   +  tax()    + "\n"
	            + "*********************************\n"
	            + "Thank you. \n";

	      String bill = Header;
	      int i = 0;
	      do
	      {

	         String name =     ""+ mod.getValueAt(i, 2);
	         String qty =      ""+ mod.getValueAt(i, 3);
	         String rate =     ""+ mod.getValueAt(i, 4);
	         String amount =   ""+ mod.getValueAt(i, 6);

	         if(name.length() > 12)
	         {
	             name = name.substring(0, 12)+"  ";
	         }
	         else
	         {
	             for(int j= name.length()-12; j<= name.length(); j++);
	             {
	                 name = name+" ";
	             } 
	         }


	         if(qty.length()<=5)
	         {
	             for(int j= 0; j<= qty.length()-5; j++);
	             {
	                qty = qty+" ";
	             }
	         }

	         rate = rate;
	         String items = 
	             name+"\t"+qty+"\t"+rate+"\t"+amount+"\n";

	         bill = bill+ items;       
	         i++;

	     } while(i <= mod.getRowCount()-1);

	     bill = bill+amt;
	     System.out.println(bill);
	     printCard(bill);
	     dispose();
	 }*/
}