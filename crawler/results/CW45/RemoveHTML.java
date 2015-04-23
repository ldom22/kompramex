import java.io.*;

public class RemoveHTML {
	//unit test
	public static void main(String args[]) throws Exception {
        BufferedReader b = new BufferedReader(new FileReader(new File("Famsa_products.txt")));
        PrintWriter pw = new PrintWriter(new File("output.txt"));
        //pw.println(" ");
        //System.exit(0);
        String line = b.readLine();
        int n=0;
        while(line != null){
            n++;
            String newli = line;
            int i = newli.indexOf("<");
            while(i>0){
                int j = line.indexOf(">");
                newli = line.substring(0,i);
                newli += " ";
                newli += line.substring(j+1,line.length());
                line = newli;
                i = newli.indexOf("<");
            }
            //remove crap
            newli = newli.replace("&nbsp;"," ");
            newli = newli.replace("\\u00e1","a");
            newli = newli.replace("\\u00e9","e");
            newli = newli.replace("\\u00ed","i");
            newli = newli.replace("\\u00f3","o");
            newli = newli.replace("\\u00fa","u");
            newli = newli.replace("  "," ");
            //make below 500 chars
            if(!newli.equals("")){
                newli = newli.replace(" ", "%20");
                while(newli.length()>500){
                    newli = newli.substring(0,newli.lastIndexOf("%20"));
                }
            }
            pw.println(newli);
            System.out.println(n+"");
            line = b.readLine();
        }
        b.close();
        pw.close();
	}
}
