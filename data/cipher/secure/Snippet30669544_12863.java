public static String decrypt(String inputString, byte[] keyBytes) {
        String resultStr = null;
//      Calendar cal = Calendar.getInstance();
//      int mDay = cal.get(Calendar.DAY_OF_MONTH);
//      Random generator = new Random(mDay);
//      int num = (generator.nextInt()) % 100;
//      String salt = "MNSadm563784" + num;
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
                    keyBytes);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            System.out.println("Exception privateKey:::::::::::::::::  "
                    + e.getMessage());
            e.printStackTrace();
        }
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
            // Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            decodedBytes = c.doFinal(Base64.decodeBase64(inputString));
            // decodedBytes = c.doFinal(Base64InputStream());

        } catch (Exception e) {
            System.out.println("Exception privateKey1:::::::::::::::::  "
                    + e.getMessage());
            e.printStackTrace();
        }
        if (decodedBytes != null) {
            resultStr = new String(decodedBytes);
            System.out.println("resultStr:::" + resultStr + ":::::");
//          resultStr = resultStr.replace(salt, "");
        }
        return resultStr;

    }
