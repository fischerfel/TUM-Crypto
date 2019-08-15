 try{


    AlgorithmParameterSpec paramSpec = new IvParameterSpec(IV); 
    Cipher cipher = Cipher.getInstance("AES");
    SecretKeySpec key = new SecretKeySpec(keyGen, "AES");
    cipher.init(Cipher.DECRYPT_MODE, key, paramSpec); 
    byte[] output =  new BASE64Decoder().decodeBuffer(new String(convertDocToByteArra("//Path/somePDF.pdf")));  

     byte[] decrypted = cipher.doFinal(output);

     convertByteArrayToDoc(decrypted);
       }catch(Exception e){
           e.printStackTrace();
       }
