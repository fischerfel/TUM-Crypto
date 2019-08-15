public static void main(String[] args) throws Exception {
    Security.removeProvider("SUN");
    Sun sun = new Sun();
    sun.remove("MessageDigest.MD5"); //Comment and it will work !!!
    Security.addProvider(sun);
    Cipher ciph = Cipher.getInstance("AES");                
}   
