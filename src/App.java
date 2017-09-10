import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

public class App{

   public static void main(String[] args) throws IOException{

       /*String command = "ipconfig /all";
       Process p = Runtime.getRuntime().exec(command);

       BufferedReader inn = new BufferedReader(new InputStreamReader(p.getInputStream()));
       Pattern pattern = Pattern.compile(".*Physical Addres.*: (.*)");

       while (true) {
            String line = inn.readLine();

	    if (line == null)
	        break;

	    Matcher mm = pattern.matcher(line);
	    if (mm.matches()) {
	        System.out.println(mm.group(1));
	    }
	}
   }*/
	   
	   Properties p = System.getProperties();
	   Enumeration keys = p.keys();
	   while (keys.hasMoreElements()) {
	       String key = (String)keys.nextElement();
	       String value = (String)p.get(key);
	       System.out.println(key + ": " + value);
	   }   
	   Map<String, String> env = System.getenv();
	   System.out.println(env);
   }
   
   
   
   
}