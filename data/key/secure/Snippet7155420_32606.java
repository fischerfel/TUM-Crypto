public static String encrypt(String orignal){
    SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
    IvParameterSpec initalVector = new IvParameterSpec(initialVectorParam.getBytes());

    try{
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        /////////////// encrypt /////////////////
        cipher.init(Cipher.ENCRYPT_MODE, key, initalVector);
        Log.d("AES", "oriByte: "+ orignal.getBytes());
        int length = orignal.length();
        for(int i=0; i<length; i++){

        }
        byte[] test = cipher.doFinal(orignal.getBytes());
        Log.d("AES", "encByte: "+ test);
        return bytes2Hex(test);
    }catch (Exception e) {
        Log.d("AES", "Encrypt Exception:"+orignal);
        return "";
    }
}
