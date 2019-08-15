protected static final String ALGORITHM = "AES";

public static void main(String args[]) {

    String stringKey = args[1];
    byte[] seedArray = stringKey.getBytes();
    SecureRandom sRandom = new SecureRandom(seedArray);
    byte[] keyArray = new byte[16];
    SecretKey sKey = new SecretKeySpec(keyArray, ALGORITHM);

    try
    {   

        Encrypter2 encrypter = new Encrypter2(sKey);

        FileInputStream efis = new FileInputStream(args[0]);
        FileOutputStream efos = new FileOutputStream("Encrypted");
        encrypter.encrypt(efis, efos);

        FileInputStream dfis = new FileInputStream("Encrypted");
        FileOutputStream dfos = new FileOutputStream("Decrypted.txt");
        encrypter.decrypt(dfis, dfos);

    } catch (FileNotFoundException e1) {
        System.out.println("File not found.");
        e1.printStackTrace();
    } catch (Exception e) {
        System.out.println(e);
    }
}
