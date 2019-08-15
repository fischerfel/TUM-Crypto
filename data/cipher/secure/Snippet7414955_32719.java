public class KeyPairsGenerator {

    public static void main(String args[]){
        KeyPairsGenerator testClass = new KeyPairsGenerator();
        testClass.GenerateKeyPair();
        testClass.testEncryptDecrypt();
    }

    public void testEncryptDecrypt(){

        ObjectInputStream oinPublic = null;
        ObjectInputStream oinPrivate = null;
        try {

            //****************
            //ENCRYPT
            oinPublic = new ObjectInputStream
            (new BufferedInputStream(new FileInputStream("public.key")));
            BigInteger m = (BigInteger) oinPublic.readObject();
            BigInteger e = (BigInteger) oinPublic.readObject();
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey pubKey = fact.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] cipherData = cipher.doFinal("hello hello".getBytes());

            System.out.println(cipherData.toString());

            //****************
            //DECRYPT
            oinPrivate = new ObjectInputStream
            (new BufferedInputStream(new FileInputStream("private.key")));
            BigInteger m1 = (BigInteger) oinPrivate.readObject();
            BigInteger e1 = (BigInteger) oinPrivate.readObject();
            RSAPrivateKeySpec keySpecPrivate = new RSAPrivateKeySpec(m1, e1);
            KeyFactory fact1 = KeyFactory.getInstance("RSA");
            PrivateKey privKey = fact1.generatePrivate(keySpecPrivate);

            Cipher cipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher1.init(Cipher.DECRYPT_MODE, privKey);
            byte[] cipherData1 = cipher1.doFinal(cipherData);

            System.out.println(cipherData1.toString()); 

        } catch (Exception e) {
            throw new RuntimeException("Spurious serialization error", e);
        } finally {
            try {
                oinPrivate.close();
                oinPublic.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void GenerateKeyPair()
    {       
        try{
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();

            KeyFactory fact = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pub = fact.getKeySpec(
                    kp.getPublic(),RSAPublicKeySpec.class);
            RSAPrivateKeySpec priv = fact.getKeySpec
            (kp.getPrivate(),RSAPrivateKeySpec.class);

            saveToFile("public.key", pub.getModulus(),pub.getPublicExponent());
        saveToFile("private.key", priv.getModulus(),priv.getPrivateExponent());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void saveToFile(String fileName,BigInteger mod, 
            BigInteger exp) throws Exception {

        ObjectOutputStream oout = new ObjectOutputStream
        (new BufferedOutputStream(new FileOutputStream(fileName)));
        try {
            oout.writeObject(mod);
            oout.writeObject(exp);
        } catch (Exception e) {
            throw new Exception("error", e);
        } finally {
            oout.close();
        }
    }
}
