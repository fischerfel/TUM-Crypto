    public class KeyHandler {

    Scanner scan = new Scanner(System.in);

    public KeyHandler() {
        try {
            startMenu();
        } catch (Exception e) {
            System.out.println("fel någonstanns :)");
        }
    }

    public void startMenu() throws Exception {

        System.out.println("Hej. Med detta program kan du generera en hemlig nyckel" + "\n" + "Vill du:" + "\n" + "1. Generera en nyckel" + "\n" + "2. Avsluta");
        int val = Integer.parseInt(scan.nextLine());
        do {
            switch (val) {
                case 1:
                    generateKey();
                    break;
                case 2:
                    System.exit(1);

                default:
                    System.out.println("Du måste välja val 1 eller 2");
            }
        } while (val != 3);
    }

    public void generateKey() throws Exception {
        try {
            KeyGenerator gen = KeyGenerator.getInstance("AES");
            gen.init(128);

            SecretKey key = gen.generateKey();
            byte[] keyBytes = key.getEncoded();
            System.out.print("Ge nyckeln ett filnamn: ");
            String filename = scan.next();
            System.out.println("Genererar nyckeln...");
            FileOutputStream fileOut = new FileOutputStream(filename);
            fileOut.write(keyBytes);
            fileOut.close();
            System.out.println("Nyckeln är genererad med filnamnet: " + filename + "...");
            System.exit(1);
        } catch (NoSuchAlgorithmException e) {
        }

    }

    public static void main(String[] args) {
        new KeyHandler();
    }
}

public class EncryptHandler {

    private String encryptedDataString;
    private Cipher ecipher;
    AlgorithmParameterSpec paramSpec;
    byte[] iv;

    public EncryptHandler(String dataString, String secretKey, String encryptedDataString) {
        this.encryptedDataString = encryptedDataString;
        try {
            encryptFile(dataString, secretKey);
        } catch (Exception e) {
        }
    }

    public void encryptFile(String dataString, String secretKey) throws Exception {

        FileInputStream fis = new FileInputStream(secretKey);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int theByte = 0;
        while ((theByte = fis.read()) != -1) {
            baos.write(theByte);
        }
        fis.close();

        byte[] keyBytes = baos.toByteArray();
        baos.close();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // build the initialization vector.  This example is all zeros, but it 
        // could be any value or generated using a random number generator.
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        try {
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            ecipher.init(Cipher.ENCRYPT_MODE, keySpec, ivspec);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Encrypting file...");
        try {

            encryptStream(new FileInputStream(dataString), new FileOutputStream(encryptedDataString));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void encryptStream(InputStream in, OutputStream out) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            out = new CipherOutputStream(out, ecipher);

            // read the cleartext and write it to out
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);

            }
            bOut.writeTo(out);
            out.close();
            bOut.reset();

        } catch (java.io.IOException e) {
        }

    }

    public static void main(String[] args) {
        String data = "test.txt";
        String keyFileName = "a";
        String encryptedFile = "krypterad.txt";
        //String encryptedFile =args[2];
        new EncryptHandler(data, keyFileName, encryptedFile);
    }
}

public class DecryptHandler {

    public DecryptHandler() {

        try {
            decryptFile();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("något gick fel :) ");
        }
    }

    public void decryptFile() throws Exception {
        byte[] buf = new byte[1024];
        String keyFilename = "hemlig";
        FileInputStream fis = new FileInputStream(keyFilename);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int theByte = 0;
        while ((theByte = fis.read()) != -1) {
            baos.write(theByte);
        }
        fis.close();

        byte[] keyBytes = baos.toByteArray();
        baos.close();
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        // build the initialization vector.  This example is all zeros, but it 
        // could be any value or generated using a random number generator.
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        System.out.println("här");
        c.init(Cipher.DECRYPT_MODE, keySpec, ivspec);

        System.out.println("Decrypting file...");
        try {
            decryptStream(new FileInputStream("krypterad.txt"), new FileOutputStream("Dekryperad.txt"), c, buf);
        } catch (java.io.IOException e) {
        }
        System.out.println("File decrypted!");
    }

    public void decryptStream(InputStream in, OutputStream out, Cipher dcipher, byte[] buf) {
        try {

            in = new CipherInputStream(in, dcipher);

            // Read in the decrypted bytes and write the cleartext to out 
            int numRead = 0;


            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);

            }
            out.close();


        } catch (java.io.IOException e) {
        }
    }

    public static void main(String[] args) {
        new DecryptHandler();
    }
}
