import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.util.*;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class JavaPOST
{

    public static void doSubmit(String url, Map<String, String> data) throws Exception 
    {

            //SSL Certificate Acceptor
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);

            URL siteUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection)siteUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setHostnameVerifier(
            new HostnameVerifier() 
            {
                @Override
                public boolean verify(String arg0, SSLSession arg1) 
                {
                    return true;
                }
            });

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());

            Set keys = data.keySet();
            Iterator keyIter = keys.iterator();
            String content = "";
            for(int i=0; keyIter.hasNext(); i++) 
            {
                Object key = keyIter.next();
                if(i!=0)
                {
                    content += "&";
                }
                content += key + "=" + URLEncoder.encode(data.get(key), "UTF-8");
            }           
            //System.out.println(content);
            out.writeBytes(content);
            out.flush();
            out.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            while((line=in.readLine())!=null) 
            {
                System.out.println(line);
            }

            System.out.println(conn.getURL());

    }

    public static void main(String[] args)
    {
        Map<String, String> data = new HashMap<String, String>();
        data.put("start_time", "103000");
        data.put("end_time", "210000");

        try
        {
            doSubmit("https://somedomain/webpage.html", data);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

class DefaultTrustManager implements X509TrustManager 
{

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

    }
