import java.io.*;

public class UploadImagesGSUTIL {
    public static void main(String[] args){
        File folder = new File(".");
        File[] lof = folder.listFiles();
        for(int i=0; i<lof.length; i++){
            String filename = lof[i].getName();
            if(filename.endsWith(".jpg")){
                String[] cmd = {"gsutil","cp",filename,
                                "gs://kompramex-imgs/walmart"};
                try{
                    Runtime r = Runtime.getRuntime();
                    Process p = r.exec(cmd);
                    p.waitFor();
                    System.out.println(i+" done");
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}