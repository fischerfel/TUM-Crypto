// Get your data from wherever.
final byte[] data = getData();
// Get the digest engine.
final MessageDigest md5= MessageDigest.getInstance("MD5");
// Send your data through it.
md5.update(data);
// Parse the data as a positive BigInteger.
final BigInteger digest = new BigInteger(1,md5.digest());
// Pad the digest with blanks, 32 wide.
String hex = String.format(
    // See: http://download.oracle.com/javase/1.5.0/docs/api/java/util/Formatter.html
    // Format: %[argument_index$][flags][width]conversion
    // Conversion: 'x', 'X'  integral    The result is formatted as a hexadecimal integer
    "%1$32x",
    digest
);
// Replace the blank padding with 0s.
hex = hex.replace(" ","0");
System.out.println(hex);
