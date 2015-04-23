//libraries
import java.io.*;

//crawls sams
public class Sams{

	//for debugging
	public static void saveTextToFile(String text, String file){
		try {
			PrintWriter pw = new PrintWriter(new File(file));
			pw.print(text);
			pw.flush();
			pw.close();
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	//offline crawl
	public static void crawl(){
		
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.sams.com.mx/all-categories.aspx");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//trim
		int i = html.indexOf("CategoriasTodo");
		html = html.substring(i+13,html.length());
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		do {
			//find category urls
			i = html.indexOf("font-size:13px");
			html = html.substring(i+14,html.length());
			i = html.indexOf("location.href=\"");
			html = html.substring(i+15,html.length());
			i = html.indexOf("\"");
			String args = html.substring(0,i);
			//construct URL
			String temp = "http://www.sams.com.mx/";
			temp += args;
			categories[num_categories] = temp;
			//find next category
			num_categories++;
			i = html.indexOf("font-size:13px");
		} while (i>0);
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Sams_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
		}
		//2) for each category page
		for(i=0; i<categories.length; i++){
			try {
				//download page
				String url = "http://www.sams.com.mx/WebControls/hlSearchProducts.ashx?";
				String args = categories[100];
				args = args.substring(args.indexOf("?")+1,args.length());
				String[] array = args.split("&");
				args = "&departamento="+array[0].split("=")[1];
				args += "&familia="+array[1].split("=")[1];
				args += "&FamiliaSub="+array[2].split("=")[1];
				html = URLConnectionReader.getText(url+args);
				System.out.println(url+args);
				
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
				//do dummy requests
				html = URLConnectionReader.getText("http://www.sams.com.mx/WebControls/hlSummaryResults.ashx?"+args);
				html = URLConnectionReader.getText("http://www.sams.com.mx/WebControls/hlBreadcrumb.ashx?"+args);
				html = URLConnectionReader.getText("http://www.sams.com.mx/WebControls/hlGetPricesRange.ashx?"+args);
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
		String url = "http://www.sams.com.mx/Busqueda.aspx?upc=";
		url += text;
		String html = URLConnectionReader.getText(url);
		return process(html);
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("Description\":\"");
		html = html.substring(temp,html.length());
		//find products
		while(temp!=-1){
			html = html.substring(temp+14,html.length());
			items[i] = new Product();
			temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp).trim();System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			temp = html.indexOf("Precio\":\"");
			html = html.substring(temp+9,html.length());
			temp = html.indexOf("\"");
			items[i].price = html.substring(0,temp).trim();System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Sams";
			i++;
			temp = html.indexOf("Description\":\"");
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
