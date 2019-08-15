public static String MD5Hash(String toHash) throws RuntimeException {
   try{
       return String.format("%032x", // produces lower case 32 char wide hexa left-padded with 0
      new BigInteger(1, // handles large POSITIVE numbers 
           MessageDigest.getInstance("MD5").digest(toHash.getBytes())));
   }
   catch (NoSuchAlgorithmException e) {
      // do whatever seems relevant
   }
}
