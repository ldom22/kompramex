import java.net.*;
import javax.net.*;
import javax.net.ssl.*;
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.image.*;

public class URLConnectionReader {
    
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
    
    //covert to rendered image
    public static RenderedImage getRenderedImage(Image in)
    {
        int w = in.getWidth(null);
        int h = in.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;
        BufferedImage out = new BufferedImage(w, h, type);
        Graphics2D g2 = out.createGraphics();
        g2.drawImage(in, null, null);
        g2.dispose();
        return out;
    }
    
    //normal http request
    public static String getText(String url) throws Exception {
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        connection.setConnectTimeout(20000);
        connection.setReadTimeout(20000);
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
    
    //http get request
    public static String getGText(String url) throws Exception {
        URL website = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)website.openConnection();
        connection.setRequestMethod("POST");
        BufferedReader in = new BufferedReader(
                                               new InputStreamReader(
                                                                     connection.getInputStream()));
        
        StringBuilder response = new StringBuilder();
        String inputLine;
        
        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        
        in.close();
        
        return response.toString();
    }
	
	public static String getSecureText(String url) throws Exception {
		
		// Create a new trust manager that trust all certificates
	TrustManager[] trustAllCerts = new TrustManager[]{
		new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}	
			public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			}
			public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
			}
		}
	};

	// Activate the new trust manager
	SSLContext sc = SSLContext.getInstance("SSL");
	sc.init(null, trustAllCerts, new java.security.SecureRandom());
	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	
        URL website = new URL(url);
        URLConnection connection = website.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }

    public static void main(String[] args) throws Exception {
        saveImage(args[0],args[0].substring(args[0].lastIndexOf("/"),args[0].length()));
    }
}