import java.io.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

public class SecurityService {

private String _username;
private String _password;
private String _stsUrl;
private String _samlAssertion;
private String _samlEncoded;
private String _binarySecret;
private String _workingDirectory;
private String _platformUrl;
private String _soapBody;
private Integer _responseCode;
private Integer _plaformResponseCode;
private String _response;
private String _platformResponse;
private String _xproofSignature;
private Map<String, String> _headerDictionary;

public void setUsername(String username) {
    this._username = username;
}

public void setPassword(String password) {
    this._password = password;
}

public void setStsUrl(String stsUrl) {
    this._stsUrl = stsUrl;
}

public String getStsUrl() {
    return _stsUrl;
}

public void setplatformUrl(String platformUrl) {
    this._platformUrl = platformUrl;
}

public String getSamlAssertion() {
    return _samlAssertion;
}

public String getSamlEncoded() {
    return _samlEncoded;
}

public String getSoapBody() {
    return _soapBody;
}

public Integer getResponseCode() {
    return _responseCode;
}

public Integer getPlatformResponseCode() {
    return _plaformResponseCode;
}

public String getResponse() {
    return _response;
}

public String getPlatformResponse() {
    return _platformResponse;
}

public String getXProofSignature() {
    return _xproofSignature;
}

public String getBinarySecret() {
    return _binarySecret;
}

public String gePlatFormUrl() {
    return _platformUrl;
}

public void setHeaderDictionary(Map<String, String> headerDictionary){
   this._headerDictionary = headerDictionary;
}

public Map<String, String> getHeaderDictionary(){
   return _headerDictionary;
}

public SecurityService() throws Exception {
}

public SecurityService(Boolean useConfig) throws Exception {

    if (useConfig) {
        this._workingDirectory = System.getProperty("user.dir") + "\\app.config";
        this.getProperties();
    }
}    

public void sendAuthenticatedGet() throws Exception {

    URL obj = new URL(_platformUrl);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    // optional default is GET
    con.setRequestMethod("GET");

    // Add request header        
    con.setRequestProperty("Authorization", "Saml " + _samlEncoded);
    con.setRequestProperty("X-ProofSignature", _xproofSignature);

    _plaformResponseCode = con.getResponseCode();       

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close(); 

    _platformResponse = response.toString();

}

public void sendAuthenticatedPost(String body) throws Exception {

    URL obj = new URL(_platformUrl);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

    //add request header
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");

    // Add request header
    con.setRequestProperty("Authorization", "Saml " + _samlEncoded);
    con.setRequestProperty("X-ProofSignature", _xproofSignature);

    // Add Azure Subscription Key using generic Add Headers method
    if (_headerDictionary != null) {
        for (String key : _headerDictionary.keySet()) {
            con.setRequestProperty(key, _headerDictionary.get(key));
        }
    }

    _soapBody = body;

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    //wr.writeBytes(urlParameters);
    wr.writeBytes(_soapBody);
    wr.flush();
    wr.close();
    _responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();

    _response = response.toString();

}

// HTTP POST request
public void sendPostToSts() throws Exception {

    URL obj = new URL(_stsUrl);
    HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

    //add request header
    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/soap+xml");

    String body = getTemplateCertificate();

    _soapBody = (((body.replace("[Created]", Instant.now().toString())).replace("[Expires]", Instant.now()
            .plusSeconds(300).toString())).replace("[username]", _username)).replace("[password]", _password).replace("[stsUrl]",                _stsUrl);

    // Send post request
    con.setDoOutput(true);
    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
    //wr.writeBytes(urlParameters);
    wr.writeBytes(_soapBody);
    wr.flush();
    wr.close();
    _responseCode = con.getResponseCode();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
    }
    in.close();

    _response = response.toString();
    // Get Binary Secret
    // <trust:BinarySecret></trust:BinarySecret>

    final Pattern patternBinarySecret = Pattern.compile("<trust:BinarySecret>(.+?)</trust:BinarySecret>");
    final Matcher matcherBinarySecret = patternBinarySecret.matcher(response.toString());
    matcherBinarySecret.find();

    _binarySecret = matcherBinarySecret.group(1);

    // Get the SAML Assertion
    final Pattern patternEncryptedAssertion = Pattern.compile("<trust:RequestedSecurityToken>(.+?)</trust:RequestedSecurityToken>");
    final Matcher matcherEncryptedAssertion = patternEncryptedAssertion.matcher(response.toString());
    matcherEncryptedAssertion.find();
    _samlAssertion = matcherEncryptedAssertion.group(1);        


    byte[] proofKeyBytes = _binarySecret.getBytes("UTF-8");
    String encoded = Base64.getEncoder().encodeToString(proofKeyBytes);
    byte[] decoded = Base64.getDecoder().decode(encoded);

    // SAML Stuff - Works beautifully
    byte[] samlBytes = _samlAssertion.getBytes("UTF-8");
    _samlEncoded = Base64.getEncoder().encodeToString(samlBytes);       

    _xproofSignature = this.encode(_samlAssertion, _binarySecret);
}

private static String readFile( String file ) throws IOException {
    BufferedReader reader = new BufferedReader( new FileReader(file));
    String line = null;
    StringBuilder stringBuilder = new StringBuilder();
    String ls = System.getProperty("line.separator");

    try {
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    } finally {
        reader.close();
    }
}

// Embedded WS-Trust template for username/password RST
private static String getTemplate () {
    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" xmlns:u=               \"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><s:Header><a:Action s:mustUnderstand=               \"1\">http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue</a:Action><a:MessageID>urn:uuid:cfea5555-248c-46c3-9b4d-              54936b7f815c</a:MessageID><a:ReplyTo><a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo><a:To                s:mustUnderstand=\"1\">[stsUrl]</a:To><o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-               open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\"><u:Timestamp u:Id=\"_0\"><u:Created>[Created]              </u:Created><u:Expires>[Expires]</u:Expires></u:Timestamp><o:UsernameToken u:Id=\"uuid-e273c018-1da7-466e-8671-86f6bfe7ce3c-              17\"><o:Username>[username]</o:Username><o:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-              token-profile-1.0#PasswordText\">[password]              </o:Password></o:UsernameToken></o:Security></s:Header><s:Body><trust:RequestSecurityToken xmlns:trust=\"http://docs.oasis-               open.org/ws-sx/ws-trust/200512\"><wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy               \"><wsa:EndpointReference xmlns:wsa=\"http://www.w3.org/2005/08/addressing               \"><wsa:Address>https://mbplatform/</wsa:Address></wsa:EndpointReference></wsp:AppliesTo><trust:RequestType>http://docs.oasis-               open.org/ws-sx/ws-trust/200512/Issue</trust:RequestType><trust:TokenType>http://docs.oasis-open.org/wss/oasis-wss-saml-token-              profile-1.1#SAMLV2.0</trust:TokenType></trust:RequestSecurityToken></s:Body></s:Envelope>";
}    

private String encode(String key, String data) throws Exception {
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
}

private void getProperties() throws Exception {
    Properties prop = new Properties();
    String fileName = _workingDirectory;
    InputStream is = new FileInputStream(fileName);
    prop.load(is);
    _username = prop.getProperty("app.username");
    _password = prop.getProperty("app.password");
    _platformUrl = prop.getProperty("app.platformUrl");
    _stsUrl = prop.getProperty("app.stsUrl");
}
