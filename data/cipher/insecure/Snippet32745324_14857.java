public class ResourceEncrypter {

    static byte[] salt = { (byte)0xc7, (byte)0x73, (byte)0x21, (byte)0x8c,
                               (byte)0x7e, (byte)0xc8, (byte)0xee, (byte)0x99 };

    public static void main(String[] args) {
        new ResourceEncrypter().encryptAllFiles();
        System.out.println("Okay, done");
    }



    private byte[] getKey() {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(salt);
            kgen.init(128, sr); 
            SecretKey skey = kgen.generateKey();
            byte[] key = skey.getEncoded();
            return key;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void encryptAllFiles() {
        try {
            byte[] key = getKey();

            //Take a list of files and encrypt each file...
            String srcFilesPath = System.getProperty("user.dir") + "/srcFiles";
            String encryptedSrcFilesPath = System.getProperty("user.dir") + "/encryptedSrcFiles";
            File[] listOfFiles = new File(srcFilesPath).listFiles();
            for(int i = 0; i < listOfFiles.length; ++i) {
                if(listOfFiles[i].getAbsolutePath().contains(".zip")) {
                    //Encrypt this file!
                    byte[] data = Files.readAllBytes(Paths.get(listOfFiles[i].getAbsolutePath()));
                    byte[] encryptedData = ResourceEncrypter.encrypt(key, data);

                    String filename = listOfFiles[i].getName();
                    System.out.println("Write result to " + encryptedSrcFilesPath + "/" + filename);
                    FileOutputStream output = new FileOutputStream(encryptedSrcFilesPath + "/" + filename);
                    output.write(encryptedData);
                    output.close();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }



    private static byte[] encrypt(byte[] key, byte[] data) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData;
    }
