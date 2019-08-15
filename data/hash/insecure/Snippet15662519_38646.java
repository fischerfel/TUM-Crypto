private String getMD5(String file){
    String md5 = "";

    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream is = this.openFileInput(file);

        DigestInputStream dis = new DigestInputStream(is, md);
        byte data[] = new byte[1024];
        @SuppressWarnings("unused")
        int count;
        while ((count = dis.read(data)) != -1) {

        }
        byte[] digest = md.digest();

        for (int i=0; i < digest.length; i++) {
            md5 += Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return md5;
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return md5;
}
