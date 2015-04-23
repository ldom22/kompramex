package search;

//libraries
import java.io.*;

//crawls walmart
public class Walmart{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.walmart.com.mx/Categorias-Todas.aspx");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		int i = html.indexOf("color:#6E6E6E;");
		while(i>0){
			html = html.substring(i+14,html.length());
			//find category urls
			i = html.indexOf("location.href=\"");
			html = html.substring(i+15,html.length());
			i = html.indexOf("\"");
			categories[num_categories] = "http://www.walmart.com.mx/"+html.substring(0,i);
			html = html.substring(i,html.length());
			//find next category
			num_categories++;
			i = html.indexOf("color:#6E6E6E;");
			
		}
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Walmart_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
		}
		//2) for each category page
		for(i=0; i<categories.length; i++){
			try {
				//download page
				html = URLConnectionReader.getText(categories[i]);
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
			}
		}
		//close file
		pw.close();
	}
	
	//online search
	public static Product[] search(String text) throws Exception{
		try {
			text = text.replace(" ","%20");
			String url = "http://www.walmart.com.mx/Busqueda.aspx?Text=";
			url += text;
			url += "&Departamento=0";
			String html = URLConnectionReader.getText(url);
			return process(html);
		} catch (Exception e){
			e.printStackTrace();
			return new Product[1];
		}
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("/<![CDATA[var ServerDataProducts=");
		html = html.substring(temp,html.length());
		//find products
		temp = html.indexOf("\"Description\":\"");
		while(temp>0){
			html = html.substring(temp+15,html.length());
			items[i] = new Product();
			temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp);//System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			temp = html.indexOf("\"Precio\":\"");
			html = html.substring(temp+10,html.length());
			temp = html.indexOf("\"");
			items[i].price = html.substring(0,temp);//System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Walmart";
			//find product url
			temp = html.indexOf("\"upc\":\"");
			html = html.substring(temp+7,html.length());
			items[i].productlink = "http://www.walmart.com.mx/Detalle-del-articulo/"+html.substring(0,html.indexOf("\""))+"/a";
			//find next item
			i++;
			temp = html.indexOf("\"Description\":\"");
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