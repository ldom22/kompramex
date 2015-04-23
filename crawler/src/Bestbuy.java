//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls bestbuy
public class Bestbuy{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getSecureText("https://www.bestbuy.com.mx");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		int i = html.indexOf("itemMenuName level2");
		while(i>0){
			html = html.substring(i+19,html.length());
			//find category urls
			i = html.indexOf("href=\"");
			html = html.substring(i+6,html.length());
			categories[num_categories] = html.substring(0,html.indexOf("\""));
			System.out.println(categories[num_categories]);
			//find next category
			num_categories++;
			i = html.indexOf("itemMenuName level2");;
		}
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Bestbuy_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
		}
		//2) for each category page
		for(i=0; i<num_categories; i++){
			try {
				//download page
				html = URLConnectionReader.getSecureText(categories[i]);
                System.out.println(categories[i]);
				//process
				Product[] temp = process(html);			
				//detect num of pages
				//TODO
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
				return;
			}
		}
		//close file
		pw.close();
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("<!--Imagen producto-->");
		//find products
		while(temp>0){
			items[i] = new Product();
			html = html.substring(temp,html.length());
			//get product link
			temp = html.indexOf("href=\"");
			html = html.substring(temp+6,html.length());
			temp = html.indexOf("\"");
			items[i].productlink = html.substring(0,temp);
			//get product name
			temp = html.indexOf("title=\"");
			html = html.substring(temp+7,html.length());
			temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp);System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			//get price
			temp = html.indexOf("$");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</span");
			items[i].price = html.substring(1,temp).trim();
			items[i].price = "$"+items[i].price.substring(1);
			System.out.println(items[i].price);
			html = html.substring(temp,html.length());
            //download item page
            String htmlDB = "";
            try {
                //description
                String htmlD = URLConnectionReader.getSecureText(items[i].productlink);
                htmlDB = htmlD;
                int tempD = htmlD.indexOf("<!--<h2>Vista General Rápida</h2>-->");
                htmlD = htmlD.substring(tempD+36,htmlD.length());
                tempD = htmlD.indexOf(">");
                htmlD = htmlD.substring(tempD+1,htmlD.length());
                items[i].description = htmlD.substring(0,htmlD.indexOf("<"));
            } catch(Exception e){
                items[i].description = "";
                e.printStackTrace();
            }
            //download image
            try {
                int tempD = htmlDB.indexOf("<!-- //jQuery LightBoxes -->");
                htmlDB = htmlDB.substring(tempD+28,htmlDB.length());
                tempD = htmlDB.indexOf("<!-- //jQuery LightBoxes -->");
                htmlDB = htmlDB.substring(tempD+28,htmlDB.length());
                tempD = htmlDB.indexOf("https");
                htmlDB = htmlDB.substring(tempD,htmlDB.length());
                htmlDB = htmlDB.substring(tempD,htmlDB.indexOf("\""));
                URL url = new URL("http://www."+htmlDB);
                htmlDB = htmlDB.substring(htmlDB.lastIndexOf("/")+1,htmlDB.length());
                htmlDB = htmlDB.substring(0,htmlDB.length()-6);
                items[i].upc = htmlDB;
                File f = new File(htmlDB+".jpg");
                if(!f.exists()){
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(20000);
                    urlc.setReadTimeout(20000);
                    Image image = ImageIO.read(urlc.getInputStream());
                    ImageIO.write(URLConnectionReader.getRenderedImage(image), "jpg", new File(htmlDB+".jpg"));
                    items[i].image = "imgs/bestbuy/"+htmlDB;
                } else {
                    items[i].image = "imgs/bestbuy/"+htmlDB;
                }
            } catch(Exception e){
                items[i].image = "";
                e.printStackTrace();
            }
			//find next item
			items[i].store = "Bestbuy";
			i++;
			temp = html.indexOf("<!--Imagen producto-->");
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
		BufferedReader br = new BufferedReader(new FileReader(new File("downloads\\categories_crawl\\bestbuy\\laptops.html")));
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
