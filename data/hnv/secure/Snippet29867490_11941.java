import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HttpsURLConnection;
public class testa {
    public static void main(String[] args) throws IOException {
        String nextLine = "";
        URL url = null;
        URLConnection urlConn = null;       
        InputStreamReader  inStream = null;
        BufferedReader buff = null;
        try{
            url  = new URL("https://kickass.to");
            urlConn = url.openConnection();      
            ((HttpsURLConnection) urlConn).setHostnameVerifier(new Verifier());
            inStream = new InputStreamReader(urlConn.getInputStream());
            buff= new BufferedReader(inStream);
            while(nextLine != null){
                nextLine = buff.readLine();
                System.out.println(nextLine);               
            }   
        }catch(MalformedURLException e){
               System.out.println("Please check the URL:" +  e.toString() );
        } catch(IOException  e1){
            System.out.println("Can't read  from the Internet: "+ e1.toString() ); 
        }        
    }

 }
