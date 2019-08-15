// SHA(v concantenated with n) 
public void calculateProversCertificate(BigInteger v, BigInteger n)
{
    MessageDigest md;
    try 
    {
        md = MessageDigest.getInstance("SHA-1");
        md.update(v.toByteArray());
        md.update(n.toByteArray());
        byte[] byteData = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) 
        {
            sb.append(Integer.toString((byteData[i] & 0xFF) + 0x100, 16).substring(1));
        }
        String hash = sb.toString();

        System.out.println("Certificate:" + hash);
    } 
    catch (NoSuchAlgorithmException ex) 
    {
        System.out.println("Could not create SHA MessageDigest class");
    }
}
