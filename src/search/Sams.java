package search;

//libraries
import java.io.*;

//crawls sams
public class Sams{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://www.sams.com.mx/Inicio.aspx?Negocio=Individual");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//trim
		int i = html.indexOf("MenuSearching");
		html = html.substring(i+13,html.length());
		i = html.indexOf("MenuSearching");
		html = html.substring(i+13,html.length());
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		i = html.indexOf("MenuSearching");
		while(i>0){
			html = html.substring(i+13,html.length());
			//find category urls
			i = html.indexOf(";");
			String args = html.substring(0,i);
			html = html.substring(i,html.length());
			args = args.replace("(","");
			args = args.replace("\"","");
			args = args.replace(")","");
			String[] args_array = args.split(",");
			//construct URL
			String temp = "http://www.sams.com.mx/Busqueda.aspx?Departamento=";
			temp += args_array[0];
			temp += "&Familia=";
			temp += args_array[1];
			temp += "&FamiliaSub=";
			temp += args_array[2];
			categories[num_categories] = temp;
			//find next category
			num_categories++;
			i = html.indexOf("MenuSearching");
		}
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
			String url = "http://www.sams.com.mx/Busqueda.aspx?upc=";
			url += text;
			String html = URLConnectionReader.getText(url);
			System.out.println(html);
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
		int temp = html.indexOf("resultadosMosaico");
		html = html.substring(temp,html.length());
		//find products
		temp = html.indexOf("pnlItemContent");
		html = html.substring(temp,html.length());
		temp = html.indexOf("Gray;\">");
		while(temp>0){
			html = html.substring(temp,html.length());
			items[i] = new Product();
			temp = html.indexOf("</a>");
			items[i].name = html.substring(8, temp).trim();//System.out.println(items[i].name);
			html = html.substring(temp,html.length());
			temp = html.indexOf("pnlPriceContent");
			html = html.substring(temp,html.length());
			temp = html.indexOf("<span>");
			html = html.substring(temp+6,html.length());
			temp = html.indexOf("</span>");
			items[i].price = html.substring(0,temp).trim();//System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Sams";
			i++;
			temp = html.indexOf("Gray;\">");
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