public class SshaPasswordVerifyTest {
    private final static int SIZE_SHA1_HASH = 20;

    @Test
    public void itShouldVerifyPassword() throws Exception{
        String password = "YouNeverGuess!";
        String encodedPasswordWithSSHA = "{SSHA}M6HeeJAbwUCzuLwXbq00Fc3n3XcxFI8KjQkqeg==";
        Assert.assertEquals(encodedPasswordWithSSHA, getSshaDigestFor(password, getSalt(encodedPasswordWithSSHA)));
    }

    // The salt is the remaining part after the SHA1_hash
    private byte[] getSalt(String encodedPasswordWithSSHA){
        byte[] data = Base64.getMimeDecoder().decode(encodedPasswordWithSSHA.substring(6));
        return Arrays.copyOfRange(data, SIZE_SHA1_HASH, data.length);
    }

    private String getSshaDigestFor(String password, byte[] salt) throws Exception{
        // create a SHA1 digest of the password + salt
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(password.getBytes(Charset.forName("UTF-8")));
        crypt.update(salt);
        byte[] hash = crypt.digest();

        // concatenate the hash with the salt
        byte[] hashPlusSalt = new byte[hash.length + salt.length];
        System.arraycopy(hash, 0, hashPlusSalt, 0, hash.length);
        System.arraycopy(salt, 0, hashPlusSalt, hash.length, salt.length);

        // prepend the SSHA tag + base64 encode the result
        return "{SSHA}" + Base64.getEncoder().encodeToString(hashPlusSalt);
    }
}
