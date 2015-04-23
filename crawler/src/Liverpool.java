//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls liverpool
public class Liverpool{

	//for debug purposes
	public static void saveTextToFile(String text, String file){
		try {
			PrintWriter pw = new PrintWriter(new File(file));
			pw.print(text);
			pw.flush();
			pw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		System.exit(0);
	}
    
    static PrintWriter pw;
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.liverpool.com.mx/tienda");
            html = html.trim();
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[2000];
		//find category urls
		int i = html.indexOf("a href=\"/tienda/");
		while(i>0){
			//find category urls
            html = html.substring(i+9,html.length());
            i = html.indexOf("\"");
			categories[num_categories] = "http://www.liverpool.com.mx/"+html.substring(0,i);
			System.out.println(categories[num_categories]);
			html = html.substring(i,html.length());
			//find next category
			num_categories++;
			i = html.indexOf("a href=\"/tienda/");
		}
		System.out.println("num categorias:"+num_categories);
		//write to file
		try {
			pw = new PrintWriter(new File("Liverpool_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
            System.exit(0);
		}
		//2) for each category page
		for(i=0; i<num_categories; i++){
			if(categories[i].contains("cat")){
                try {
                    //download page
                    html = URLConnectionReader.getText(categories[i]);
                    html = html.trim();
                    System.out.println(categories[i]);
                    //process
                    Product[] temp = process(html);
                    //write to file already in process
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
		}
		//close file
		pw.close();
	}
	
	//online search
	public static Product[] search(String text) throws Exception{
		text = text.replace(" ","+");
		String url = "http://www.liverpool.com.mx/shopping/content/search?Dy=1&Nty=1&N=0&Ntt=";
		url += text;
		String html = URLConnectionReader.getText(url);
		return process(html);
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("Selecciona para comparar");
		html = html.substring(temp,html.length());
		//first productlink
		temp = html.indexOf("href=\"");
		html = html.substring(temp+6,html.length());
		String productlinktemp = html.substring(0,html.indexOf("\""));
		String baseurl = "http://www.liverpool.com.mx";
		productlinktemp = baseurl+productlinktemp;
		//find products
		while(temp!=-1){
			//name
            temp = html.indexOf("title=\"");
			html = html.substring(temp+7,html.length());
			items[i] = new Product();
			items[i].productlink = productlinktemp;
			temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp);System.out.println(items[i].name);
			html = html.substring(temp,html.length());
            //upc
            items[i].upc = items[i].productlink.substring(items[i].productlink.lastIndexOf("/")+1,items[i].productlink.length());
            //image
            temp = html.indexOf("data-original=\"");
            html = html.substring(temp+15,html.length());
            temp = html.indexOf("\"");
            String imageURL = html.substring(0,temp);
            html = html.substring(temp,html.length());
            String filename = items[i].upc + ".jpg";
            try {
                if(!(new File(filename).exists())){
                    URL url = new URL(imageURL);
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(20000);
                    urlc.setReadTimeout(20000);
                    Image image = ImageIO.read(urlc.getInputStream());
                    ImageIO.write(URLConnectionReader.getRenderedImage(image), "jpg", new File(filename));
                    items[i].image = "imgs/liverpool/"+filename;
                } else {
                    items[i].image = "imgs/liverpool/"+filename;
                }
            } catch(Exception e){
                System.out.println("Could not download image.");
                items[i].image = "";
                e.printStackTrace();
                
            }
			//price
            temp = html.indexOf("$");
			html = html.substring(temp,html.length());
            temp = html.indexOf("$");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</p>");
			items[i].price = html.substring(0,temp).trim();
			html = html.substring(temp,html.length());
            //store
			items[i].store = "Liverpool";
            //download description
            /*try {
                String htmlD = URLConnectionReader.getText(items[i].productlink);
                saveTextToFile(htmlD,"wtf.txt");
                temp = htmlD.indexOf("descripcion_heredada");
                htmlD = htmlD.substring(temp+20,htmlD.length());
                temp = htmlD.indexOf("<h3>");
                htmlD = htmlD.substring(temp+4,htmlD.length());
                temp = htmlD.indexOf("<P ALIGN=\"LEFT\">");
                htmlD = htmlD.substring(temp+16,htmlD.length());
                temp = htmlD.indexOf("<");
                items[i].description = htmlD.substring(0,temp);
            } catch(Exception e){
                e.printStackTrace();
                items[i].description = "";
            }*/
            //COULD NOT DECIPHER DESCRIPTION!TODO!!
            items[i].description = items[i].name;
			//find next productlink
            temp = html.indexOf("Selecciona para comparar");
            html = html.substring(temp,html.length());
            //next productlink
            temp = html.indexOf("href=\"");
            html = html.substring(temp+6,html.length());
            productlinktemp = html.substring(0,html.indexOf("\""));
            productlinktemp = baseurl+productlinktemp;
            //print to file
            pw.print(items[i].name);
            pw.print("@@@@");
            pw.print(items[i].store);
            pw.print("@@@@");
            pw.print(items[i].price);
            pw.print("@@@@");
            pw.print(items[i].productlink);
            pw.print("@@@@");
            pw.print(items[i].image);
            pw.print("@@@@");
            pw.print(items[i].description);
            pw.print("@@@@");
            pw.print(items[i].upc);
            pw.println("");
            pw.flush();
            //advance index
			i++;
		}
		Product[] result = new Product[i];
		for(int j=0; j<i; j++){
			result[j] = items[j];
		}
		return result;
	}
	
	public static void main(String args[]) throws Exception{
		
		crawl();
		
		/*//test process
		BufferedReader br = new BufferedReader(new FileReader(new File("C:\\Documents and Settings\\Administrador\\Mis documentos\\r&d\\kompramex\\downloads\\Liverpool.htm")));
		//read test file
		String temp = "";
		String html = "";
		while(temp != null){
			temp = br.readLine();
			html += temp;
		}
		//test
		Product[] items = process(html);
		String s = WriteResults.generate("laptop",items);
		//write to file
		try {
			PrintWriter pw = new PrintWriter(new File("test.html"));
			pw.println(s);
			pw.close();
		} catch(Exception e){
			e.printStackTrace();
		}*/
		
	}
}
