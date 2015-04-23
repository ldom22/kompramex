package search;

//used to generate product detail page
public class WriteProductDetail {
	//generates raw HTML
	public static String generate(Product item){
		String basepage = SearchServlet.basepage;
		String resultHTML = "";
		
        //begin HTML page
		resultHTML += HTML.printTitle(item.name);
		resultHTML += HTML.printCSS();
		resultHTML += HTML.printHeader(item.name);
		
		//patch for empty descriptions
		if((item.description==null) || item.description.equals("")){
			item.description = item.name;
		}
				
        //dynamic content
		resultHTML += "<center><table width=\"70%\" style=\"border-collapse:collapse;\">";
        resultHTML += "<h1>"+item.name+"</h1>";
        resultHTML += "</table></center>";
        resultHTML += "<center><table width=\"70%\" style=\"border-collapse:collapse;\">";
        resultHTML += "<td>"+item.getProductImage(false)+"</td>";
        resultHTML += "<td>";
        resultHTML += "<center><h1>"+item.price+"</h1></center>";
        resultHTML += "<center><a href = \""+item.getProductLink()+"\" target=\"_blank\">Ver en la página de "+item.store+"</a></center>";
        //missing description bug
        if(item.description.equals("ón")){
        	resultHTML += "<center>"+item.name+"</center>";
        } else {
        	resultHTML += "<p align=\"center\">"+item.description+"</p>";
        }
        resultHTML += "<br>";
        resultHTML += "<center><a href = \""+item.getStoreLink()+"\" target=\"_blank\">"+item.getStoreLink()+"</a></center>";
        resultHTML += "<br>";
        	//amazon ads
        	resultHTML += "<center>";
        	resultHTML += "<script type=\"text/javascript\" language=\"javascript\">";
       		resultHTML += "  amzn_assoc_ad_type = \"contextual\";";
        	resultHTML += "  amzn_assoc_tracking_id = \"kompramex-20\";";
       		resultHTML += "  amzn_assoc_marketplace = \"amazon\";";
       		resultHTML += "  amzn_assoc_region = \"US\";";
       		resultHTML += "  amzn_assoc_placement = \"RZGFHH6JWBDJSJXF\";";
       		resultHTML += "  amzn_assoc_linkid = \"RZGFHH6JWBDJSJXF\";";
       		resultHTML += "  amzn_assoc_emphasize_categories = \"\";";
       		resultHTML += "  amzn_assoc_fallback_products = \"B00F3J4E5U, B00J4TK4B8, B00KKRC4I4\";";
       		resultHTML += "  amzn_assoc_width = \"300\";";
       		resultHTML += "  amzn_assoc_height = \"250\";";
       		resultHTML += "</script>";
       		resultHTML += "<script type=\"text/javascript\" language=\"javascript\" src=\"//z-na.amazon-adsystem.com/widgets/q?ServiceVersion=20070822&Operation=GetScript&ID=OneJS&WS=1&MarketPlace=US&source=ac\"></script>";
       		resultHTML += "</center>";
       		//end amazon ads
        resultHTML += "</td>";
        resultHTML += "</table></center>";
        
        //static content
        resultHTML += "<br><br><br><br><br>";
		resultHTML += HTML.printFooterInfolinks();
		
        //return page
		return resultHTML;
	}
}
