package search;

import java.text.DecimalFormat;

//used to generate results page
public class WriteResults {
	
	//formats a number
	public static String fn(float f){
		DecimalFormat df = new DecimalFormat("#,###.00");
		String s = df.format(f);
		return "$"+s;
	}
	
	//deformat a number
	public static float dfn(String s){
		float f = Float.parseFloat(s);
		return f;
	}
	
	//
	public static String generate(String search, Product items[], int num){
		String basepage = SearchServlet.basepage;
		String resultHTML = "";
		
		resultHTML += HTML.printTitle(search);
		resultHTML += HTML.printCSS();
		//resultHTML += HTML.printScript();
		resultHTML += HTML.printHeader(search);
		resultHTML += "<p>Resultados de <b>"+search+"</b>:</p>";
		
		//if no results
		if(num==0){
			resultHTML += "<b>No se encontraron resultados</b>";
			resultHTML += "<br><br><br>";
		} else {
			//calculate 10 price steps
			float step = (SearchAll.max - SearchAll.min)/10;
			String stepsf = "";
			String stepsb = "";
			for(int i=0; i<11; i++){
				float f = SearchAll.max-(step*i);
				if(i==0){
					stepsf += "<option value=\""+f+"\" selected>"+fn(f)+"</option>";
				} else {
					stepsf += "<option value=\""+f+"\">"+fn(f)+"</option>";
				}
				if(i==10){
					stepsb += "<option value=\""+f+"\" selected>"+fn(f)+"</option>";
				} else {
					stepsb += "<option value=\""+f+"\">"+fn(f)+"</option>";
				}
			}
			//filter interface
			resultHTML += "<a href=\"javascript:void(0);\" onclick=\"javascript:show();\">Filtrar resultados</a>";
			resultHTML += "<form id=\"filterOptions\" style=\"display:none;\">";
			resultHTML += "<table style=\"background-color:#EFEFEF;margin-left:20px;\"  >";
			resultHTML += "<tr><td>";
			resultHTML += "<b>Mostrar resultados de:</b><br>";
			resultHTML += "<table><tr><td>";
			resultHTML += "<input id=\"fbestbuy\" type=\"checkbox\" checked>Bestbuy<br>";
			resultHTML += "<input id=\"fcoppel\" type=\"checkbox\" checked>Coppel<br>";
			resultHTML += "<input id=\"fcostco\" type=\"checkbox\" checked>Costco<br>";
			resultHTML += "</td><td>";
			resultHTML += "<input id=\"felektra\" type=\"checkbox\" checked>Elektra<br>";
			resultHTML += "<input id=\"fliverpool\" type=\"checkbox\" checked>Liverpool<br>";
			resultHTML += "<input id=\"fsanborns\" type=\"checkbox\" checked>Sanborns<br>";
			resultHTML += "</td><td valign=\"top\">";
			resultHTML += "<input id=\"fsears\" type=\"checkbox\" checked>Sears<br>";
			resultHTML += "<input id=\"fwalmart\" type=\"checkbox\" checked>Walmart<br>";
			resultHTML += "<input id=\"ffamsa\" type=\"checkbox\" checked>Famsa<br>";
			resultHTML += "<td>";
			resultHTML += "</table>";
			resultHTML += "</td><td>&nbsp;&nbsp;</td><td valign=\"top\">";
			/*resultHTML += "<b>Ordenar por:</b><br>";
			resultHTML += "<input id=\"rRel\" type=\"radio\" name=\"orderBy\" checked>Relevancia<br>";
			resultHTML += "<input id=\"rMen\" type=\"radio\" name=\"orderBy\">Menor precio<br>";
			resultHTML += "<input id=\"rMay\" type=\"radio\" name=\"orderBy\">Mayor precio<br>";
			resultHTML += "</td><td>&nbsp;&nbsp;</td><td valign=\"top\">";*/
			resultHTML += "<b>Rango de precios:</b><br>";
			resultHTML += "Precio maximo <select id=\"maxSelect\">"+stepsf+"</select><br>";
			resultHTML += "Precio minimo <select id=\"minSelect\">"+stepsb+"</select>";
			resultHTML += "</td>";
			resultHTML += "</table>";
			resultHTML += "</form>";
			
			//product table
			resultHTML += "<center><table width=\"70%\" style=\"border-collapse:collapse;\">";
			resultHTML += "<tr id=\"resultTable\"bgcolor=\"#7B88E8\">";
			resultHTML += "<th>Imagen</th>";
			resultHTML += "<th>Producto</th>";
			resultHTML += "<th>Tienda</th>";
			resultHTML += "<th>Precio</th>";
			resultHTML += "</tr>";
			//dynamic content
			boolean alternate = true;
			for(int i=0; i<num; i++){
				if((items[i]!=null)&&(!items[i].name.equals(""))){
					if(alternate){
						resultHTML += "<tr id=\"r"+i+"\" bgcolor=\"#B9CCFF\">";
					} else {
						resultHTML += "<tr id=\"r"+i+"\">";
					}
					alternate = !alternate;
					resultHTML += "<td><center><a href=\""+items[i].getDetailLink()+"\">"+items[i].getProductImage(true)+"</a></center></td>";
					resultHTML += "<td><center><a href=\""+items[i].getDetailLink()+"\">"+items[i].name+"</a></center></td>";
					resultHTML += "<td><center><a href=\""+items[i].getStoreLink()+"\" target=\"_blank\"><image src=\""+items[i].getStoreImage()+"\" id=\"i"+i+"\"></a></center></td>";
					resultHTML += "<td id=\"p"+i+"\"><center>"+items[i].price+"</center></td>";
					resultHTML += "</tr>";
				}
			}
			//static content
			resultHTML += "</table></center>";
			resultHTML += "<br><br><br><br><br>";
		}
		
		resultHTML += HTML.printFooterWithScript(num);
		//return page
		return resultHTML;
	}
}
