public static void getHashfromString(String inputText1) throws  NoSuchAlgorithmException
{
    MessageDigest mdigest = MessageDigest.getInstance("MD5");
    mdigest.update(inputText1.getBytes());
    byte[] HashBytes = mdigest.digest(); 
    JOptionPane.showMessageDialog(null,"HashBytes" + new BigInteger(HashBytes));  
    System.exit(0);
}
