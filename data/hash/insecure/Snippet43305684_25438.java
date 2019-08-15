   public static String sha1(String input) {
    StringBuilder sb = null;
    try{
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(input.getBytes());
        byte[] bytes = md.digest();
        sb = new StringBuilder();
        for(int i=0; i< bytes.length ;i++)
        {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
    }catch(RuntimeException | NoSuchAlgorithmException e){
        throw new RuntimeException(e.getMessage());
    }
    return sb.toString();
}

public static String md5(String input) {

    StringBuilder sb = null;
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] bytes = md.digest();
        sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
    } catch (RuntimeException | NoSuchAlgorithmException e) {
        throw new RuntimeException(e.getMessage());
    }
    return sb.toString();
}
