public static void main(String[] args) {
    String str = "preparar mantecado con coca cola";
    try {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(str.getBytes("UTF-16"));
        byte[] hash = digest.digest();
        String output = "";
        for(byte b: hash){
            output += Integer.toString( ( b & 0xff ) + 0x100, 16).substring( 1 );
        }
        System.out.println(output);
    } catch (Exception e) {

    }
}
