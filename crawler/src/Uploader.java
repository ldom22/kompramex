//libraries
import java.io.*;
import java.text.*;

//uploads all data to datastore
public class Uploader {
	public static void main(String args[]) throws Exception {
		//1)get list of text files
		String files;
		//windows File folder = new File("..");
        //mac
		File[] listOfFiles = new File[1];
        listOfFiles[0] = new File("/Users/ldom/Documents/workspace/search/crawler/results/CW45/output.txt");
        //listOfFiles[1] = new File("/Users/ldom/Documents/workspace/search/crawler/results/CW33/Costco_products.txt");
        //listOfFiles[2] = new File("/Users/ldom/Documents/workspace/search/crawler/results/CW33/Liverpool_products.txt");
        //listOfFiles[3] = new File("/Users/ldom/Documents/workspace/search/crawler/results/CW33/Sanborns_products.txt");
        //listOfFiles[4] = new File("/Users/ldom/Documents/workspace/search/crawler/results/CW33/Sears_products.txt");
        //listOfFiles[5] = new File("/Users/ldom/Documents/workspace/search/crawler/results/CW33/Walmart_products.txt");
		//2)read and print them to datastore
		long n = 0;
        long start = 0;
		for(File f: listOfFiles){
            System.out.println(f.getName());
			if(!f.getName().endsWith(".txt")){
				break;
			}
			BufferedReader br = new BufferedReader(new FileReader(f));
			String input = br.readLine();
			while(input != null){
                if(n<start){
                    n++;
                    System.out.println("skipped. n = "+n);
                    input = br.readLine();
                } else {
                    input = input.replace("\"","%22");
                    input = input.replace("\t","");
                    input = input.replace(" ","%20");
                    String[] item = input.split("@@@@");
                    //arm product
                    Product p = new Product(item[0],item[1],item[2],item[3]);
                    //patch price NOTE REMOVE THIS FOR OTHER STORES!!!!
                    if(p.store.equals("Famsa")){
                        DecimalFormat df = new DecimalFormat("#,###.00");
                        p.price = df.format(p.price_int);
                        p.price = "$"+p.price;
                    }
                    //END REMOVE!!!
                    try {
                        p.image = item[4];
                    } catch(Exception e){}
                    try {
                        p.description = item[5];
                        //patch: limit description to 500 chars
                        /*if(!p.description.equals("")){
                            p.description = p.description.replace(" ", "%20");
                            while(p.description.length()>500){
                                p.description = p.description.substring(0,p.description.lastIndexOf("%20"));
                            }
                        }*/
                    } catch(Exception e){}
                    try {
                        p.upc = item[6];
                    } catch(Exception e){}
                    //String baseUrl = "http://127.0.0.1:8888/search?q=g3h34389h7432&";
                    String baseUrl = "http://www.kompramex.com/search?q=g3h34389h7432&";
                    baseUrl += "name="+p.name+"&";
                    baseUrl += "store="+p.store+"&";
                    baseUrl += "price="+p.price+"&";
                    baseUrl += "productlink="+p.productlink+"&";
                    baseUrl += "image="+p.image+"&";
                    baseUrl += "description="+p.description+"&";
                    baseUrl += "upc="+p.upc;
                    //post to url
                    try {
                        String response = URLConnectionReader.getText(baseUrl);
                        if(response.equals("Success: saved")){
                            n++;
                            System.out.println(n+" items uploaded");
                            //read next line only if succeded
                            input = br.readLine();
                        } else if(response.equals("Success: repeated")){
                            n++;
                            System.out.println(n+" items uploaded (current item was repeated and not uploaded");
                            //read next line only if succeded
                            input = br.readLine();
                        } else {
                            System.out.println("ERROR! current item is "+n);
                            System.out.print("Skip? Y or N");
                            BufferedReader b = new BufferedReader(new InputStreamReader(System.in));
                            if(b.readLine().contains("Y")){
                                input = br.readLine();
                            }
                        }
                    } catch (Exception e){
                        System.out.println("format error, skipping item"+n);
                        e.printStackTrace();
                        input = br.readLine();
                    }
                }
            }
        }
		System.out.println("Successfully finished all lists!!!");
		System.out.println("Successfully finished all lists!!!");
		System.out.println("Successfully finished all lists!!!");
	}
}
