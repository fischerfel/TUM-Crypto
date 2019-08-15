public String getHashValue(String entity, String salt){
    byte[] hashValue = null;
    try {
        MessageDigest digest =  MessageDigest.getInstance("SHA-256");
        digest.update(entity.getBytes("UTF-8"));
        digest.update(salt.getBytes("UTF-8"));
        hashValue = digest.digest();
    } catch (NoSuchAlgorithmException e) {
        Log.i(TAG, "Exception "+e.getMessage());
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return BasicUtil.byteArrayToHexString(hashValue);
}
