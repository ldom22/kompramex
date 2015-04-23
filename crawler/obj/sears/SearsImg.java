//libraries
import java.io.*;
import java.net.*;

//gets the sears images
public class SearsImg {
    
    //save an image
    public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);
        
		byte[] b = new byte[2048];
		int length;
        
		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}
        
		is.close();
		os.close();
	}
    
	public static void main(String args[]) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("Sears_products.txt"));
        String input = br.readLine();
        while(input != null){
            String[] item = input.split("@@@@");
            try {
                String filename = item[4];
                filename = filename.substring(filename.lastIndexOf("/")+1,filename.length());
                saveImage(item[4],filename);
                System.out.println(filename);
            } catch(Exception e){
                System.out.println("Could not download image");
            }
            input = br.readLine();
        }
	}
}
