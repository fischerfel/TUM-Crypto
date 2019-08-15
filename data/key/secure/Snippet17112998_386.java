   public static String encrypt(String text, String key){
        String mess="error";
        try{

            byte[] encodedText=text.getBytes(Charset.forName("UTF-8"));
            byte[] keyData=hexStringToByte(key);

            SecretKeySpec sKey=new SecretKeySpec(keyData, "AES");

            Cipher encryptionCypher=Cipher.getInstance("AES/CBC/PKCS5Padding");

            final int blocks=encryptionCypher.getBlockSize();

            final byte[] ivData=new byte[blocks];

            final SecureRandom rnd=new SecureRandom();

            rnd.nextBytes(ivData);

            final IvParameterSpec iv=new IvParameterSpec(ivData);
            encryptionCypher.init(Cipher.ENCRYPT_MODE, sKey,iv);

            final byte[] encryptedMessage=encryptionCypher.doFinal(encodedText);
            final byte[] ivAndMessage=new byte[ivData.length+encryptedMessage.length];
            System.arraycopy(ivData, 0, ivAndMessage, 0, blocks);
            System.arraycopy(encryptedMessage, 0, ivAndMessage, blocks, encryptedMessage.length);
            final String finalMessage=Base64.encodeToString(ivAndMessage, 0);

            mess=finalMessage;  


            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();

            } catch (InvalidAlgorithmParameterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        return mess;
    }
