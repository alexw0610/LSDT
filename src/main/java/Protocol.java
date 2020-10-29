import java.util.regex.*;

public class Protocol{
	
	private final static Protocol instance = new Protocol();
	private static final Pattern createRegex = Pattern.compile("(nt)\\s[a-zA-Z]+\\([a-z]+(\\([0-9]+\\))?\\s[a-zA-Z]+(\\,[a-z]+(\\([0-9]+\\))?\\s[a-zA-Z]+)*\\)");
	private static final Pattern deleteRegex = Pattern.compile("(dt)\\s[a-zA-Z]+");
	private Protocol(){
			
	}
	
	public static Protocol getInstance(){
		return instance;
	}
	
	
	public String parseInput(String input){
		
		if("".equals(input)){
			return "Input not valid: Empty Command";
		}
		
		if(input.startsWith("nt")){
			
			Matcher m = createRegex.matcher(input);
			boolean b = m.matches();
			if(m.matches()){
				return createTable(input);
			}else{
				return "Input not valid: Command doesnt match Regex: "+m.pattern();
			}
		
		}else if(input.startsWith("dt")){
			Matcher m = deleteRegex.matcher(input);
			boolean b = m.matches();
			if(m.matches()){
				return deleteTable(input);
			}else{
				return "Input not valid: Command doesnt match Regex: "+m.pattern();
			}

		}else if(input.startsWith("it")){
			

		}else if(input.equals("lt")){

			LSDT.loadTables();
			return LSDT.listTables();
			//out.writeUTF(listTables());
			
		}
		
		return "Input not valid: Unknown command";
		
		
		
		
	}
	private String deleteTable(String input){
		String[] split = input.split("\\s",2);
		String tablename = split[1];
		Table[] loadedTables = LSDT.loadedTables;
		String result = "";
		for(Table table : loadedTables){
			if(tablename.equals(table.name)){
				result = table.deleteTable();
				result +="\n"+LSDT.deleteTable(table);
				break;
			}
		}
		LSDT.loadTables();
		return result;
	}
	
	private String createTable(String input){
		
		String[] result = input.split("\\(",2);
		result[1] = result[1].substring(0,result[1].length()-1);
		String tableName = result[0].split("\\s")[1];
		String[] columns = result[1].split(",");
		
		String[] columnNames = new String[columns.length];
		int[] columnOffsets = new int[columns.length];
		byte[] columnTypes = new byte[columns.length];
		
		for(int i = 0; i< columns.length; i++){
			
			columnNames[i] = columns[i].split("\\s")[1];
			String type = columns[i].split("\\s")[0];
			int limit = 1;
			
			if(type.startsWith("char")){
				
				String limittemp = type.split("\\(")[1];
				type = type.split("\\(")[0];
				limit = Integer.parseInt(limittemp.substring(0,limittemp.length()-1));
				columnOffsets[i] = 2*limit;
				columnTypes[i] = (byte)1;
				
			}else if(type.equals("int")){
				
				columnOffsets[i] = 4; 
				columnTypes[i] = (byte)2;
				
			}else if(type.equals("float")){
				
				columnOffsets[i] = 4; 
				columnTypes[i] = (byte)3;

			}else if(type.equals("bool")){
				
				columnOffsets[i] = 1; 
				columnTypes[i] = (byte)4;

			}
			
		}

		Table temp = new Table(tableName,columnOffsets,columnNames,columnTypes);
		return temp.initTable();
	}

	
	
	
}