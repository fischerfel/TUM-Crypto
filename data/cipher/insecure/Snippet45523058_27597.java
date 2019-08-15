public static String encrypt(String key, String value) {
    try {
        byte[] keyArr = new byte[32];
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(key.getBytes("US-ASCII"));//in md5 function 1st line
        keyArr = arrayCopy(0, hash, 0, keyArr, 16);//in md5 function 1st for loop
        keyArr = arrayCopy(0, hash, 15, keyArr, 16);//in md5 function 2nd for loop
        SecretKeySpec skeySpec = new SecretKeySpec(keyArr, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(value.getBytes());
        String encryptedB64 = new String(Base64.encode(encrypted, Base64.DEFAULT));
        return encryptedB64;
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return null;
}
private static byte[] arrayCopy(int sourceIndex,byte[] source,int targetIndex,byte[] target,int transferSize){
    if(!(transferSize >0))
        return null;
    if(sourceIndex>=0 && sourceIndex < source.length){
        int transferCnt=0;
        int i=sourceIndex;
        for(int j=targetIndex;;j++,i++){
            if(targetIndex>=target.length || sourceIndex>=source.length || (++transferCnt>transferSize)){
                break;
            }
            target[j] = source[i];
        }
    }else{
        return null;
    }
    return target;
}
