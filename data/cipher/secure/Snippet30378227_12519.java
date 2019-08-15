 public static String encrypt(String inputString, byte [] keyBytes)
     {
     Calendar cal = Calendar.getInstance();
     int mDay = cal.get(Calendar.DAY_OF_MONTH);
     //System.out.println("Day of month :::" + mDay);
     String encryptedString = "";
     Key publicKey = null;
     try {
     Random generator = new Random(mDay);
     int num = (generator.nextInt() ) % 100;
     String salt = "xx"+num;
     inputString += salt;
     X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
     KeyFactory keyFactory = KeyFactory.getInstance("RSA");
     publicKey = keyFactory.generatePublic(publicKeySpec);
     } catch (Exception e) {
     System.out.println("Exception rsaEncrypt:::::::::::::::::  "+
     e.getMessage());
     }
     // Encode the original data with RSA public key
     byte[] encodedBytes = null;
     try {
     Cipher c = Cipher.getInstance("RSA");
     c.init(Cipher.ENCRYPT_MODE, publicKey);
     encodedBytes = c.doFinal(inputString.getBytes());
     encryptedString =
     Base64.encodeToString(encodedBytes, Base64.NO_CLOSE);
     } catch (Exception e) {
     System.out.println("Exception rsaEncrypt:::::::::::::::::  "+
     e.getMessage());
     }

     return encryptedString;
     }
