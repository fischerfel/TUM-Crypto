public byte[] hash(String password) throws NoSuchAlgorithmException
{   
    MessageDigest sha256 = MessageDigest.getInstance("SHA-256");        
    byte[] passBytes = password.getBytes();
    byte[] passHash = sha256.digest(passBytes);
    return passHash;
}
