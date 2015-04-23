//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls costco
public class Costco{
	
	//offline crawl
	public static void crawl(){
		//0) download main page
		String html;
		try {
			html = URLConnectionReader.getText("http://costco.com.mx/");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		int i = html.indexOf("<ul class=\"grandchildren\">");
		html = html.substring(i+26,html.length());
		while(i>0){
			//find category urls
			int j = html.indexOf("a href=\"");
			html = html.substring(j+8,html.length());
			while(j>0){
				categories[num_categories] = "http://costco.com.mx"+html.substring(0,html.indexOf("\""));
				//find next category
				num_categories++;
				j = html.indexOf("a href=\"");
				html = html.substring(j+8,html.length());
			}
			//find next category
			i = html.indexOf("<ul class=\"grandchildren\">");
			html = html.substring(i+26,html.length());
		}
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Costco_products.txt"));
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
				//TODO
				/*//for each page
				for(int j=0; j<10; j++){
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
		text = text.replace(" ","+");
		String url = "http://costco.com.mx/view/search?sort=score&search=";
		url += text;
		String html = URLConnectionReader.getText(url);
		return process(html);
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("<div class=\"productList_name\">");
		html = html.substring(temp,html.length());
		//find products
		while(temp>0){
			items[i] = new Product();
			//get product link
			temp = html.indexOf("href=\"");
			html = html.substring(temp+7,html.length());
			temp = html.indexOf("\"");
			items[i].productlink = "http://costco.com.mx/"+html.substring(0,temp);
			//get product name
			temp = html.indexOf("\">");
			html = html.substring(temp+2,html.length());
			temp = html.indexOf("<");
			if(temp==0){
				temp = html.indexOf(">");
				html = html.substring(temp+6,html.length());
				temp = html.indexOf("<a class=\"productList_a\"");
				html = html.substring(temp+34,html.length());
				temp = html.indexOf("\">");
				html = html.substring(temp+2,html.length());
				temp = html.indexOf("<");
			}
			items[i].name = html.substring(0, temp);System.out.println(items[i].name);
			
			html = html.substring(temp,html.length());
			temp = html.indexOf("$");
			html = html.substring(temp,html.length());
			temp = html.indexOf("</a");
			items[i].price = html.substring(0,temp).trim();System.out.println(items[i].price);
			html = html.substring(temp,html.length());
			items[i].store = "Costco";
            //download description
            String htmlD = "";
            try {
                String descHTML = URLConnectionReader.getText(items[i].productlink);
                htmlD = descHTML;
                int indexOfD = descHTML.indexOf("\"productDetail_tab2");
                descHTML = descHTML.substring(indexOfD,descHTML.length());
                indexOfD = descHTML.indexOf("<p>");
                descHTML = descHTML.substring(indexOfD+3,descHTML.length());
                items[i].description = descHTML.substring(0,descHTML.indexOf("<"));
            } catch(Exception e){
                System.out.println("Could not download description.");
                items[i].description = "";
            }
            //product code
            items[i].upc = items[i].productlink.substring(items[i].productlink.lastIndexOf("/")+1,items[i].productlink.length());
            //download image
            try {
                int indexOfD = htmlD.indexOf("medias");
                htmlD = htmlD.substring(indexOfD,htmlD.length());
                indexOfD = htmlD.indexOf(".jpg");
                htmlD = htmlD.substring(0,indexOfD);
                String url = "http://www.costco.com.mx/"+htmlD+".jpg";
                System.out.println(url);
                File f = new File(items[i].upc+".jpg");
                if(!f.exists()){
                    URLConnectionReader.saveImage(url,items[i].upc+".jpg");
                    items[i].image = "imgs/costco/"+items[i].upc+".jpg";
                } else {
                    items[i].image = "imgs/costco/"+items[i].upc+".jpg";
                }
            } catch(Exception e){
                System.out.println("could not download image.");
                items[i].image = "";
            }
			i++;
			temp = html.indexOf("<a class=\"productList_a\"");
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
		BufferedReader br = new BufferedReader(new FileReader(new File("\\downloads\\categories_crawl\\costco\\main.txt")));
		//read test file
		String temp = "";
		String html = "";
		while(temp != null){
			temp = br.readLine();
			html += temp;
		}
		//test
		Product[] items = process(html);
		String s = WriteResults.generate("laptop",items);*/
		
		//crawl
		
		/*//write to file
		try {
			PrintWriter pw = new PrintWriter(new File("test.html"));
			pw.println(s);
			pw.close();
		} catch(Exception e){
			e.printStackTrace();
		}*/
		
	}
}
