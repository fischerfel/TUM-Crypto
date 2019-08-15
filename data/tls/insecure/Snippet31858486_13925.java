  import java.io.IOException;
  import java.net.*;
  import java.security.*;
  import java.io.*;
  import java.security.KeyManagementException;
  import java.security.NoSuchAlgorithmException;
  import java.security.KeyStoreException;
  import java.security.UnrecoverableKeyException;
  import javax.net.ssl.HttpsURLConnection;
  import javax.net.ssl.KeyManagerFactory;
  import javax.net.ssl.SSLContext;
  import javax.net.ssl.SSLSocketFactory;
  import org.apache.commons.codec.binary.Base64;

  public class AzureRest
  {
        private static KeyStore getKeyStore(String keyStoreName, String password)
throws IOException
{
    KeyStore ks = null;
    FileInputStream fis = null;
    try {
        ks = KeyStore.getInstance("JKS");
        char[] passwordArray = password.toCharArray();
        fis = new java.io.FileInputStream(keyStoreName);
        ks.load(fis, passwordArray);
        fis.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    finally {
        if (fis != null) {
            fis.close();
        }
    }
    return ks;
}

    private static SSLSocketFactory getSSLSocketFactory(String keyStoreName, String password)
throws UnrecoverableKeyException, KeyStoreException, 
NoSuchAlgorithmException, KeyManagementException, IOException {

    KeyStore ks = getKeyStore(keyStoreName, password);
    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
    keyManagerFactory.init(ks, password.toCharArray());

    SSLContext context = SSLContext.getInstance("TLS");
    context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

    return context.getSocketFactory();
}

private static String getStringFromInputStream(InputStream is) {

    BufferedReader br = null;
    StringBuilder sb = new StringBuilder();

    String line;
    try {
        br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (br != null) {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    return sb.toString();
}

private static String processGetRequest(URL url, String keyStore, String keyStorePassword) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
    SSLSocketFactory sslFactory = getSSLSocketFactory(keyStore, keyStorePassword);
    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.setSSLSocketFactory(sslFactory);
    con.setRequestMethod("GET");
    con.addRequestProperty("x-ms-version", "2013-08-01");
    InputStream responseStream = (InputStream) con.getContent();
    String response = getStringFromInputStream(responseStream);
    responseStream.close();
    return response;
}

private static int processPostRequest(URL url, byte[] data, String contentType, String keyStore, String keyStorePassword) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
    SSLSocketFactory sslFactory = getSSLSocketFactory(keyStore, keyStorePassword);
    HttpsURLConnection con = null;
    con = (HttpsURLConnection) url.openConnection();
    con.setSSLSocketFactory(sslFactory);
    con.setDoOutput(true);
    con.setRequestMethod("POST");
    con.addRequestProperty("x-ms-version", "2013-08-01");
    con.setRequestProperty("Content-Length", String.valueOf(data.length));
    con.setRequestProperty("Content-Type", contentType);

    DataOutputStream requestStream = new DataOutputStream (con.getOutputStream());
    requestStream.write(data);
    requestStream.flush();
    requestStream.close();

    System.out.println(con.getResponseMessage());

    InputStream error = ((HttpURLConnection) con).getErrorStream(); 

    BufferedReader br = null;
    if (error == null) {
         InputStream inputstream = con.getInputStream();
          br = new BufferedReader(new InputStreamReader(inputstream));
    } else {
        br = new BufferedReader(new InputStreamReader(error));
    }
    String response = "";
    String nachricht;
    while ((nachricht = br.readLine()) != null){
          response += nachricht;
    }
    System.out.println(response);
    return con.getResponseCode();
}

private static int processDeleteRequest(URL url, String keyStore, String keyStorePassword) throws UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException {
    SSLSocketFactory sslFactory = getSSLSocketFactory(keyStore, keyStorePassword);
    HttpsURLConnection con = null;
    con = (HttpsURLConnection) url.openConnection();
    con.setSSLSocketFactory(sslFactory);
    con.setRequestMethod("DELETE");
    con.addRequestProperty("x-ms-version", "2013-08-01");
    return con.getResponseCode();
}

public static void getLocation(String keyStorePath, String keyStorePassword, String subscriptionId)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    // List locations
    url = String.format("https://management.core.windows.net/%s/locations", subscriptionId);
    String response = processGetRequest(new URL(url), keyStorePath, keyStorePassword);
    System.out.println(response);
}

public static void createCloudService(String keyStorePath, String keyStorePassword, String subscriptionId, String cloudService)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    // Create cloud service
    String label = "Cloud service in java";
    String description = "Cloud service in java";
    String location = "East US 2";
    url = String.format("https://management.core.windows.net/%s/services/hostedservices", subscriptionId);
    String requestBody = "<?xml version=\"1.0\" encoding=\"utf-8\"?><CreateHostedService xmlns=\"http://schemas.microsoft.com/windowsazure\"><ServiceName>%s</ServiceName><Label>%s</Label><Description>%s</Description><Location>%s</Location></CreateHostedService>";
    requestBody = String.format(requestBody, cloudService, Base64.encodeBase64String(label.getBytes()), description, location);
    int createResponseCode = processPostRequest(new URL(url), requestBody.getBytes(), "application/xml", keyStorePath, keyStorePassword);
    System.out.println("Created service: " + createResponseCode);
}

public static void deleteCloudService(String keyStorePath, String keyStorePassword, String subscriptionId, String cloudService)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    //Delete cloud service
    url = String.format("https://management.core.windows.net/%s/services/hostedservices/%s", subscriptionId, cloudService);
    int deleteResponseCode = processDeleteRequest(new URL(url), keyStorePath, keyStorePassword);
    System.out.println(deleteResponseCode);
}

public static void listImages(String keyStorePath, String keyStorePassword, String subscriptionId)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    String response = "";
    // List Images
    url = String.format("https://management.core.windows.net/%s/services/images", subscriptionId);
    response = processGetRequest(new URL(url), keyStorePath, keyStorePassword);
    System.out.println(response);
}

public static void listAutomationAccounts(String keyStorePath, String keyStorePassword, String subscriptionId)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    String response = "";
    // List automation accounts in a cloud service
    url = String.format("https://management.core.windows.net/%s/cloudServices/OaaSCSI6EGAZU6F6QTCK5XRVT45FKJC6RC7IQIQW3OPR7SVLE4ZPD4IQQQ-East-US?resourceType=AutomationAccount&detailLevel=Full&resourceProviderNamespace=automation", subscriptionId);
    response = processGetRequest(new URL(url), keyStorePath, keyStorePassword);
    System.out.println(response);
}

public static void checkCloudServiceAvailability(String keyStorePath, String keyStorePassword, String subscriptionId, String cloudService)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    String response = "";

    // Check service name availability
    url = String.format("https://management.core.windows.net/%s/services/hostedservices/operations/isavailable/%s", subscriptionId, cloudService);
    response = processGetRequest(new URL(url), keyStorePath, keyStorePassword);
    System.out.println(response);
}

public static void createAutomationAccount(String keyStorePath, String keyStorePassword, String subscriptionId)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    String response = "";
    // Create automation
    String automationName = "javaauto";
    String location = "East US 2";

    url = String.format("https://management.core.windows.net/%s/cloudServices/OaaSCSI6EGAZU6F6QTCK5XRVT45FKJC6RC7IQIQW3OPR7SVLE4ZPD4IQQQ-East-US/resources/automation/AutomationAccount/%s?resourceType=AutomationAccount&detailLevel=Full&resourceProviderNamespace=automation", subscriptionId, automationName);

    String requestBody = "<Response xmlns=\"http://schemas.microsoft.com/windowsazure\"><CloudServiceSettings><GeoRegion>East US 2</GeoRegion></CloudServiceSettings><SchemaVersion>1.0</SchemaVersion><Plan>Basic</Plan></Resource>";

    int createResponseCode = processPostRequest(new URL(url), requestBody.getBytes(), "application/xml", keyStorePath, keyStorePassword);

    System.out.println("CreateAutomationAccount " + createResponseCode);
}

// Create a published runbook
public static void createRunbook(String keyStorePath, String keyStorePassword, String subscriptionId)
throws  UnrecoverableKeyException, KeyManagementException, KeyStoreException, NoSuchAlgorithmException, IOException
{
    String url = "";
    String response = "";
    // Create runbook 
    String runbookName = "Write-HellowWorld";
    String cloudService = <CLOUD_SERVICE>;

    url = String.format("https://management.core.windows.net/%s/cloudServices/%s/resources/automation/~/automationAccounts/javaauto/runbooks/%s?api-version=2014-12-08", subscriptionId, cloudService, runbookName);
    String requestBody = "{ \"tags\":{ \"Testing\":\"show value\", \"Source\":\"TechNet Script Center\" }, \"properties\":{ \"description\":\"Hello world\", \"runbookType\":\"Script\", \"logProgress\":false, \"publishContentLink\":{ \"uri\":\"https://gallery.technet.microsoft.com/scriptcenter/The-Hello-World-of-Windows-81b69574/file/111354/1/Write-HelloWorld.ps1\", \"contentVersion\":\"1.0.0.0\", \"contentHash\":{ \"algorithm\":\"sha256\", \"value\":\"EqdfsYoVzERQZ3l69N55y1RcYDwkib2+2X+aGUSdr4Q=\" } } } }";

    int createResponseCode = processPutRequest(new URL(url), requestBody.getBytes(), "application/json", keyStorePath, keyStorePassword);
    System.out.println("created runbook:: " + createResponseCode);
}

public static void main(String[] args)
{
    try {
        String subscriptionId = "<Insert your subscription ID>"; 
        String keyStorePath = "KeyStore.jks";
        String keyStorePassword = "secret321";

        String cloudService = "1testjava";

        //listImages(keyStorePath, keyStorePassword, subscriptionId);
        listAutomationAccounts(keyStorePath, keyStorePassword, subscriptionId);
        checkCloudServiceAvailability(keyStorePath, keyStorePassword, subscriptionId, cloudService);
        deleteCloudService(keyStorePath, keyStorePassword, subscriptionId, cloudService);
        createCloudService(keyStorePath, keyStorePassword, subscriptionId, cloudService);
        createAutomationAccount(keyStorePath, keyStorePassword, subscriptionId);

        // This also throws an error
        createRunbook(keyStorePath, keyStorePassword, subscriptionId);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}
