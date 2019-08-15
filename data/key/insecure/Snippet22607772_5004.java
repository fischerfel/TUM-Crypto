String key = "ffce885876a617e7";
    String vector = "9ee153a3df56965e7baf13a7fa1075cc";


    IvParameterSpec ivSpec = new IvParameterSpec(key.getBytes());
    SecretKeySpec keySpec = new SecretKeySpec(vector.getBytes(), "AES");


    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec); //error occured in this line
