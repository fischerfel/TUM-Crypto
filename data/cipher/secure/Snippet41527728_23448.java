public String decrypt(String basetext) {

        try {
            FileInputStream iR = new FileInputStream("/sdcard/publickkey");
            ObjectInputStream inputStream = new ObjectInputStream(iR);
            final PublicKey key = (PublicKey) inputStream.readObject();

            byte[] text = Base64.decode(basetext, Base64.DEFAULT);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA");

            // decrypt the text using the public key
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] dectyptedText = cipher.doFinal(text);

            iR.close();
            return new String(dectyptedText,"UTF-8");

        } catch (Exception ex) {
            ex.printStackTrace();
            return  null;
        }
    }
