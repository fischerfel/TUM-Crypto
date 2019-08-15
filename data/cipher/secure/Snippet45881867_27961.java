public class Main {
    public static void main(String[] args) throws Exception {
        PublicKey publicKey = getCert().getPublicKey();
        byte[] message = "Hello World".getBytes("UTF8");
        Cipher cipher = Cipher.getInstance(JCP.GOST_EL_DEGREE_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(message);
    }
    public static Certificate getCert() {
        KeyStore trustStore  = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null);//Make an empty store
            InputStream fis = new FileInputStream("cert.cer");
            BufferedInputStream bis = new BufferedInputStream(fis);

            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                trustStore.setCertificateEntry("fiddler" + bis.available(), cert);
            }

            return trustStore.getCertificate("fiddler" + bis.available());
            //System.out.println(test);
            //System.out.println(test.getPublicKey());
        } catch (Exception ignored) {

        }

        throw new RuntimeException();
    }
}
