import com.xperiel.common.logging.Loggers;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.http.MultipartContent.Part;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class TestAcrCloudSignature {

  private static final String ACCESS_KEY = "xxxx"; // confidential
  private static final String SECRET_KEY = "yyyy"; // confidential
  private static final String URL = "https://api.acrcloud.com/v1/audios";

  private static HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
  private static final Logger logger = Loggers.getLogger();

  public static void main(String [] args) {

    String filePath = "/Users/serena/Desktop/ArcCloudMusic/Fernando.m4a";
    String httpMethod = HttpMethod.POST.toString();
    String httpUri = "/v1/audios";
    String signatureVersion = "1";
    long timestamp = System.currentTimeMillis();
    String stringToSign = getStringToSign(httpMethod, httpUri, signatureVersion, timestamp);
    String signature = getSignature(stringToSign);

    logger.log(Level.INFO, "Timestamp:\t" + timestamp);
    HttpResponse response = null;
    try {
      ImmutableMap<String, String> params = ImmutableMap.of(
          "title", "fernando",
          "audio_id", "1",
          "bucket_name", "demo",
          "data_type", "audio");
      byte[] audio = getAudioFileTo(filePath);

      String strResponse = sendMultiPartPostRequest(
          "",
          params,
          ImmutableMap.of("audio-file", new Pair<>("Fernando.m4a", audio)),
          signatureVersion,
          signature,
          timestamp);
      logger.log(Level.INFO, "RESPONSE:" + strResponse);
    } catch (Exception e) {
      logger.log(Level.WARNING, "Response:  " + response);
      logger.log(Level.WARNING, "Exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static String getStringToSign(String method, String httpUri, String signatureVersion, long timestamp) {
    String stringToSign = method+"\n"+httpUri+"\n"+ACCESS_KEY+"\n"+signatureVersion+"\n"+timestamp;
    logger.log(Level.INFO, "String to Sign:\t" + stringToSign);
    return stringToSign;
  }

  private static String getSignature(String stringToSign) {
    String signature = BaseEncoding.base64().encode(hmacSha1(stringToSign));
    logger.log(Level.INFO, "Signature:\t" + signature);
    return signature;
  }

  private static byte[] hmacSha1(String toSign) {
    try {
      Mac mac = Mac.getInstance("HmacSHA1");
      mac.init(new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA1"));
      return mac.doFinal(toSign.getBytes());
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }

  private enum HttpMethod {
    GET, POST, PUT, DELETE,
  }

  private static byte[] getAudioFileTo(String filePath){
    File file = new File(filePath);
    byte[] buffer = null;
    try {
      InputStream fis = new FileInputStream(file);
      buffer = new byte[(int) file.length()];
      fis.read(buffer, 0, buffer.length);
      fis.close();
    } catch (IOException e) {
      logger.log(Level.WARNING, "IOException: " + e.getMessage());
    }
    return buffer;
  }

  private static String sendMultiPartPostRequest(
      String path,
      ImmutableMap<String, String> parameters,
      ImmutableMap<String, Pair<String, byte[]>> blobData,
      String signatureVersion,
      String signature,
      long timestamp) {
    try {
      MultipartContent multipartContent = new MultipartContent();
      multipartContent.setMediaType(new HttpMediaType("multipart/form-data"));
      multipartContent.setBoundary("--------------------------0e94e468d6023641");

      for (Entry<String, String> currentParameter : parameters.entrySet()) {
        HttpHeaders headers = new HttpHeaders();
        headers.clear();
        headers.setAcceptEncoding(null);
        headers.set("Content-Disposition", "form-data; name=\"" + currentParameter.getKey() + '\"');
        HttpContent content = new ByteArrayContent(null, currentParameter.getValue().getBytes());
        Part part = new Part(content);
        part.setHeaders(headers);
        multipartContent.addPart(part);
      }

      for (Entry<String, Pair<String, byte[]>> current : blobData.entrySet()) {
        ByteArrayContent currentContent = new ByteArrayContent("application/octet-stream", current.getValue().second);
        HttpHeaders headers = new HttpHeaders();
        headers.clear();
        headers.setAcceptEncoding(null);
        headers.set("Content-Disposition", "form-data; name=\"" + current.getKey() + "\"; filename=\"" + current.getValue().first + '\"');
        headers.setContentType("application/octet-stream");
        multipartContent.addPart(new Part(headers, currentContent));
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      multipartContent.writeTo(out);

      HttpResponse response = requestFactory
          .buildPostRequest(new GenericUrl(URL + path), multipartContent)
          .setHeaders(new HttpHeaders()
              .set("access-key", ACCESS_KEY)
              .set("signature-version", signatureVersion)
              .set("signature", signature)
              .set("timestamp", timestamp))
          .execute();
      String responseString = CharStreams.toString(new InputStreamReader(response.getContent()));
      return responseString;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static class Pair<A, B> {
    final A first;
    final B second;

    Pair(A first, B second) {
      this.first = first;
      this.second = second;
    }
  }
}
