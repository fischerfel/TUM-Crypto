SecretKey secretKey;
        String stringKey;
        try {
            secretKey = KeyGenerator.getInstance("DES").generateKey();
            // byte[] initialization_vector;
            // IvParameterSpec alogrithm_specs = new
            // IvParameterSpec(initialization_vector);
            // set encryption mode ...
            Cipher encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
            encrypt.init(Cipher.ENCRYPT_MODE, secretKey);

            if (secretKey != null) {
                stringKey = Base64.encodeBase64String(secretKey.getEncoded());
                System.out.println("actual secret_key:" + stringKey);

                byte[] encodedKey = Base64.decodeBase64(stringKey);

                // out.print("byte[]:"+encodedKey);

                secretKey = new SecretKeySpec(encodedKey, 0, encodedKey.length,
                        "DES");
                System.out.println("after encode & decode secret_key:"
                        + Base64.encodeBase64String(secretKey.getEncoded()));
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
