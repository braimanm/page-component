package ui.auto.core.data.generators;

import java.util.List;

public class File2ListGenerator extends File2ListReader{
	private List<String> list;
	
	public File2ListGenerator(String fileName){
		list=populate("/" + fileName);
	}
	
	public String getValue(){
		if (list.size()==0) 
			throw new RuntimeException("The file is empty!");
		int index=(int) (Math.random()*list.size());
		return list.get(index);
	}
	
}
