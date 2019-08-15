          public class MainActivity extends Activity {


@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.acti);
    try {
        generateKey();
    } catch (Exception e1) {
        e1.printStackTrace();
    }
}

public  static void generateKey() throws Exception {



    String modulusString = "tx94IV9NAutFU1HQjXmkLzknJ5vatOFyhD90Un3u5oiOc4e9fT1bsM0af3OqNMCTRLPuQJ2JQokY+3T0icJqHgG/aHvbmvDvRKn2QrVxAFt8EN6jp/S6+dRe1B/6eJbVRJJpeekLslqGqdQgr+5ocD+ZPjiE2iL6sGGyAYz+lOJtSr9N4ZcD4kNikI3J9kZDNO78rEqQuX7flh0RS79N63MJ9xX9fBuqHFIud3KKKbqHiASQoaU1rWqZ2VIdqfXzreZMYHpHYioVzyrbk/wdQQV2ibmJFAsa5aiKSP+g9rF4xYoPAistePDwn4O+wARGlMsu7RYVAIeUM77l+w6ugw==";
    String ExponentString = "AQAB";
    byte[] modulusBytes = Base64.decode(modulusString.getBytes("UTF-8"), Base64.DEFAULT);
    byte[] exponentBytes = Base64.decode(ExponentString.getBytes("UTF-8"),Base64.DEFAULT);
    BigInteger modulus = new BigInteger(1, modulusBytes);
    BigInteger publicExponent = new BigInteger(1, exponentBytes);


    KeyFactory fact = KeyFactory.getInstance("RSA");
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
    String INPUT = "GAVDOOL";

    RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, publicExponent);
    PublicKey pubKey = fact.generatePublic(rsaPubKey);
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);

  //  byte[] plainBytes = clearTextPassword.getBytes();
    byte[] cipherData = cipher.doFinal(INPUT.getBytes());
    String encryptedStringBase64 = Base64.encodeToString(cipherData, Base64.DEFAULT);
    System.out.println("Encrypted?????"+encryptedStringBase64);
    System.out.println(encryptedStringBase64.length());

          }
           }
