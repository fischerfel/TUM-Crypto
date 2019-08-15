/*
 * LNMOCheckoutTester.java
 *
 * Nov 20, 2016 Joseph Makara -  Created File to tester Lina Na M-PESA Online checkout
 *
 *
 */
package testMe;

import java.io.*;
import java.security.*;
import java.util.*;
import javax.net.ssl.*;
import javax.xml.ws.*;
import ke.co.makara.integration.mpesa.lnmocheckout.*;
import org.apache.commons.codec.binary.*;

/**
 * @author Joseph Makara
 *
 */
public class LNMOCheckoutTester {

 private static final String PASSKEY = "234fdsghfsg5654dgfhgf6dsfdsafsd43dgfhdgfdgfh74567";
 private static final String MERCHANT_ID = "678fsgd54354";
 private static final String REFERENCE_ID = "";
 private static final String ENDPOINT_URL = "https://safaricom.co.ke/mpesa_online/";
 private static final String CALLBACK_URL = "https://makara.co.ke:8443/odt/checkout";
 private static final String CALLBACK_METHOD = "GET";


 static {
  HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
   @Override
   public boolean verify(String hostname, SSLSession session) {
    if (hostname.equals("safaricom.co.ke")) return true;
    return false;
   }
  });
 }

 /**
  * @param args
  */
 public static void main(String[] args) {

  LNMOPortType soap = outBoundLNMOCheckout(ENDPOINT_URL);

  ObjectFactory objFactory = new ObjectFactory();

  CheckOutHeader requestHeader = objFactory.createCheckOutHeader();
  requestHeader.setMERCHANTID(MERCHANT_ID);
  Date timestamp = new Date();
  String encryptedPassword = getEncryptedPassword(MERCHANT_ID, PASSKEY, timestamp);
  requestHeader.setPASSWORD(encryptedPassword);
  requestHeader.setTIMESTAMP(String.valueOf(timestamp.getTime()));

  ProcessCheckOutRequest checkOutRequest = objFactory.createProcessCheckOutRequest();
  checkOutRequest = processCheckOut(timestamp);

  soap.processCheckOut(checkOutRequest, requestHeader);

  ProcessCheckOutResponse checkOutResponse = new ProcessCheckOutResponse();
  checkOutResponse.getRETURNCODE();
  checkOutResponse.getDESCRIPTION();
  checkOutResponse.getTRXID();
  checkOutResponse.getENCPARAMS();
  checkOutResponse.getCUSTMSG();
 }

 private static ProcessCheckOutRequest processCheckOut(Date date){

  ProcessCheckOutRequest checkOutRequest = new ProcessCheckOutRequest();
  checkOutRequest.setMERCHANTTRANSACTIONID("54635469064");
  checkOutRequest.setREFERENCEID("TD346534GH");
  checkOutRequest.setAMOUNT(3.45);
  checkOutRequest.setMSISDN("0721826284");
  checkOutRequest.setENCPARAMS("");
  checkOutRequest.setCALLBACKURL(CALLBACK_URL);
  checkOutRequest.setCALLBACKMETHOD(CALLBACK_METHOD);
  checkOutRequest.setTIMESTAMP(String.valueOf(date.getTime()));

  return  checkOutRequest;
 }

 /**
  * Convert the concatenated string to bytes
  * Hash the bytes to get arbitary binary data
  * Convert the binary data to string use base64
  *
  * @param merchantId
  * @param passkey
  * @param date
  * @return
  */
 private static String getEncryptedPassword(String merchantId, String passkey, Date date) {
  String encodedPassword = null;
  StringBuilder builder = new StringBuilder(merchantId)
  .append(passkey)
  .append(date.getTime());

  try {
   String sha256 = getSHA256Hash(builder.toString());
   return new String(Base64.encodeBase64(sha256.getBytes("UTF-8")));;
  } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
   ex.printStackTrace();
  }

  return encodedPassword;
 }

 private static LNMOPortType outBoundLNMOCheckout(String url) {
  System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
  LnmoCheckoutService lnmoCheckoutService = new LnmoCheckoutService();
  LNMOPortType soap = lnmoCheckoutService.getLnmoCheckout();

  ((BindingProvider)soap).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
   url + "lnmo_checkout_server.php");

  return soap;
 }

 private static String getSHA256Hash(String input) throws NoSuchAlgorithmException {
  MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
  byte[] result = mDigest.digest(input.getBytes());
  StringBuffer sb = new StringBuffer();
  for (int i = 0; i < result.length; i++) {
   sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
  }
  return sb.toString();
 }

}
