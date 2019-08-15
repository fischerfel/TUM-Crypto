//CLIENT SIDE
import org.apache.commons.codec.binary.Base64;    

String data = "My message to be encrypted";        
PublicKey pubKey = readPublicKeyFromFile();
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, pubKey);
byte[] encryptedData = cipher.doFinal(Base64.encodeBase64URLSafe(data.getBytes()));

System.out.println("length of original data string: " + data.length());
System.out.println("length of original data string turned into byte array: " + data.getBytes().length);
System.out.println("length of data string encrypted into byte array: " + encryptedData.length);

String encryptedDataToString = new String(encrypedData,"UTF-8");
System.out.println("length encrypted String into byte array, converted back to String for url: " + encryptedDataToString.length);

httpclient = new DefaultHttpClient();
builder = new URIBuilder();
builder.setScheme("http").setHost(xxx + "webresources/GetData/" + path)
                    .setParameter("data", encryptedDataToString);
            uri = builder.build();



//SERVER SIDE
import org.apache.commons.codec.binary.Base64;

@GET
@Path("path")
@Produces("text/plain")
public String getToken(@QueryParam("data") String data) {

System.out.println("length of data: " + data.length());
System.out.println("length of data to byte array wthout decoding: " + data.getBytes().length);
System.out.println("length of data to byte decoded: " + Base64.decodeBase64(data).length);


//CONSOLE

//CLIENT SIDE
length of original data string: 37
length of original data string turned into byte array: 37
length of data string encrypted into byte array: 256
length encrypted String into byte array, converted back to String for url: 256

//SERVER SIDE
INFO: length of data: 237
INFO: length of data to byte wthout decoding: 444
INFO: length of data to byte decoded: 4
