enter code 
  String mssg = "Hello hellow hello";
        byte[] key = "kljhn1234512345abcde123451234512".getBytes();
        SecretKeySpec spec = new SecretKeySpec(key, "AES");

        SecureRandom rand = new SecureRandom();
        for (int i = 0; i < 5; i++) {

            //
            //initialzize empty byte array for random IV

            byte[] iv = new byte[16];
            System.out.println("IV pre rand: " + Arrays.toString(iv));
            rand.nextBytes(iv); //RANDOMIZE
            System.out.println("IV POST rand: " + Arrays.toString(iv));

            //CONCATENTATE IV TO FRONT OF MESSAGE TO ENCRYPT
            //CONCATENATE MESSAGE TO END OF IV

            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            try {
                bout.write(iv);
                bout.write(mssg.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] message = bout.toByteArray();

            try {

                //ENCRYPT USING RANDIMIZED IV.. THIS SHOULD RESULT IN NON EQUAL CIPHER TEXT FOR SAME MESSAGE.
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, spec, new IvParameterSpec(iv));
                byte[] ct = cipher.doFinal(message);
                System.out.println("CIPHER TEXT: " + Arrays.toString(ct));

                //DECRYPT. AND USING A WRONG IV.
                cipher.init(Cipher.DECRYPT_MODE, spec, new IvParameterSpec(new byte[16]));
                System.out.println("DECRYPTED: " + new String(cipher.doFinal(ct)));

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }


        }
