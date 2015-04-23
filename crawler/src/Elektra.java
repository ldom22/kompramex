//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls coppel
public class Elektra{

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
			html = URLConnectionReader.getText("http://www.elektra.com.mx/elektra/Default.aspx?IndGle=TV,%20Video%20y%20Audio-Linea%20Blanca-Telefonia-Electrodomesticos-Muebles%20Para%20Armar");
		} catch (Exception e){
            System.out.println("Could not fetch category page.");
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		//trim
		int i = html.indexOf("blockSpaceCH");
		html = html.substring(i+12,html.length());
        i = html.indexOf("blockSpaceCH");
		html = html.substring(i+12,html.length());
        //find first category
        i = html.indexOf("href='");
        //find categories
		while(i>0){
			html = html.substring(i+6,html.length());
			//find category urls
			i = html.indexOf("'");
            String possibleUrl = html.substring(0,i);
            if(possibleUrl.contains("ArmaConsulta.aspx")){
                categories[num_categories] = possibleUrl;
                System.out.println(categories[num_categories]);
                num_categories++;
                html = html.substring(i,html.length());
            }
			//find next category
			i = html.indexOf("href='");
		}
        System.out.println(num_categories);
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Elektra_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
		}
		//2) for each category page
		for(i=0; i<num_categories; i++){
			try {
				//download page
				html = URLConnectionReader.getText(categories[i].replace(" ","%20"));
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
		//find products
		int temp = html.indexOf("class=\"producto\"");
		while(temp>0){
			html = html.substring(temp+16,html.length());
			items[i] = new Product();
            //url
			temp = html.indexOf("a href=\"");
            html = html.substring(temp+8,html.length());
            temp = html.indexOf("\"");
			items[i].productlink = "http://www.elektra.com.mx/Elektra"+html.substring(0, temp);System.out.println(items[i].productlink);
			html = html.substring(temp+1,html.length());
            //name
            temp = html.indexOf("img title=\"");
            html = html.substring(temp+11,html.length());
            temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp);System.out.println(items[i].name);
			html = html.substring(temp+1,html.length());
            //store
            items[i].store = "Elektra";
            //image
            temp = html.indexOf("src=\"");
            html = html.substring(temp+5,html.length());
            temp = html.indexOf("\"");
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
                    items[i].image = "imgs/elektra/"+filename;
                } else {
                    items[i].image = "imgs/elektra/"+filename;
                }
            } catch(Exception e){
                System.out.println("Could not download image.");
                items[i].image = "";
                e.printStackTrace();
            }
            //descripcion
            temp = html.indexOf("class=\"descripcion\">");
            html = html.substring(temp+20,html.length());
            temp = html.indexOf("<");
			items[i].description = html.substring(0, temp);
			html = html.substring(temp+1,html.length());
            //price
            temp = html.indexOf("class='precio'>");
            html = html.substring(temp+15,html.length());
            temp = html.indexOf("<");
			items[i].description = html.substring(0, temp);
			html = html.substring(temp+1,html.length());
            //upc
            items[i].upc = filename.substring(0,filename.indexOf("."));
			//find next product
            temp = html.indexOf("class=\"producto\"");
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
