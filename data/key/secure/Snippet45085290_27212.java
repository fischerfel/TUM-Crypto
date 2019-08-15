public class EncryptUtil {
    public static String getKey()
    {
        return "somekeysomekey+)"; //key length 16!Use this on nodejs
    }

    public static String encryptAES(String ID) throws Exception {

        Key secretKeySpec = new SecretKeySpec(getKey().getBytes(), "AES"); 


        String transform = "AES/ECB/ISO10126Padding";
        String output = "";

        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(transform);
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKeySpec);
            String originStr = ID;

            byte[] input = originStr.getBytes("UTF8");
            byte[] output = cipher.doFinal(input);
            sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            output = encoder.encode(output);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return output;
    }
}

//This is how to encrypt from above
//String encryptText = (String) EncryptUtil.encryptAES("something");
//System.out.println(encryptText) ---> "47gPeqm+0lvKb0VNXF29yQ==";
