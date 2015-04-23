//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls walmart
public class Famsa {

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
			html = URLConnectionReader.getText("http://www.famsa.com/");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		//1) define categories
		int num_categories = 0;
		String[] categories = new String[1000];
		//trim
		int i = html.indexOf("sub-categorias");
		html = html.substring(i,html.length());
        i = html.indexOf("a href=\"");
		while(i>0){
			html = html.substring(i+8,html.length());
			//find category urls
			i = html.indexOf("\"");
            String possibleUrl = "http://www.famsa.com"+html.substring(0,i);
            if(possibleUrl.endsWith("-")){
                categories[num_categories] = possibleUrl;
                System.out.println(categories[num_categories]);
                num_categories++;
            }
			html = html.substring(i,html.length());
			//find next category
			i = html.indexOf("a href=\"");
			
		}
        System.out.println(num_categories);
        //patch interrupted crawl
        int cont = 0;
        /*for(cont=0;cont<num_categories;cont++){
            if(categories[cont].contains("http://www.walmart.com.mx/Libros/Ocio/Gastronomia")){
                break;
            }
        }
        System.out.println(cont);*/
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Famsa_products.txt"));
		} catch(Exception e){
			e.printStackTrace();
		}
		//2) for each category page
		for(i=cont; i<num_categories; i++){
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
	
	//online search
	public static Product[] search(String text) throws Exception{
		text = text.replace(" ","%20");
		String url = "http://www.walmart.com.mx/Busqueda.aspx?Text=";
		url += text;
		url += "&Departamento=0";
		String html = URLConnectionReader.getText(url);
		return process(html);
	}
	
	//convert html to Items
	public static Product[] process(String html){
		Product[] items = new Product[100];
		int i = 0;
		//trim
		int temp = html.indexOf("BlueknowCategoria");
		html = html.substring(temp+17,html.length());
		//find products
		temp = html.indexOf("\"idProducto\":\"");
		while(temp>0){
            items[i] = new Product();
            //productlink
			html = html.substring(temp+14,html.length());
            String urlp = html.substring(0,html.indexOf("\""));
            items[i].productlink = "http://www.famsa.com/famsa-"+urlp+".html";
            //upc
			temp = html.indexOf("\"sku\":\"");
			html = html.substring(temp+7,html.length());
            String upc = html.substring(0,html.indexOf("\""));System.out.println(upc);
            items[i].upc = upc;
			//name
            temp = html.indexOf("\"nombre\":\"");
            html = html.substring(temp+10,html.length());
			temp = html.indexOf(",");
			items[i].name = html.substring(0, temp-1);System.out.println(items[i].name);
            html = html.substring(temp+1,html.length());
            //price
			temp = html.indexOf("\"precioRegular\":\"");
			html = html.substring(temp+17,html.length());
			temp = html.indexOf("\"");
			items[i].price = html.substring(0,temp);System.out.println(items[i].price);
			html = html.substring(temp+1,html.length());
			//store
            items[i].store = "Famsa";
            //description
            temp = html.indexOf("\"descripcion\":\"");
			html = html.substring(temp+15,html.length());
			temp = html.indexOf("\",\"");
			items[i].description = html.substring(0,temp);
			html = html.substring(temp+1,html.length());
            //download image
            try {
                //check if image already exists
                File f = new File(upc+".jpg");
                if(!f.exists()){
                    //find
                    temp = html.indexOf("\"urlImagen\":\"");
                    html = html.substring(temp+13,html.length());
                    temp = html.indexOf("\"");
                    String urlimage = html.substring(4,temp);
                    urlimage = urlimage.replace("\\/","/");
                    //download
                    URL url = new URL("http://"+urlimage);
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(20000);
                    urlc.setReadTimeout(20000);
                    Image image = ImageIO.read(urlc.getInputStream());
                    ImageIO.write(URLConnectionReader.getRenderedImage(image), "jpg", new File(upc+".jpg"));
                    items[i].image = "imgs/famsa/"+upc+".jpg";
                } else {
                    items[i].image = "imgs/famsa/"+upc+".jpg";
                }
            } catch(Exception e){
                System.out.println("could not download image.");
                items[i].image = "";
            }
			//find next item
			i++;
			temp = html.indexOf("\"idProducto\":\"");
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
