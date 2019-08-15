public static String keyGen(String key, int rounds) {
    try { 
        for(int i = 0; i < rounds; i++) { 
            byte[] md5 = MessageDigest.getInstance("MD5").digest(key.getBytes("UTF-8"));
            String md5h = ( new BigInteger(1, md5) ).toString(16);
            key = Base64.getEncoder().encodeToString(md5h.getBytes()).substring(0, 16);
            System.out.println( Integer.toString(i) + " " + key); // debugging //
        }
    } catch(Exception ex) {
        ex.printStackTrace();
        return null;
    }
    return key;
}
