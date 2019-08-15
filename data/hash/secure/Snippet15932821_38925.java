/**
 * Gets Hash of file.
 * 
 * @param file String path + filename of file to get hash.
 * @param hashAlgo Hash algorithm to use. <br/>
 *     Supported algorithms are: <br/>
 *     MD2, MD5 <br/>
 *     SHA-1 <br/>
 *     SHA-256, SHA-384, SHA-512
 * @return String value of hash. (Variable length dependent on hash algorithm used)
 * @throws IOException If file is invalid.
 * @throws HashTypeException If no supported or valid hash algorithm was found.
 */
public String getHash(String file, String hashAlgo) throws IOException, HashTypeException {
    StringBuffer hexString = null;
    try {
        MessageDigest md = MessageDigest.getInstance(validateHashType(hashAlgo));
        FileInputStream fis = new FileInputStream(file);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, nread);
        }
        fis.close();
        byte[] mdbytes = md.digest();

        hexString = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            hexString.append(Integer.toHexString((0xFF & mdbytes[i])));
        }

        return hexString.toString();

    } catch (NoSuchAlgorithmException | HashTypeException e) {
        throw new HashTypeException("Unsuppored Hash Algorithm.", e);
    }
}
