package search;

//libraries
import java.io.*;

//crawls liverpool
public class Liverpool{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.liverpool.com.mx/shopping/store/");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		//find category urls
		int i = html.indexOf("master_menu_container");
		while(i>0){
			html = html.substring(i,html.length());
			//find category urls
			i = html.indexOf("shopping/content/browse/_/");
			html = html.substring(i,html.length());
			i = html.indexOf("\"");
			categories[num_categories] = "http://www.liverpool.com.mx/"+html.substring(0,i);
			html = html.substring(i,html.length());
			//find next category
			num_categories++;
			i = html.indexOf("shopping/content/browse/_/");
		}
		System.out.println("num categorias:"+num_categories);
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Liverpool_products.txt"));
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
				//TOFO
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
	
	//online search
	public static Product[] search(String text) throws Exception{
		try {
			text = text.replace(" ","+");
			String url = "http://www.liverpool.com.mx/shopping/content/search?Dy=1&Nty=1&N=0&Ntt=";
			url += text;
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
		int temp = html.indexOf("<!-- Hide comparison for only one product -->");
		html = html.substring(temp,html.length());
		//first productlink
		temp = html.indexOf("href=\"");
		html = html.substring(temp+6,html.length());
		String productlinktemp = html.substring(0,html.indexOf("\""));
		String baseurl = "http://www.liverpool.com.mx";
		productlinktemp = baseurl+productlinktemp;
		//find products
		while(temp>0){
			temp = html.indexOf("<h2 class=\"nombre-producto-modulo\">");
			html = html.substring(temp,html.length());
			items[i] = new Product();
			items[i].productlink = productlinktemp;
			temp = html.indexOf("</h2>");
			items[i].name = html.substring(35, temp);//System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			temp = html.indexOf("<p class=\"precio-modulo\">");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</p>");
			items[i].price = html.substring(25,temp).trim();//System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Liverpool";
			//find next productlink
			temp = html.indexOf("shop.jsp");
			html = html.substring(temp+8,html.length());
			productlinktemp = html.substring(0,html.indexOf("\""));
			baseurl = "http://www.liverpool.com.mx/shopping/store/shop.jsp";
			productlinktemp = baseurl+productlinktemp;
			//System.out.println(productlinktemp);
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