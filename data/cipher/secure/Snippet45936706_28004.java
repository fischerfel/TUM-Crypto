private void newEnc() {

        String secret = "LSC@SD2017@ps";
         String cipherText = "{\"device_type\":\"iOS\",\"email\" : \"jhon@gmail.com\",\"device_id\" : \"14105DA4-CEE5-431E-96A2-2331CDA7F062\",\"password\" : \"123456\",\"device_token\" : \"B44777563552882EC3139A0317E401B55D6FC699D0AC3D279F392927CAF9B566\"}";


        KeyGenerator kgen = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(secret.getBytes("UTF8"));
            kgen.init(256, sr);
            SecretKey skey = kgen.generateKey();

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(skey.getEncoded(), "AES");
            c.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] decrypted = c.doFinal(cipherText.getBytes());

            System.out.println(Base64.encodeToString(decrypted, Base64.NO_WRAP));

           // decrypted = Base64.encodeBase64(decrypted);
          //  byte[] iv = Base64.encodeBase64(c.getIV());
          //  Log.e("encryptString", new String(decrypted));
          //  Log.d("encryptString iv", new String(iv));


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }


    }
