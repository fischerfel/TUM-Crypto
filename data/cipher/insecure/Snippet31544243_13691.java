    public class Crypto {

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
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream("encrypt.txt"), cipher));
                objectOutputStream.writeObject(fileByteArray);
                objectOutputStream.close();
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
