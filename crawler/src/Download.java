import java.io.*;

public class Download {
	public static void main(String args[]) throws Exception{
		String html = URLConnectionReader.getText("http://www.sears.com.mx/");
		PrintWriter pw = new PrintWriter(new File("justdownloaded.html"));
		pw.println(html);
		pw.close();
	}
}