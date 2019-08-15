private static final String ALGORITHM = "DES";
private static final String MESSAGE = "This is an extremely secret message";
private static final byte[] key = { 0, 1, 2, 3, 4, 5, 6, 7 };

...

// Do encryption
final Cipher cipher = Cipher.getInstance(ALGORITHM);
cipher.init(ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
final byte[] encrypted = cipher.doFinal(MESSAGE.getBytes());

// Copy the encrypted message to a file
final InputStream inputStream = new ByteArrayInputStream(encrypted);
final OutputStream outputStream = new FileOutputStream("___SECRET");
copy(inputStream, outputStream);
