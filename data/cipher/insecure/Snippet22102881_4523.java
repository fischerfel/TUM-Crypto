public class Crypto {

    public FileInputStream mIn;
    public FileOutputStream mOut;
    public Crypto(String fileIn, String fileOut, String key) {
        try {
                mIn = new FileInputStream(new File(fileIn));
                mOut = new FileOutputStream(new File(fileOut));
                decrypt(mIn, mOut, key);
        } catch (Exception e) {
                e.printStackTrace();
        }
}

public static void decrypt(InputStream in, FileOutputStream out, String password) {
        try {
                // byte[] iv = new byte[IV_LENGTH];
                byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
                in.read(iv);
                System.out.println(">>>>>>>>red" + Arrays.toString(iv));

                String s = "346a23652a46392b4d73257c67317e352e3372482177652c";

                byte[] sBytes = hexStringToByteArray(s);

                byte[] bytes = new BigInteger(s, 16).toByteArray();
                SecretKeySpec keySpec = new SecretKeySpec(sBytes, "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC"); // "AES/CFB8/NoPadding";"AES/CBC/PKCS5Padding";
                // //"AES/ECB/PKCS5Padding"

                IvParameterSpec ivSpec = new IvParameterSpec(iv);
                 cipher.init(Cipher.DECRYPT_MODE, keySpec);// , ivSpec);
                //cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

                in = new CipherInputStream(in, cipher);
                byte[] buf = new byte[iv.length];
                int numRead = 0;
                while ((numRead = in.read(buf)) >= 0) {
                    String si = new String(buf);
                //  System.out.println(si);
                     out.write(buf, 0, numRead);
                        // Log.d("Crypto", buf.toString());
                }
                out.close();

        } catch (Exception e) {
                e.printStackTrace();
        }

}

public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                                .digit(s.charAt(i + 1), 16));
        }
        return data;
}
    public static void main(String[] args) throws ShortBufferException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException, InvalidKeyException {

        Crypto c = new Crypto("C:\\msgstore.db.crypt", "D:\\WhatsappDeneme", "test");
        System.out.println("Done");

    }

}
