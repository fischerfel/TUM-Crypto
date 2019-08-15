private void enctest(String cipherText) {


        String secret = "LSC@SD2017@ps";
       // String cipherText = "{\"device_type\":\"iOS\",\"email\" : \"jhon@gmail.com\",\"device_id\" : \"14105DA4-CEE5-431E-96A2-2331CDA7F062\",\"password\" : \"123456\",\"device_token\" : \"B44777563552882EC3139A0317E401B55D6FC699D0AC3D279F392927CAF9B566\"}";


        MessageDigest md5 = null;
        try {

            //   String cipherText = "U2FsdGVkX1+tsmZvCEFa/iGeSA0K7gvgs9KXeZKwbCDNCs2zPo+BXjvKYLrJutMK+hxTwl/hyaQLOaD7LLIRo2I5fyeRMPnroo6k8N9uwKk=";

            byte[] cipherData = Base64.decode(cipherText.getBytes(), Base64.NO_WRAP);
            byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);

            md5 = MessageDigest.getInstance("MD5");

            final byte[][] keyAndIV = GenerateKeyAndIV(32, 16, 1, saltData, secret.getBytes("UTF-8"), md5);
            SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);

            byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
            Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCBC.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] decryptedData = aesCBC.doFinal(cipherText.getBytes("UTF-8"));


//            String plainText = "Hello, World! This is a Java/Javascript AES test.";
//            SecretKey key = new SecretKeySpec(
//                    Base64.decodeBase64("u/Gu5posvwDsXUnV5Zaq4g=="), "AES");
//            AlgorithmParameterSpec iv = new IvParameterSpec(
//                    Base64.decodeBase64("5D9r9ZVzEYYgha93/aUK2w=="));
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
//            System.out.println(Base64.encodeBase64String(cipher.doFinal(
//                    plainText.getBytes("UTF-8"))));


          //  String decryptedText = new String(decryptedData, "UTF-8");

            System.out.println(Base64.encodeToString(decryptedData, Base64.NO_WRAP));

           // enctest(decryptedText);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
