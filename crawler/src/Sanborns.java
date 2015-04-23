//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls sanborns
public class Sanborns{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.sanborns.com.mx/Paginas/Inicio.aspx");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[200];
		int i = html.indexOf("Familia.aspx");
		while(i>0){
			html = html.substring(i,html.length());
			int j = html.indexOf("\"");
			categories[num_categories] = "http://www.sanborns.com.mx/Paginas/"+html.substring(0,j);
			html = html.substring(j,html.length());
			//find next category
			num_categories++;
			i = html.indexOf("Familia.aspx");
		}
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Sanborns_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
		}
		//2) for each category page
		for(i=0; i<num_categories; i++){
			try {
				//download page
				html = URLConnectionReader.getText(categories[i]);
				//process
				Product[] temp = process(html);
				//detect num of pages
				//TDDO
				//for each page
				/*for(int j=0; j<10; j++){
					html = URLConnectionReader.getText(categories[i]);
					temp = process(html);
					//add to array
				}*/
				//write to file
				for(Product product : temp){
					pw.print(product.name);
					pw.print("@@@@");
					pw.print(product.store);
					pw.print("@@@@");
					pw.print(product.price);
					pw.print("@@@@");
					pw.print(product.productlink);
                    pw.print("@@@@");
                    pw.print(product.image);
                    pw.print("@@@@");
                    pw.print(product.description);
                    pw.print("@@@@");
                    pw.print(product.upc);
					pw.println("");
					pw.flush();
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		//close file
		pw.close();
	}
	
	//online search
	public static Product[] search(String text) throws Exception{
		text = text.replace(" ","%20");
		String url = "http://www.sanborns.com.mx/Paginas/Busqueda.aspx?q=";
		url += text;
		String html = URLConnectionReader.getText(url);
		return process(html);
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//find first item
		int temp = html.indexOf("<div class=\"producto\">");
		//find products
		while(temp>0){
			//log new product
			items[i] = new Product();
			//find product link
			temp = html.indexOf("<div class=\"producto\">");
			html = html.substring(temp,html.length());
			temp = html.indexOf("a href=\"");
			html = html.substring(temp+8,html.length());
			String baseurl = "http://www.sanborns.com.mx/Paginas/";
			String productlink = baseurl+html.substring(0,html.indexOf("\""));
			System.out.println(productlink);
			items[i].productlink = productlink;
			//find product name
			temp = html.indexOf("<div class=\"desc\"><b>");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</b>");
			items[i].name = html.substring(21, temp);System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			//find product price
			temp = html.indexOf("#F00'>");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</span>");
			items[i].price = html.substring(6,temp).trim();System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			//add store information
			items[i].store = "Sanborns";
            //download description
            try {
                String htmlD = URLConnectionReader.getText(items[i].productlink);
                temp = htmlD.indexOf("Descripción");
                htmlD = htmlD.substring(temp+9,htmlD.length());
                temp = htmlD.indexOf("<");
                items[i].description = htmlD.substring(0,temp);
                items[i].upc = items[i].productlink.substring(items[i].productlink.indexOf("=")+1,items[i].productlink.length());
            } catch(Exception e){
                e.printStackTrace();
                items[i].description = "";
            }
            //download image
            try {
                if(!(new File(items[i].upc+".jpg").exists())){
                    URL url = new URL("http://www.sanborns.com.mx/img/240/"+items[i].upc+".jpg");
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(20000);
                    urlc.setReadTimeout(20000);
                    Image image = ImageIO.read(urlc.getInputStream());
                    ImageIO.write(URLConnectionReader.getRenderedImage(image), "jpg", new File(items[i].upc+".jpg"));
                }
                items[i].image = "imgs/sanborns/"+items[i].upc+".jpg";
            } catch(Exception e){
                e.printStackTrace();
                items[i].image = "";
            }
			//check if there are more products
			temp = html.indexOf("<div class=\"desc\"><b>");
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
	}
}