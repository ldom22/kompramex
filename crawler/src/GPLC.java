//file library
import java.io.*;

//joins results in a class
public class GPLC {
	public static void main(String args[]) throws Exception{
		//1)get list of text files
		String files;
		File folder = new File("..\\results");
		File[] listOfFiles = folder.listFiles(); 
		//prepare class output file
		PrintWriter pw = new PrintWriter(new File("..\\results\\Products.java"));
		pw.println("public class Products {");
		pw.println("	public static Product[] List = {");
		//2)read and print them to single class file
		for(File f: listOfFiles){
			if(!f.getName().endsWith(".txt")){
				break;
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String input = br.readLine();
			while(input != null){
				pw.print("\t\tnew Product(");
				String[] item = input.replace("\"","\\\"").split("@@@@");
				for(String s: item){
					s = s.replace("\t","");
					pw.print("\""+s+"\",");
				}
				pw.println("),");
				pw.flush();
				//read next line
				input = br.readLine();
			}
		}
		//3)end file
		pw.println("	};");
		pw.println("}");
		pw.flush();
		pw.close();
	}
}