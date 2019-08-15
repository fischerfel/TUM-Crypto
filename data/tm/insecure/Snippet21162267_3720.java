/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.eclat.client;

import com.sun.xml.ws.security.soap12.Header;
import com.sun.xml.wss.XWSSConstants;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPFactory;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import org.eclat.vtlservice.AuthHeader;
import org.eclat.vtlservice.Responsevalid;
import org.eclat.vtlservice.DNvalidation;
import org.eclat.vtlservice.DNvalidationSoap;

/**
 *
 * @author surjit
 */
public class Client {

    private static final String WS_URL = "https://202.164.33.157:7074/DNvalidation.asmx?WSDL";


    private static void trustEveryone() { 
     try { 
             HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){ 
                     public boolean verify(String hostname, SSLSession session) { 
                             return true; 
                     }}); 
             SSLContext context = SSLContext.getInstance("TLS"); 
             context.init(null, new X509TrustManager[]{new X509TrustManager(){ 
                     public void checkClientTrusted(X509Certificate[] chain, 
                                     String authType) throws CertificateException {} 
                     public void checkServerTrusted(X509Certificate[] chain, 
                                     String authType) throws CertificateException {} 
                     public X509Certificate[] getAcceptedIssuers() { 
                             return new X509Certificate[0]; 
                     }}}, new SecureRandom()); 
             HttpsURLConnection.setDefaultSSLSocketFactory( 
                             context.getSocketFactory()); 
     } catch (Exception e) { // should never happen 
             e.printStackTrace(); 
     } 
 }

    public static  Responsevalid chkMob(String directorynumber, String email, String alternate) {

        DNvalidation service = new org.eclat.vtlservice.DNvalidation();
        DNvalidationSoap port = service.getDNvalidationSoap();

        Map<String, Object> requestContext = ((BindingProvider)port).getRequestContext();

        requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, WS_URL);
        Map<String, List<String>> requestHeaders = new HashMap<String, List<String>>();
        requestHeaders.put("Username", Collections.singletonList("username"));
        requestHeaders.put("Password", Collections.singletonList("password"));
        requestHeaders.put("IP", Collections.singletonList("IP"));
        requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, requestHeaders);

        return port.chkMob(directorynumber, email, alternate);

    }

    public static void main(String args[]) {  
        trustEveryone();
       System.out.println(chkMob("parameter1","parameter2","parameter3")); 
    }

}


But when I am running this code..I am getting following error:



Jan 16, 2014 5:38:47 PM [com.sun.xml.ws.policy.jaxws.PolicyConfigParser]  parse
INFO: WSP5018: Loaded WSIT configuration from file: file:/E:/New%20Workspace/src/service/build/web/WEB-INF/classes/META-INF/wsit-client.xml.
Exception in thread "main" javax.xml.ws.soap.SOAPFaultException: System.Web.Services.Protocols.SoapException: Server was unable to process request. ---> System.NullReferenceException: Object reference not set to an instance of an object.
   at DNvalidation.chk_mob(String directorynumber, String email, String alternate) in d:\QTLAPI\App_Code\DNvalidation.cs:line 57
   --- End of inner exception stack trace ---
    at com.sun.xml.ws.fault.SOAP11Fault.getProtocolException(SOAP11Fault.java:189)
    at com.sun.xml.ws.fault.SOAPFaultBuilder.createException(SOAPFaultBuilder.java:122)
    at com.sun.xml.ws.client.sei.SyncMethodHandler.invoke(SyncMethodHandler.java:119)
    at com.sun.xml.ws.client.sei.SyncMethodHandler.invoke(SyncMethodHandler.java:89)
    at com.sun.xml.ws.client.sei.SEIStub.invoke(SEIStub.java:140)
    at com.sun.proxy.$Proxy37.chkMob(Unknown Source)
    at org.eclat.client.Client.chkMob(Client.java:85)`enter code here`
    at org.eclat.client.Client.main(Client.java:91)
Java Result: 1
