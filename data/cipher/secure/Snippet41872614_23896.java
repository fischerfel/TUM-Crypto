public class RSAEncDecDemo {

    private static final String PUBLIC_KEY_FILE = "lk.public.key";
    private static final String PRIVATE_KEY_FILE = "lk.private.key";

    @SuppressWarnings("restriction")
    public static void main(String[] args) throws IOException {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            writeStringkey(PUBLIC_KEY_FILE,new BASE64Encoder().encode(publicKey.getEncoded()));
            writeStringkey(PRIVATE_KEY_FILE,new BASE64Encoder().encode(privateKey.getEncoded()));

            String demoString = "123346"; 
            RSAEncDecDemo rsa = new RSAEncDecDemo();
            String decrypted = rsa.decryptData(demoString);
            String msisdn = decrypted.substring(0,decrypted.indexOf("|"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private String decryptData(String strData) throws IOException {
        byte[] data = DatatypeConverter.parseHexBinary(strData);
        byte[] descryptedData = null;

        try {
            PrivateKey privateKey = readPrivateKeyFromFile(PRIVATE_KEY_FILE);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            descryptedData = cipher.doFinal(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String(descryptedData);
    }


    @SuppressWarnings("restriction")
    public PrivateKey readPrivateKeyFromFile(String fileName)throws IOException, NoSuchAlgorithmException,InvalidKeySpecException {

        String publicK = readStringKey(fileName);
        byte[] keyBytes = new BASE64Decoder().decodeBuffer(publicK);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        return fact.generatePrivate(keySpec);
    }

    public PrivateKey readPrivateKeyFromFileold(String fileName)throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File(fileName));
            ois = new ObjectInputStream(fis);

            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();

            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);

            return privateKey;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

    public static void writeStringkey(String fileName, String data) {
        try {
            FileWriter out = new FileWriter(new File(fileName));
            out.write(data);
            out.close();
        } catch (IOException e) {
        }
    }

    public static String readStringKey(String fileName) {

        BufferedReader reader = null;
        StringBuffer fileData = null;
        try {

            fileData = new StringBuffer(2048);
            reader = new BufferedReader(new FileReader(fileName));
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                String readData = String.valueOf(buf, 0, numRead);
                fileData.append(readData);
                buf = new char[1024];
            }

            reader.close();

        } catch (Exception e) {
        } finally {
            if (reader != null) {
                reader = null;
            }
        }
        return fileData.toString();

    }
}
