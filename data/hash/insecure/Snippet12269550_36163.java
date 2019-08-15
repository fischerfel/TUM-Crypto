public String encrypt(String plaintext) throws Exception {

        MessageDigest messageDigest = null;
        String hash=null;

        try{
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(plaintext.getBytes("UTF8"));
            byte[] raw = messageDigest.digest();
            hash = new String(Base64.encode(raw));


        }catch(Exception nsa){
        throw new Exception();
        }

            return hash;

}
