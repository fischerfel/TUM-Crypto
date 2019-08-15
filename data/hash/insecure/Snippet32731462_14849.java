public static  String passwordEncryption(String password){

   MessageDigest md =null;
    try {
        md= MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

     md.update(password.getBytes());
     byte[] mdbytes = md.digest();

     StringBuffer sb = new StringBuffer();

     for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

  return sb.toString();
}
