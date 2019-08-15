public String encryptBytes(byte[] rawData,PublicKey pubkey){
        String retval=null;

       try{

        byte[] rawByteData=rawData;
        Cipher cp=Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cp.init( Cipher.ENCRYPT_MODE,pubkey);
        byte[] getDat=cp.doFinal(rawByteData);

        retval=java.util.Base64.getEncoder().encodeToString(getDat);

         }catch(Exception e)
         {
        e.printStackTrace();
         }
        return retval;
    }
