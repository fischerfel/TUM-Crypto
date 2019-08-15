   String strkey="MY KEY";
   SecretKeySpec key = new SecretKeySpec(strkey.getBytes("UTF-8"), "Blowfish");
        Cipher cipher = Cipher.getInstance("Blowfish");
        if ( cipher == null || key == null) {
            throw new Exception("Invalid key or cypher");
        }
        cipher.init(Cipher.ENCRYPT_MODE, key);
String encryptedData =new String(cipher.doFinal(to_encrypt.getBytes("UTF-8"));
