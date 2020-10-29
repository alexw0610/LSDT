import java.io.*;
import java.nio.ByteBuffer;
public class Table{
	
	public String name;
	private String[] columnNames;
	private int[] columnOffsets;
	private byte[] types;
	
	
	public Table(String name, int[] offsets, String[] names, byte[] types){
		this.name = name;
		this.columnOffsets = offsets;
		this.columnNames = names;
		this.types = types;
	}
	
	public String initTable(){
		File datafile = new File("./"+name+".adb");
		
		if(!datafile.exists()){
			String indexString = getIndexString();
			try{
			
				FileOutputStream out = new FileOutputStream(datafile);
				out.close();

				FileOutputStream indexout = new FileOutputStream("./index.adb",true);
				indexout.write(indexString.getBytes());
				indexout.close();
				return "Created Table \""+name+"\""; 
				
			}catch(IOException e){
				
				System.err.println("Error creating database file for Table \""+name+"\"");
				return "Error"; 
				
			}
		}else{
			return "Table\""+name+"\" already exists";
		}
	}
	
	public String insert(String data){
		
		File datafile = new File("./"+name+".adb");

		if(datafile.exists()){
			
			try{
				FileOutputStream dataout = new FileOutputStream(datafile,true);
			
				String[] datastring = data.split(",");
			
				for(int i = 0; i < datastring.length && i < types.length; i++){
					if(types[i] == 1){
						String temp = datastring[i];
						while(temp.length() < columnOffsets[i]){
							temp+="~";
						}
						
						dataout.write(temp.getBytes());
					
					}else if(types[i] == 2){
						dataout.write(intToByteArray(Integer.parseInt(datastring[i])));
					
					}else if(types[i] == 3){
						dataout.write(floatToByteArray(Float.parseFloat(datastring[i])));
					
					}else if(types[i] == 4){
						if(datastring[i].equals("true")){
							dataout.write((byte)1);
						}else{
							dataout.write((byte)0);
						}
					
				}
			}
			
			dataout.close();
			return "Saved Data to Table";
			}catch(IOException e){
				
				System.err.println("Error creating database file for Table \""+name+"\"");
				return "Error"; 
				
			}
			
			
			
			
		}else{
			return "Error datafile for Table \""+this.name+"\" does not exist";
		}
		
	}
	
	private byte[] intToByteArray(int number){
		
		return ByteBuffer.allocate(4).putInt(number).array();
		
	}
	
	private byte[] floatToByteArray(float number){
		
		return ByteBuffer.allocate(4).putFloat(number).array();
		
	}
	
	private int byteArrToInt(byte[] arr){
	
        ByteBuffer byteBuffer = ByteBuffer.wrap(arr);
        return byteBuffer.getInt();
		
    }
	private float byteArrToFloat(byte[] arr){
	
        ByteBuffer byteBuffer = ByteBuffer.wrap(arr);
        return byteBuffer.getFloat();
	
	}
	
	private String getIndexString(){
		
		String temp = this.name;
		
		for(int i = 0; i< this.columnNames.length;i++){
			
			temp += ","+this.columnOffsets[i]+"."+this.types[i]+"."+this.columnNames[i];
		
		}
		
		temp += ";";
		
		return temp;
		
	}
	
	private String getData(){
		
		String ret = "";
		
		File datafile = new File("./"+this.name+".adb");
		try{
			FileInputStream dataIn = new FileInputStream(datafile);
			int rowLength = 0;
			System.out.println(this.columnOffsets.length+" "+this.name+" "+this.columnOffsets[0]);
			for(int i = 0; i < this.columnOffsets.length; i++){
				rowLength += this.columnOffsets[i];
			}
			int dataCount = dataIn.available()/rowLength;
			for(int i = 0;i<dataCount;i++){
				String row = i+"  |";
				for(int c = 0; c<columnOffsets.length;c++){
					byte[] temp = new byte[columnOffsets[c]];
					dataIn.read(temp);
					if(types[c] == 1){
						String result = new String(temp,"UTF-8");
						result = result.replace("~"," ");
						row += " "+result+" |";
						
					}else if(types[c] == 2){
						int result = byteArrToInt(temp);
						row += " "+result;
						String head = columnNames[c]+"  ";
						for(int s = 0; s< head.length()-((" "+result).length());s++){
							row += " ";
						}
						row += "|";
						
					}else if(types[c] == 3){
						float result = byteArrToFloat(temp);
						row += " "+result;
						String head = columnNames[c]+"  ";
						for(int s = 0; s< head.length()-((" "+result).length());s++){
							row += " ";
						}
						row += "|";
						
					}else if(types[c] == 4){
						if(temp[0] == (byte)1){
							row += " true";
							String head = columnNames[c]+"  ";
							for(int s = 0; s< head.length()-((" true").length());s++){
							 row += " ";
							}
							row += "|";
							
						}else{
							row += " false";
							String head = columnNames[c]+"  ";
							for(int s = 0; s< head.length()-((" false").length());s++){
							row += " ";
							}
							row += "|";
							
						}
					}
				}
				ret += row+"\n";
				
			}
			dataIn.close();
			return ret;
		}catch(IOException e){
			System.err.println("Error creating database file for Table \""+name+"\"");
			return "Error Printing Table \""+this.name+"\"";
		}
		
		
	
	}
	
	public String printTable(){
		
		String ret = "";
		
		String header = "id |";
		for(int i = 0; i < this.columnNames.length;i++){
			if(types[i] == 1){
				header += " char";
				header += " "+this.columnNames[i]+" ";
				String test = " char "+this.columnNames[i]+" ";
				for(int l = 0; l < (this.columnOffsets[i])-(test.length());l++){
					header += " ";
				}
				header += "  |";
			}else{
				header += " "+this.columnNames[i]+" |";
			}
			
			
		}
		header += "\n";
		int length = header.length()-5-this.name.length();
		
		String title = "";
		
		title += "---|";
		for(int i = 0; i<length/2;i++){
			title += "-";
		}
		title += this.name;
		for(int i = 0; i<(length-length/2);i++){
			title += "-";
		}
		title +="|\n";
		
		String spacer = "";
		
		spacer += "---|";
		for(int i = 0; i<length/2;i++){
			spacer += "-";
		}
		for(int i = 0; i<this.name.length();i++){
			spacer += "-";
		}
		for(int i = 0; i<(length-length/2);i++){
			spacer += "-";
		}
		spacer += "|\n";

		ret += title + spacer + header + spacer + getData() + spacer + "\n";
		
		return ret;
		
		
	}

	public String deleteTable(){
		File datafile = new File("./"+this.name+".adb");
		String result = "";
		if(datafile.delete()){
			result = "Successfully deleted "+this.name+".adb!";
		}else{
			if(datafile.exists()){
				result = "Error deleting table "+this.name+".adb!";
			}else{
				result = "Error deleting table "+this.name+".adb. The file was not found!";
			}
		}
		return result;
	}
	
	
}