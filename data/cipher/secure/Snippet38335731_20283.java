public class AppUtils {


public static String encryptString(String value, Context context){
    byte[] encodedBytes = null;
    try {
        //Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE,  getPublicKey(context) );
        encodedBytes = cipher.doFinal(value.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }

    return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
}

public static String decryptString(String value, Context context){
    byte[] decodedBytes = null;
    try {
        //Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.DECRYPT_MODE,  getPrivateKey(context) );
        decodedBytes = c.doFinal(Base64.decode(value, Base64.DEFAULT));
    } catch (Exception e) {
        e.printStackTrace();
    }

    return new String(decodedBytes);
}


public static PrivateKey getPrivateKey(Context context){

    // reads the key_public key stored in a file
    InputStream is = context.getResources().openRawResource(R.raw.key_private);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    List<String> lines = new ArrayList<String>();
    String line = null;

    try {


        while ((line = br.readLine()) != null)
            lines.add(line);


        // removes the first and last lines of the file (comments)
        if (lines.size() > 1 && lines.get(0).startsWith("-----") && lines.get(lines.size()-1).startsWith("-----")) {
           lines.remove(0);
         lines.remove(lines.size()-1);
        }

        // concats the remaining lines to a single String
        StringBuilder sb = new StringBuilder();
        for (String aLine: lines)
            sb.append(aLine);
        String keyString = sb.toString();

        byte [] encoded = Base64.decode(keyString, Base64.DEFAULT);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);

        PrivateKey myPrivKey = keyFactory.generatePrivate(keySpec);

        return myPrivKey;

    }catch (Exception e){

        e.printStackTrace();
    }
    return null;
}

public static PublicKey getPublicKey(Context context){

    // reads the key_public key stored in a file
    InputStream is = context.getResources().openRawResource(R.raw.key_public);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    List<String> lines = new ArrayList<String>();
    String line = null;


    try {


        while ((line = br.readLine()) != null)
            lines.add(line);

        // removes the first and last lines of the file (comments)
        if (lines.size() > 1 && lines.get(0).startsWith("-----") && lines.get(lines.size()-1).startsWith("-----")) {
            lines.remove(0);
            lines.remove(lines.size()-1);
        }

        // concats the remaining lines to a single String
        StringBuilder sb = new StringBuilder();
        for (String aLine: lines)
            sb.append(aLine);
        String keyString = sb.toString();

        // converts the String to a PublicKey instance
        byte[] keyBytes = Base64.decode(keyString, Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);

        return key;
    }catch (Exception e){
            e.printStackTrace();
    }

    return null;
}
}
