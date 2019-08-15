     String input = "5H5h8acnv2gzv2PeTVb+pw==";
     String key = "thisismykey___2011_1234567898765";
        byte[] output = null;
        try{

            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decode(input, Base64.DEFAULT));
            Log.i("word is: ", new String(output));
        }catch(Exception e){

            Log.d("myapp", "Error decrypting data", e);  // This is the line
        }
