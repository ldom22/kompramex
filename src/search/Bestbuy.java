package search;

//libraries
import java.io.*;

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
					pw.println("");
					pw.flush();
				}
			} catch(Exception e){
				e.printStackTrace();
				pw.close();
				return;
			}
		}
		//close file
		pw.close();
	}
	
	//online search
	public static Product[] search(String text) throws Exception{
		try{
			text = text.replace(" ","+");
			String url = "https://www.bestbuy.com.mx/catalogsearch/result/?q=";
			url += text;
			String html = URLConnectionReader.getSecureText(url);
			return process(html);
		} catch(Exception e){
			e.printStackTrace();
			return new Product[1];
		}
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
			html = html.substring(temp+7,html.length());
			temp = html.indexOf("\"");
			items[i].productlink = html.substring(0,temp);
			//get product name
			temp = html.indexOf("title=\"");
			html = html.substring(temp+7,html.length());
			temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp);//System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			//get price
			temp = html.indexOf("$");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</span");
			items[i].price = html.substring(0,temp).trim();
			items[i].price = "$"+items[i].price.substring(3);
			//System.out.println(items[i].price);
			html = html.substring(temp,html.length());
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