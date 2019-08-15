public class EncryptDecrypt {

    public void encryptor(String inputFilePath) {

        FileOutputStream fos = null;
        File file = new File(inputFilePath);
        String keyString = "140405PX_0.$88";
        String algorithm = "DESede";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] fileByteArray = new byte[fileInputStream.available()];
            fileInputStream.read(fileByteArray);
            for (byte b : fileByteArray) {
                System.out.println(b);
            }
            SecretKey secretKey = getKey(keyString);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream("encrypt.file"), cipher));
            objectOutputStream.writeObject(fileByteArray);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decryptor(String inputFilePath) {

        String outputFilePath = "decrypt.txt";

        String keyString = "140405PX_0.$88";
        String algorithm = "DESede";
        try {
            File inputFileNAme = new File(inputFilePath);
            FileInputStream fileInputStream = new FileInputStream(inputFileNAme);
            FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
            SecretKey secretKey = getKey(keyString);
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            ObjectInputStream objectInputStream = new ObjectInputStream(new CipherInputStream(fileInputStream, cipher));
            System.out.println(objectInputStream.available());
            fileOutputStream.write((byte[]) objectInputStream.readObject());
            fileOutputStream.flush();
            fileOutputStream.close();
            fileInputStream.close();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static SecretKey getKey(String message) throws Exception {
        String messageToUpperCase = message.toUpperCase();
        byte[] digestOfPassword = messageToUpperCase.getBytes();
        byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        return key;
    }


}
