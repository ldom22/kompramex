//libraries
import java.io.*;
import javax.imageio.*;
import java.awt.*;
import java.net.*;

//crawls walmart
public class WalmartSuper{

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
			html = URLConnectionReader.getText("http://www.walmart.com.mx/super");
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
        //precategories
        String[] precat = {
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Despensa",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Lacteos-y-Huevo",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Bebidas",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Farmacia",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Bebes",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Carnes-y-Pescados",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Limpieza-y-Mascotas",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Frutas-y-Verduras",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Higiene-y-Belleza",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Congelados",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Salchichoneria",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Vinos-y-Licores",
        "http://www.walmart.com.mx/super/Categoria.aspx?Departamento=d-Panaderia-y-tortilleria"};
        
        //1) define categories
        int num_categories = 0;
        String[] categories = new String[1000];

        for(int k=0; k<precat.length; k++){
            //download page
            try {
                html = URLConnectionReader.getText(precat[k]);
            } catch (Exception e){
                e.printStackTrace();
                return;
            }
            //trim
            int i = html.indexOf("Departamentos</span>");
            html = html.substring(i+20,html.length());
            //find first one
            i = html.indexOf("Departamentos</span>");
            html = html.substring(i+20,html.length());
            while(i>0){
                html = html.substring(i+15,html.length());
                //find category urls
                i = html.indexOf("size:12px;font-weight:bold;padding-top:5px;'  href='");
                html = html.substring(i+52,html.length());
                i = html.indexOf("'");
                String possibleUrl = "http://www.walmart.com.mx/"+html.substring(0,i);
                categories[num_categories] = possibleUrl;
                System.out.println(categories[num_categories]);
                num_categories++;
                html = html.substring(i,html.length());
                //find next category
                i = html.indexOf("font-size:12px;");
			
            }
        }
        System.out.println(num_categories);
        //patch interrupted crawl
        int cont = 0;
        for(cont=0;cont<num_categories;cont++){
            if(categories[cont].contains("http://www.walmart.com.mx/Libros/Ocio/Gastronomia")){
                break;
            }
        }
        System.out.println(cont);
		//write to file
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new File("Walmart_products.txt"));
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
		int temp = html.indexOf("var ServerDataProducts= '[{");
		html = html.substring(temp+27,html.length());
		//find products
		temp = html.indexOf("\"UrlDescription\":\"");
		while(temp>0){
			//name
            html = html.substring(temp+18,html.length());
			items[i] = new Product();
			temp = html.indexOf("\"");
			items[i].name = html.substring(0, temp);System.out.println(items[i].name);
            html = html.substring(temp+1,html.length());
            //price
			temp = html.indexOf("\"Precio\":\"");
			html = html.substring(temp+10,html.length());
			temp = html.indexOf("\"");
			items[i].price = html.substring(0,temp);System.out.println(items[i].price);
			html = html.substring(temp+1,html.length());
			//store
            items[i].store = "Walmart";
			//upc
			temp = html.indexOf("\"upc\":\"");
			html = html.substring(temp+7,html.length());
            String upc = html.substring(0,html.indexOf("\""));System.out.println(upc);
            items[i].upc = upc;
            //productlink
			temp = html.indexOf("\"ProductUrl\":\"");
			html = html.substring(temp+14,html.length());
            String urlp = html.substring(0,html.indexOf("\""));
            items[i].productlink = "http://www.walmart.com.mx/"+urlp;
            //download image
            try {
                //check if image already exists
                File f = new File(upc+".jpg");
                if(!f.exists()){
                    URL url = new URL("http://www.walmart.com.mx/images/products/img_medium/"+upc+"m.jpg");
                    URLConnection urlc = url.openConnection();
                    urlc.setConnectTimeout(20000);
                    urlc.setReadTimeout(20000);
                    Image image = ImageIO.read(urlc.getInputStream());
                    ImageIO.write(URLConnectionReader.getRenderedImage(image), "jpg", new File(upc+".jpg"));
                    items[i].image = "imgs/walmart/"+upc+".jpg";
                }
            } catch(Exception e){
                System.out.println("could not download image.");
                items[i].image = "";
            }
            //get description
            try {
                String descHTML = URLConnectionReader.getText(items[i].productlink);
                int indexOfD = descHTML.indexOf("<h2 id=\"h2Caracteristicas\" style=\"display: none;\">");
                descHTML = descHTML.substring(indexOfD,descHTML.length());
                indexOfD = descHTML.indexOf(">");
                descHTML = descHTML.substring(indexOfD+1,descHTML.length());
                items[i].description = descHTML.substring(0,descHTML.indexOf("<"));
            } catch(Exception e){
                System.out.println("Could not download description.");
                items[i].description = "";
            }
			//find next item
			i++;
			temp = html.indexOf("\"UrlDescription\":\"");
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
