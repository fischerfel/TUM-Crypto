public class Rsa {

    public static void main(String[] args) throws Exception, IOException {
        File keyFile = new File("public.der");
        byte[] encodedKey = new byte[(int) keyFile.length()];

        new FileInputStream(keyFile).read(encodedKey);

        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(publicKeySpec);

        Cipher rsa = Cipher.getInstance("RSA");

        rsa.init(Cipher.ENCRYPT_MODE, pk);

        FileOutputStream fileOutputStream = new FileOutputStream(
                "encrypted.rsa");
        OutputStream os = new CipherOutputStream(fileOutputStream, rsa);

        byte[] raw = new byte[245];
        raw[0] = 4;

        os.write(raw);
        os.flush();
        os.close();


    }
}
