//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls coppel
public class Coppel{

	//for debug purposes
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
			html = URLConnectionReader.getText("http://www.coppel.com/");
		} catch (Exception e){
            System.out.println("Could not fetch category page.");
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		//trim
		int i = html.indexOf("<!-- Terminan promociones-->");
		html = html.substring(i+28,html.length());
        //find first category
        i = html.indexOf("ul class=\"false\"");
        //find categories
		while(i>0){
			html = html.substring(i+16,html.length());
			//find category urls
			i = html.indexOf("a href=\"");
			html = html.substring(i+8,html.length());
			i = html.indexOf("\"");
            String possibleUrl = "http://www.coppel.com"+html.substring(0,i);
            categories[num_categories] = possibleUrl;
            System.out.println(categories[num_categories]);
            num_categories++;
			html = html.substring(i,html.length());
			//find next category
			i = html.indexOf("ul class=\"false\"");
		}
        System.out.println(num_categories);
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Coppel_products.txt"));
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
		int temp = html.indexOf("div_li_wrap");
		html = html.substring(temp+11,html.length());
		//find products
		temp = html.indexOf("div_li_wrap");
		while(temp>0){
			html = html.substring(temp+11,html.length());
			items[i] = new Product();
            //url
			temp = html.indexOf("location.href=\"");
            html = html.substring(temp+15,html.length());
            temp = html.indexOf("\"");
			items[i].productlink = "http://www.coppel.com"+html.substring(0, temp);System.out.println(items[i].productlink);
			html = html.substring(temp+1,html.length());
            //name
            temp = html.indexOf("title_art\">");
            html = html.substring(temp+11,html.length());
            temp = html.indexOf("<");
			items[i].name = html.substring(0, temp);System.out.println(items[i].name);
			html = html.substring(temp+1,html.length());
            //store
            items[i].store = "Coppel";
            //image
            temp = html.indexOf("img src='");
            html = html.substring(temp+9,html.length());
            temp = html.indexOf("'");
            String imgurl = html.substring(0, temp);
            html = html.substring(temp+1,html.length());
            String filename = imgurl.substring(imgurl.lastIndexOf("/")+1,imgurl.length());
            try {
                if(!(new File(filename).exists())){
                    URL url = new URL(imgurl);
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(20000);
                    urlc.setReadTimeout(20000);
                    Image image = ImageIO.read(urlc.getInputStream());
                    ImageIO.write(URLConnectionReader.getRenderedImage(image), "jpg", new File(filename));
                    items[i].image = "imgs/coppel/"+filename;
                } else {
                    items[i].image = "imgs/coppel/"+filename;
                }
            } catch(Exception e){
                System.out.println("Could not download image.");
                items[i].image = "";
                e.printStackTrace();
            }
            //price
            temp = html.indexOf("Contado:");
            html = html.substring(temp+8,html.length());
            temp = html.indexOf("$");
            html = html.substring(temp,html.length());
            temp = html.indexOf("<");
            items[i].price = html.substring(0, temp);System.out.println(items[i].price);
            html = html.substring(temp+1,html.length());
            //no description, no upc
            items[i].description = items[i].name;
            items[i].upc = filename.substring(filename.indexOf("-")+1,filename.lastIndexOf("-"));
			//find next product
            temp = html.indexOf("div_li_wrap");
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
