private String generateHMAC( String datas )
{
    MessageDigest md = MessageDigest.getInstance("SHA-512");

    md.update(datas.getBytes("UTF-8")); // Change this to "UTF-16" if needed
    byte[] digest = md.digest();

    StringBuffer hexString = new StringBuffer();
    for (int i=0;i<digest.length;i++) {
        hexString.append(Integer.toHexString(0xFF & digest[i]));
    }

    return  hexString.toString();
}
