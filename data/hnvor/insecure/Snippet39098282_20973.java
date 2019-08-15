package javaapplication7;



import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import javax.crypto.SecretKey;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 *
 * @author dhanish
 */
public class JavaApplication7 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

           try
        {
            String keyPath = "C:\\key.jks";
            String keyPass = "xxx";
            String keyType = "JKS";


            //path to SSL keystore

            System.setProperty("javax.net.ssl.keyStore", keyPath);
            System.setProperty("javax.net.ssl.keyStorePassword", keyPass);
            System.setProperty("javax.net.ssl.keyStoreType", keyType);

            //build the XML to post
            String xmlString = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:req=\"http://olp.sadad.com/sadadpaymentmanagement/service/olppaymentmanager/req\">";
            xmlString = xmlString + "\n" + "<soapenv:Header/><soapenv:Body>";
            xmlString = xmlString + "\n" + "<initiatePaymentDetailsReq>";
            xmlString = xmlString + "\n" + "<olpIdAlias>xxxx</olpIdAlias>";
            xmlString = xmlString + "\n" + "<merchantId>xxxx</merchantId>";
            xmlString = xmlString + "\n" + "<merchantRefNum>2016081870001</merchantRefNum>";
            xmlString = xmlString + "\n" + "<paymentAmount>100</paymentAmount>";
            xmlString = xmlString + "\n" + "<paymentCurrency>SAR</paymentCurrency>";
            xmlString = xmlString + "\n" + "<dynamicMerchantLandingURL>http://sweetroomksa.com/index.php?route=payment/custom/confirm</dynamicMerchantLandingURL>";
            xmlString = xmlString + "\n" + "<dynamicMerchantFailureURL>#</dynamicMerchantFailureURL>";
            xmlString = xmlString + "\n" + "</initiatePaymentDetailsReq>";
            xmlString = xmlString + "\n" + "</soapenv:Body></soapenv:Envelope>";

            //post XML over HTTPS
            URL url = new URL("https://xxx.xxx.com/soap?service=RB_OLP_INITIATE_PAYMENT"); //replace 
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput( true );


            connection.setRequestProperty( "Content-Type", "text/xml"  );
            connection.setHostnameVerifier(new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
            connection.connect();

            //tell the web server what we are sending
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(xmlString);
            writer.flush();
            writer.close();

            // reading the response
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            StringBuilder buf = new StringBuilder();
            char[] cbuf = new char[ 2048 ];
            int num;
            while ( -1 != (num=reader.read( cbuf )))
            {
                buf.append( cbuf, 0, num );
            }
            String result = buf.toString();
            System.out.println(result);
        }
        catch(Exception e)
        {
            System.out.println(e.getCause());
            e.printStackTrace();
        }





    }

}
