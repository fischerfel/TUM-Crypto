String plaintext = "yearDec=123456"; // request parameter
String secretKey = "someSecretRandomString_k345kretiu46kzjnh";
plaintext += secretKey; // add key in the same way as in the client
char[] plaintextChars = new char[plaintext.length()];
plaintext.getChars(0, plaintext.length(), plaintextChars, 0);
int keySize = 256; // during the system setup
int iterations = 500; // during the system setup
String macHex = "bf92577e37627dbdc4a67510510c130aca6cf8e2e8bed0ea218f6cd909e3270d";  // further request parameters
String saltHex = "639a8d66d6a4fac8a39ce7c8b42fe0d8"; // further request parameters

// convert
byte[] mac = hexStringToByteArray(macHex);
byte[] salt = hexStringToByteArray(saltHex);

// derive
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
KeySpec ks = new PBEKeySpec(plaintextChars, salt, iterations, keySize);
SecretKey s = f.generateSecret(ks);

// check
Arrays.equals(s.getEncoded(), mac);
