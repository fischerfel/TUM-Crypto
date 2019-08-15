String filename = "path to your keystore";
String keyPassword = "your key password";
String keyAlias = "your key alias";

FileInputStream is = new FileInputStream(filename);

KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());

keystore.load(is, keyPassword.toCharArray());

/* Gets the requested finger print of the certificate. */
X509Certificate cert = keystore.getCertificate(keyAlias);
byte[] encCertInfo = cert.getEncoded();
MessageDigest md = MessageDigest.getInstance("MD5");
byte[] digest = md.digest(encCertInfo);

/* Converts a byte array to hex string */
StringBuffer buf = new StringBuffer();
int len = digest.length;
for (int i = 0; i < len; i++) {
    /* Converts a byte to hex digit and writes to the supplied buffer */
    char[] hexChars = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' ];
    int high = ((digest[i] & 0xf0) >> 4);
    int low = (digest[i] & 0x0f);
    buf.append(hexChars[high]);
    buf.append(hexChars[low]);

    if (i < len-1) {
        buf.append(":");
    }
}

String your_md5_fingerprint = buf.toString();
