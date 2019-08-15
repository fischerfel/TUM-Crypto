import java.net.*;
import java.util.*;
import java.text.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.apache.commons.codec.binary.Base64;

public class Test
{
    private static Base64 base64 = new Base64();

    public static void signRequestSK(HttpURLConnection request, String account, String key) throws Exception
    {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";

        StringBuilder sb = new StringBuilder();
        sb.append("GET\n"); // method
        sb.append('\n'); // content encoding
        sb.append('\n'); // content language
        sb.append('\n'); // content length
        sb.append('\n'); // md5 (optional)
        sb.append('\n'); // content type
        sb.append('\n'); // legacy date
        sb.append('\n'); // if-modified-since
        sb.append('\n'); // if-match
        sb.append('\n'); // if-none-match
        sb.append('\n'); // if-unmodified-since
        sb.append('\n'); // range
        sb.append("x-ms-date:" + date + '\n'); // headers
        sb.append("x-ms-version:2009-09-19\n");
        sb.append("/" + account + request.getURL().getPath() + "\ncomp:list");

        //System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(base64.decode(key), "HmacSHA256"));
        String authKey = new String(base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
        String auth = "SharedKey " + account + ":" + authKey;
        request.setRequestProperty("x-ms-date", date);
        request.setRequestProperty("x-ms-version", "2009-09-19");
        request.setRequestProperty("Authorization", auth);
        request.setRequestMethod("GET");
        System.out.println(auth);
    }

    public static void signRequestSKL(HttpURLConnection request, String account, String key) throws Exception
    {
        SimpleDateFormat fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
        String date = fmt.format(Calendar.getInstance().getTime()) + " GMT";

        StringBuilder sb = new StringBuilder();
        sb.append("GET\n"); // method
        sb.append('\n'); // md5 (optional)
        sb.append('\n'); // content type
        sb.append('\n'); // legacy date
        sb.append("x-ms-date:" + date + '\n'); // headers
        sb.append("x-ms-version:2009-09-19\n");
        sb.append("/" + account + request.getURL().getPath() + "?comp=list");

        //System.out.println(sb.toString());
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(base64.decode(key), "HmacSHA256"));
        String authKey = new String(base64.encode(mac.doFinal(sb.toString().getBytes("UTF-8"))));
        String auth = "SharedKeyLite " + account + ":" + authKey;
        request.setRequestProperty("x-ms-date", date);
        request.setRequestProperty("x-ms-version", "2009-09-19");
        request.setRequestProperty("Authorization", auth);
        request.setRequestMethod("GET");
        System.out.println(auth);
    }



    public static void main(String args[]) throws Exception
    {
        String account = args[0];
        String key = args[1];
        HttpURLConnection connection = (HttpURLConnection) (new URL("http://" + account + ".blob.core.windows.net/?comp=list")).openConnection();
        signRequestSKL(connection, account, key);
        connection.connect();
        System.out.println(connection.getResponseMessage());

        connection = (HttpURLConnection) (new URL("http://" + account + ".blob.core.windows.net/?comp=list")).openConnection();
        signRequestSK(connection, account, key);
        connection.connect();
        System.out.println(connection.getResponseMessage());
    }
}
