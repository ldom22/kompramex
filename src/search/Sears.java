package search;

//libraries
import java.io.*;

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
					pw.println("@@@@");
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
			items[i].productlink = "http://www.sears.com.mx"+html.substring(0,html.indexOf("\""));
			System.out.println(items[i].productlink);
			//find name and price
			temp = html.indexOf("class=\"producto_nombre\">");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</a>");
			items[i].name = html.substring(24, temp);//System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			temp = html.indexOf("Precio Internet ");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</div>");
			items[i].price = html.substring(16,temp).trim();//System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Sears";
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
		try {
			text = text.replace(" ","+");
			String url = "http://www.sears.com.mx/buscar/?c=";
			url += text;
			String html = URLConnectionReader.getText(url);
			return process(html);
		} catch (Exception e){
			e.printStackTrace();
			return new Product[1];
		}
	}
	
	public static void main(String args[]) throws Exception{
		crawl();
	}
}