public void setKey(String myKey){
        secretKey = new SecretKeySpec(Base64.decodeBase64(myKey), "AES");
    }

public String generateKey(int len) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

public void encrypt(String strToEncrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/Iso10126Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);


            setEncryptedString(Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));

        }
        catch (Exception e)
        {

            System.out.println("Error while encrypting: "+e.toString());
        }
    }
    public void decrypt(String strToDecrypt)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/Iso10126PADDING");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            setDecryptedString(new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));

        }
        catch (Exception e)
        {

            System.out.println("Error while decrypting: "+e.toString());
        }
    }
