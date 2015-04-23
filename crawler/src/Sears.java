//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls walmart
public class Sears{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.sears.com.mx/");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[200];
		int i = html.indexOf("categoria");
		while(i>0){
			html = html.substring(i,html.length());
			int j = html.indexOf("\"");
			categories[num_categories] = "http://www.sears.com.mx/"+html.substring(0,j);
			System.out.println(categories[num_categories]);
			html = html.substring(j,html.length());
			//find next category
			num_categories++;
			i = html.indexOf("categoria");
		}
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Sears_products.txt"));
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
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("<!-- TERMINA ORACLE RECOMENDATION -->");
		html = html.substring(temp,html.length());
		//find products
		temp = html.indexOf("class=\"producto_nombre\">");
		while(temp>0){
			items[i] = new Product();
			//find product link
			temp = html.indexOf("<div class=\"info\">");
			html = html.substring(temp+18,html.length());
			temp = html.indexOf("a href=\"");
			html = html.substring(temp+8,html.length());
			items[i].productlink = "http://www.sears.com.mx/producto/"+html.substring(0,html.indexOf("\""));
			System.out.println(items[i].productlink);
			//find name and price
			temp = html.indexOf("class=\"producto_nombre\">");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</a>");
			items[i].name = html.substring(24, temp);System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			temp = html.indexOf("Precio Internet:");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</div>");
			items[i].price = html.substring(16,temp).trim();System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Sears";
            //download description
            String htmlD = "";
            try {
                String descHTML = URLConnectionReader.getGText(items[i].productlink);
                htmlD = descHTML;
                int indexOfD = descHTML.indexOf("<!-- INICIA DESCRIPCION LARGA -->");
                descHTML = descHTML.substring(indexOfD,descHTML.length());
                indexOfD = descHTML.indexOf("<p>");
                descHTML = descHTML.substring(indexOfD+3,descHTML.length());
                items[i].description = descHTML.substring(0,descHTML.indexOf("<"));
            } catch(Exception e){
                System.out.println("Could not download description.");
                e.printStackTrace();
                items[i].description = "";
            }
            //product code TODO get real code
            String upc = items[i].productlink;
            upc = upc.substring(0,upc.length()-1);
            upc = upc.substring(upc.lastIndexOf("/"),upc.length());
            //download image
            try {
                if(!(new File(upc+".jpg").exists())){
                    int indexOfD = htmlD.indexOf("img src");
                    htmlD = htmlD.substring(indexOfD+9,htmlD.length());
                    indexOfD = htmlD.indexOf("\"");
                    htmlD = htmlD.substring(0,indexOfD);
                    System.out.println(htmlD);
                    //URLConnectionReader.saveImage(htmlD,upc+".jpg");
                }
                items[i].image = htmlD;
            } catch(Exception e){
                System.out.println("could not download image.");
                e.printStackTrace();
                items[i].image = "";
            }
            //next item
			i++;
			temp = html.indexOf("class=\"producto_nombre\">");
		}
		Product[] result = new Product[i];
		for(int j=0; j<i; j++){
			result[j] = items[j];
		}
		return result;
	}
	
	//online search
	public static Product[] search(String text) throws Exception{
		text = text.replace(" ","+");
		String url = "http://www.sears.com.mx/buscar/?c=";
		url += text;
		String html = URLConnectionReader.getText(url);
		return process(html);
	}
	
	public static void main(String args[]) throws Exception{
		crawl();
	}
}
