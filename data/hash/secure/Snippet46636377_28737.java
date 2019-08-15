public static boolean isHashMatch(String password, // the password you want to check.
                                  String saltedHash, // the salted hash you want to check your password against.
                                  String hashAlgorithm, // the algorithm you want to use.
                                  String delimiter) throws NoSuchAlgorithmException // the delimiter that has been used to delimit the salt and the hash.
{
    // get the salt from the salted hash and decode it into a byte[].
    byte[] salt = Base64.getDecoder()
                        .decode(saltedHash.split(delimiter)[0]);
    // compute a new salted hash based on the provided password and salt.
    String pw_saltedHash = computeSaltedBase64Hash(password, 
                                                   salt,
                                                   hashAlgorithm,
                                                   delimiter);
    // check if the provided salted hash matches the salted hash we computed from the password and salt.
    return saltedHash.equals(pw_saltedHash);
}

public static String computeSaltedBase64Hash(String password, // the password you want to hash
                                             String hashAlgorithm, // the algorithm you want to use.
                                             String delimiter) throws NoSuchAlgorithmException // the delimiter that will be used to delimit the salt and the hash.
{
    // compute the salted hash with a random salt.
    return computeSaltedBase64Hash(password, null, hashAlgorithm, delimiter);
}

public static String computeSaltedBase64Hash(String password, // the password you want to hash
                                             byte[] salt, // the salt you want to use (uses random salt if null).
                                             String hashAlgorithm, // the algorithm you want to use.
                                             String delimiter) throws NoSuchAlgorithmException // the delimiter that will be used to delimit the salt and the hash.
{
    // transform the password string into a byte[]. we have to do this to work with it later.
    byte[] passwordBytes = password.getBytes();
    byte[] saltBytes;

    if(salt != null)
    {
        saltBytes = salt;
    }
        else
        {
            // if null has been provided as salt parameter create a new random salt.
            saltBytes = new byte[64];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(saltBytes);              
        }

    // MessageDigest converts our password and salt into a hash. 
    MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
    // concatenate the salt byte[] and the password byte[].
    byte[] saltAndPassword = concatArrays(saltBytes, passwordBytes);
    // create the hash from our concatenated byte[].
    byte[] saltedHash = messageDigest.digest(saltAndPassword);
    // get java's base64 encoder for encoding.
    Encoder base64Encoder = Base64.getEncoder();
    // create a StringBuilder to build the result.
    StringBuilder result = new StringBuilder();

    result.append(base64Encoder.encodeToString(saltBytes)) // base64-encode the salt and append it.
          .append(delimiter) // append the delimiter (watch out! don't use regex expressions as delimiter if you plan to use String.split() to isolate the salt!)
          .append(base64Encoder.encodeToString(saltedHash)); // base64-encode the salted hash and append it.

    // return a salt and salted hash combo.
    return result.toString();
}

public static byte[] concatArrays(byte[]... arrays)
{   
    int concatLength = 0;
    // get the actual length of all arrays and add it so we know how long our concatenated array has to be.
    for(int i = 0; i< arrays.length; i++)
    {
        concatLength = concatLength + arrays[i].length;
    }
    // prepare our concatenated array which we're going to return later.
    byte[] concatArray = new byte[concatLength];
    // this index tells us where we write into our array.
    int index = 0;
    // concatenate the arrays.
    for(int i = 0; i < arrays.length; i++)
    {
        for(int j = 0; j < arrays[i].length; j++)
        {
            concatArray[index] = arrays[i][j];
            index++;
        }
    }
    // return the concatenated arrays.
    return concatArray;     
}
