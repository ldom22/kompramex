//libraries
import java.io.*;

//used to generate results page
public class WriteResults {
	//
	public static String generate(String search, Product items[]){
		String resultHTML = "";
		//static content
		resultHTML += "<html>";
		
		resultHTML += "<head>";
		resultHTML += "<title>Kompramex - "+search+"</title>";
		resultHTML += "</head>";
		
		resultHTML += "<style type=\"text/css\">";
		resultHTML += "<!--";
		resultHTML += "A:link{text-decoration:none}";
		resultHTML += "A:visited{text-decoration:none}";
		resultHTML += "A:active{text-decoration:none}";
		resultHTML += "A:hover{text-decoration:underline}";
		resultHTML += "-->";
		resultHTML += "</style>";
		
		resultHTML += "<body>";
		
		resultHTML += "<table width = \"100%\" bordercolor=\"#EFEFEF\" border=\"10\" bgcolor=\"#EFEFEF\"";
		resultHTML += "style=\"top:0;left:0;right:0;position:absolute;\">";
		resultHTML += "<tr><td>";
		resultHTML += "<p style=\"color:#7B88E8\"><b>Kompramex</b> <input size=\"50\"></input><button>Buscar</button></p>";
		resultHTML += "<tr><td>";
		resultHTML += "</table>";
		
		resultHTML += "<br><br><br>";
		
		resultHTML += "<p>Resultados de "+search+"</p>";
		
		resultHTML += "<br>";
		
		//if no results
		if(items.length==0){
			resultHTML += "<b>No se encontraron resultados</b>";
			resultHTML += "<br><br><br>";
		} else {
		
			resultHTML += "<table width=\"70%\">";
			resultHTML += "<tr>";
			resultHTML += "<th>Producto</th>";
			resultHTML += "<th>Tienda</th>";
			resultHTML += "<th>Precio</th>";
			resultHTML += "</tr>";
			//dynamic content
			for(int i=0; i<items.length; i++){
				if(items[i]!=null){
					resultHTML += "<tr>";
					resultHTML += "<td><center><a href=\""+items[i].productlink+"\" target=\"_blank\">"+items[i].name+"</a></center></td>";
					resultHTML += "<td><center><a href=\""+items[i].getStoreLink()+"\" target=\"_blank\"><image src=\""+items[i].getStoreImage()+"\"></a></center></td>";
					resultHTML += "<td><center>"+items[i].price+"</center></td>";
					resultHTML += "</tr>";
				}
			}
			//static content
			resultHTML += "</table>";
		}
		
		resultHTML += "<table width = \"100%\" bordercolor=\"#EFEFEF\" border=\"10\" bgcolor=\"#EFEFEF\">";
		resultHTML += "	<tr>";
		resultHTML += "		<td><a href=\"test.html\">Acerca de Kompramex</a></td>";
		resultHTML += "		<td><a href=\"test.html\">Danos tu opinion</a></td>";
		resultHTML += "		<td><a href=\"test.html\">Contacto</a></td>";
		resultHTML += "	</tr>";
		resultHTML += "</table>";
		
		resultHTML += "</body>";
		resultHTML += "</html>";
		//return page
		return resultHTML;
	}
	
	//test
	public static void main(String args[]){
		//test array
		Product items[] = new Product[4];
		items[0] = new Product("Apple Ipod Touch 32 gb","Costco","$999");
		items[1] = new Product("Apple Ipod Touch 32 gb","Liverpool","$1,200");
		items[2] = new Product("Apple Ipod Touch 32 gb","Sams","$1,100");
		items[3] = new Product("Apple Ipod Touch 32 gb","Sears","$800");
		items[3] = new Product("Apple Ipod Touch 32 gb","Sanborns","$800");
		items[3] = new Product("Apple Ipod Touch 32 gb","Walmart","$800");
		//generate result page
		String html = generate("ipod",items);
		//write to html
		try {
			PrintWriter pw = new PrintWriter(new File("test.html"));
			pw.println(html);
			pw.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}