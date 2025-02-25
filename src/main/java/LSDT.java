import java.io.*;

public class LSDT{

	public final static LSDT instance = new LSDT();
	public Table[] loadedTables = new Table[0];

	private LSDT(){

		System.out.println("Lightweight SQL-like Database");
		File indexfile = new File("./index.adb");
		if(!indexfile.exists()){
			createIndexFile();
		}

		loadTables();

	}

	public void consoleLoop(){

		try{
			BufferedReader consolein = new BufferedReader(new InputStreamReader(System.in));
			String input = "";

			while(true){
				input = consolein.readLine();

				if(input.equals("close")|| input.equals("q")){

					System.out.println("Closing");
					System.exit(1);

				}else{
					System.out.println(Protocol.getInstance().parseInput(input));
				}
			}
		}catch(IOException e){
			System.err.println("Error creating Server");
		}

	}

	public static void main(String[] args){

		LSDT.instance.consoleLoop();

	}
	
	private void createIndexFile(){
		try{
			FileOutputStream out = new FileOutputStream("./index.adb");
			out.close();	
		}catch(IOException e){
			System.err.println("Error creating index file");
		}
	}

	public String listTables(){
		String result = "No tables found!";
		if(loadedTables.length>0){
			result ="";
		}
		for(int i = 0; i < loadedTables.length; i++){
			result += loadedTables[i].printTable();
		}
		return result;
	}
	
	public void loadTables(){
		
		try{
			File indexfile = new File("./index.adb");
			indexfile.createNewFile();

			if(indexfile.exists()){
				FileInputStream in = new FileInputStream(indexfile);
				int max = in.available();
				System.out.println("Total bytes in index File: "+max);
				int data = 0;
				String content = "";
				while((data = in.read())!= -1){
					content = content + (char)data;
				}
				in.close();
				if(content.isEmpty()){
					System.out.println("No tables found!");
					loadedTables = new Table[0];
					return;
				}
				String[] tables = content.split(";");
				loadedTables = new Table[tables.length];
				System.out.println("Total tables found: "+loadedTables.length);
				for(int i = 0; i<loadedTables.length;i++){

					String[] columns = tables[i].split(",");
					int[] offsets = new int[columns.length-1];
					String[] names = new String[columns.length-1];;
					byte[] types = new byte[columns.length-1];;
				
					for(int c = 1; c<columns.length;c++){
					
						String[] values = columns[c].split("\\.");
						offsets[c-1] = Integer.parseInt(values[0]);
						types[c-1] = (byte)Integer.parseInt(values[1]);
						names[c-1] = values[2];
					}
					Table temp = new Table(columns[0],offsets,names,types);
					loadedTables[i] = temp;
				
				
				}
			}
		
		
		}catch(IOException e){
			System.err.println("Error loading Tables");
		}
		
	}

	public String deleteTable(Table table){

		try{
			File indexfile = new File("./index.adb");

			if(indexfile.exists()){
				FileInputStream in = new FileInputStream(indexfile);

				int data = 0;
				String content = "";
				while((data = in.read())!= -1){
					content = content + (char)data;
				}
				in.close();


				if(new File("./index.adb.old").exists()){
					new File("./index.adb.old").delete();
				}
				FileOutputStream indexOldOut = new FileOutputStream("./index.adb.old",true);
				indexOldOut.write(content.getBytes());
				indexOldOut.close();
				if(!new File("./index.adb").delete()){
					return "Error deleting table. Cannot modify ./index.adb. Is the file opened?";
				}

				String[] tables = content.split(";");
				File newIndexfile = new File("./index.adb");
				newIndexfile.createNewFile();
				FileOutputStream indexout = new FileOutputStream("./index.adb",true);
				for(int i = 0; i < tables.length; i++){
					String tableName = tables[i].split(",")[0];
					if(!tableName.equals(table.name)){
						indexout.write(tables[i].getBytes());
					}

				}
				indexout.close();
				return "Successfully removed table ["+table.name+"] from index file \n"+
						"Saved old indexfile to index.adb.old as a backup\n"+
						"WARNING: this backup gets overwritten each time a table is dropped!";

			}else{
				return "Error, index file not found!";
			}


		}catch(IOException e){
			System.err.println("Error deleting Table");
		}

		return "Successfully, removed table ["+table.name+"] from the index file. \n"+
				"old index file renamed to index.adb.old";
	}

}

