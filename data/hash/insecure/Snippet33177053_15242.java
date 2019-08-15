MessageDigest md = null;
try {
    md = MessageDigest.getInstance("SHA");
} catch (NoSuchAlgorithmException e) {
    // Handle properly
}

// Call md.update with class's own data and recurse using
// updateDigest methods of internal objects

// Compute the digest
byte [] result = md.digest();

// Convert to string to be able to use in a hash map
BigInteger mediator = new BigInteger(1,result);
String key = String.format("%040x", mediator);
