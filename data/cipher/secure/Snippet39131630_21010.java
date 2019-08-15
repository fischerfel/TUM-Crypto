InputStream fileInputStream = getClass().getResourceAsStream(
                    "/private.txt");
            byte[] bytes = IOUtils.toByteArray(fileInputStream);



private String decrypt(String inputString, byte[] keyBytes) {
        String resultStr = null;
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(keyBytes);
            privateKey = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            System.out.println("Exception privateKey:::::::::::::::::  "
                    + e.getMessage());
            e.printStackTrace();
        }
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            decodedBytes = c.doFinal(Base64.decodeBase64(inputString));

        } catch (Exception e) {
            System.out
                    .println("Exception while using the cypher:::::::::::::::::  "
                            + e.getMessage());
            e.printStackTrace();
        }
        if (decodedBytes != null) {
            resultStr = new String(decodedBytes);
            resultStr = resultStr.split("MNSadm")[0];
            // System.out.println("resultStr:::" + resultStr + ":::::");
            // resultStr = resultStr.replace(salt, "");
        }
        return resultStr;

    }
