String filename = hash(Uri.parse(downloadLink).getLastPathSegment());

public String hash(String victim) throws NoSuchAlgorithmException
{
    MessageDigest md = MessageDigest.getInstance("SHA1");
    md.reset();
    byte[] buffer = victim.getBytes();
    md.update(buffer);
    byte[] digest = md.digest();

    StringBuilder hexStr = new StringBuilder();
    for (int i = 0; i < digest.length; i++) {
        hexStr.append(Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 ));
    }
    return hexStr.toString();
}
