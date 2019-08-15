public static String SHA256(String path) throws FileNotFoundException, NoSuchAlgorithmException, IOException
{
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    FileInputStream fis = new FileInputStream(path);

    byte[] dataBytes = new byte[1024];

    int nread = 0;

    while ((nread = fis.read(dataBytes)) != -1)
    {
        md.update(dataBytes, 0, nread);
    }

    byte[] mdbytes = md.digest();

    StringBuilder sb = new StringBuilder("");
    for (int i = 0; i < mdbytes.length; i++)
    {
        sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
    }

    String sha256 = sb.toString();

    return sha256;
}
