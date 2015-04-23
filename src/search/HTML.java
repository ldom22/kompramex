package search;

public class HTML {
	//puts title, opens HTML tag
	public static String printTitle(String subtitle){
		String html = "";
		html += "<html>";
		html += "<head>";
		html += "<title>Kompramex - "+subtitle+"</title>";
		html += "</head>";
		return html;
	}
	//puts CSS and body tag
	public static String printCSS(){
		String html = "";
		html += "<style type=\"text/css\">";
		html += "<!--";
		html += "A:link{text-decoration:none}";
		html += "A:visited{text-decoration:none}";
		html += "A:active{text-decoration:none}";
		html += "A:hover{text-decoration:underline}";
		html += "img{border-style:none;}";
		html += "-->";
		html += "</style>";
		html += "<body>";
		return html;
	}
	//puts the sorting script
	public static String printScript(){
		String html = "";
		html += "<script type=\"text/javascript\" ";
		html += "src=\""+SearchServlet.basepage+"sort.js\">";
		return html;
	}
	//prints header with search bar
	public static String printHeader(String search){
		String html = "";
		html += "<table ";
		html += "style=\"width:100%;border:20px;background-color:#EFEFEF;top:0;left:0;right:0;position:absolute;\">";
		html += "<td>";
		html += "<form name=\"f\"><a href=\"index.html\"><b style=\"color:#7B88E8;font-size:30;\">Kompramex</b></a> <b style=\"color:#7B88E8;font-size:30;\">|</b> <input name=\"q\" size=\"50\" value=\""+search+"\"></input><button onclick=\"go();\">Buscar</button> &nbsp;&nbsp;&nbsp;";
		html += "Busca y compara precios en las tiendas mas grandes de <b style=\"color:#7B88E8\">M&eacute;xico</b></form>";
		html += "</td>";
		html += "</table>";
		html += "<br><br><br>";
		return html;
	}
	//prints footer and javascript, closes body and HTML tags
	public static String printFooter(){
		String html = "";
		html += "<table "; 
		html += "style=\"width:100%;bottom:0;left:0;right:0;position:fixed;background-color:#EFEFEF;border:20px;\">";
		html += "<th><br><br></th></table>";
		html += "<div style=\"bottom:10;left:20;position:fixed;right:0\">";
		html += "<a href=\"about.html\">Acerca de Kompramex</a>";
		html += "&nbsp;&nbsp;&nbsp;&nbsp;";
		html += "<a href=\"https://docs.google.com/forms/d/1NfM9zCrJBPtEgQ0aqap0R98HBfY_H-1htms3nLt5y60/viewform\">Danos tu opini&oacute;n</a>";
		html += "</div>";
		html += "<div style=\"bottom:2;right:20;position:fixed;\">";
		html += "<a href=\"http://www.facebook.com/kompramex\"><image src=\"facebook.png\"></a> ";
		html += "<a href=\"http://www.twitter.com/kompramex\"><image src=\"twitter.png\"></a>";
		html += "</div>";
		
		html += "</body>";
				
		html += "<script>";
		html += "function go() {window.location.href='clock?q='+f.q.value;}";
		html += "</script>";
				
		html += "</html>";
		return html;
	}
	
	//prints footer and javascript, closes body and HTML tags
	public static String printFooterWithScript(int num){
		String html = "";
		html += "<table "; 
		html += "style=\"width:100%;bottom:0;left:0;right:0;position:fixed;background-color:#EFEFEF;border:20px;\">";
		html += "<th><br><br></th></table>";
		html += "<div style=\"bottom:10;left:20;position:fixed;right:0\">";
		html += "<a href=\"about.html\">Acerca de Kompramex</a>";
		html += "&nbsp;&nbsp;&nbsp;&nbsp;";
		html += "<a href=\"https://docs.google.com/forms/d/1NfM9zCrJBPtEgQ0aqap0R98HBfY_H-1htms3nLt5y60/viewform\">Danos tu opini&oacute;n</a>";
		html += "</div>";
		html += "<div style=\"bottom:2;right:20;position:fixed;\">";
		html += "<a href=\"http://www.facebook.com/kompramex\"><image src=\"facebook.png\"></a> ";
		html += "<a href=\"http://www.twitter.com/kompramex\"><image src=\"twitter.png\"></a>";
		html += "</div>";
			
		html += "</body>";
					
		html += "<script type=\"text/javascript\">";
		html += "var numOfRows="+num+";";
		html += "var max="+SearchAll.max+";";
		html += "var min="+SearchAll.min+";";
		html += "function go() {window.location.href='clock?q='+f.q.value;}";
		html += "</script>";
		
		html += "<script type=\"text/javascript\" src =\"sort.js\">";
		html += "</script>";
					
		html += "</html>";
		return html;
	}
	
	//prints footer and javascript, closes body and HTML tags
	public static String printFooterInfolinks(){
		String html = "";
		html += "<table "; 
		html += "style=\"width:100%;bottom:0;left:0;right:0;position:fixed;background-color:#EFEFEF;border:20px;\">";
		html += "<th><br><br></th></table>";
		html += "<div style=\"bottom:10;left:20;position:fixed;right:0\">";
		html += "<a href=\"about.html\">Acerca de Kompramex</a>";
		html += "&nbsp;&nbsp;&nbsp;&nbsp;";
		html += "<a href=\"https://docs.google.com/forms/d/1NfM9zCrJBPtEgQ0aqap0R98HBfY_H-1htms3nLt5y60/viewform\">Danos tu opini&oacute;n</a>";
		html += "</div>";
		html += "<div style=\"bottom:2;right:20;position:fixed;\">";
		html += "<a href=\"http://www.facebook.com/kompramex\"><image src=\"facebook.png\"></a> ";
		html += "<a href=\"http://www.twitter.com/kompramex\"><image src=\"twitter.png\"></a>";
		html += "</div>";
		
		//infolinks ads
		html += "<script type=\"text/javascript\">";
		html += "var infolinks_pid = 2220654;";
		html += "var infolinks_wsid = 0;";
		html += "</script>";
		html += "<script type=\"text/javascript\" src=\"http://resources.infolinks.com/js/infolinks_main.js\"></script>";
		//end infolinks ads
		
		html += "</body>";
					
		html += "<script>";
		html += "function go() {window.location.href='search?q='+f.q.value;}";
		html += "</script>";
					
		html += "</html>";
		return html;
	}
}
