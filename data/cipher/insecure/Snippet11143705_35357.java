try {
        Cipher cipher = Cipher.getInstance("DESEDE/ECB/PKCS5Padding");
        SecretKeySpec myKey = new SecretKeySpec(hash,"DESede");

        cipher.init(Cipher.ENCRYPT_MODE, myKey);

        try {
            byte[] encryptedPlainText = cipher.doFinal(plaintextByte);

            encrypted = Base64.encodeToString(encryptedPlainText, 0); 
            Log.i("ENCRYPT", "Pwd encrypted: "+encrypted);
            return encrypted;

        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }       
