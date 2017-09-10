import java.util.ArrayList;
import java.util.List;


public class testjava {

	
	public static void main(String[] args) {
		List<String> inputList = new ArrayList<String>();
		List<String> inputList2 = new ArrayList<String>();
		List<String> outputList = new ArrayList<String>();
		inputList.add("abc");
		inputList.add("bcd");
		inputList.add("vishal");
		inputList.add("Bhagwat");
		inputList2.add("mohan");
		inputList2.add("ram");
		inputList2.add("ganesh");
		inputList2.add("data");
		
		for(String str:inputList){
			if(str.contains("a")){
				outputList.add(str);
			}
		}
		for(String str:inputList2){
			if(str.contains("a")){
				outputList.add(str);
			}
		}
		System.out.println("Op : "+outputList);
		}

}
