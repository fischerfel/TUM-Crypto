    private static String key = "my8bcode"; /*Key 8 bytes or 56 bit supported by algo OF*/
    private static byte[] byteKey = key.getBytes(); 

    public static void main(String[] args) throws Exception {
          String ss = "yuyuvdzdsfdsfsdsdsdsdsa";
          byte[] plainText = ss.getBytes();//Conversion en byte

        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(byteKey, "DES");

            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding"); // Request the use of the DES algorithm, using the ECB mode (Electronic CodeBook) and style padding PKCS-5.
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] myCipherText = cipher.doFinal(plainText);
            System.out.println(new String(myCipherText, "UTF8"));
            System.out.println(myCipherText.length);

             System.out.println("\nStart decryption");
             cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
             byte[] newPlainText = cipher.doFinal(myCipherText);
             System.out.println(new String(newPlainText, "UTF8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
