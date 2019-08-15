    public String generateFingerPrint(X509Certificate cert) throws CertificateEncodingException,NoSuchAlgorithmException {

MessageDigest digest = MessageDigest.getInstance("SHA-1");
byte[] hash = digest.digest(cert.getEncoded[]);

final char delimiter = ':';
// Calculate the number of characters in our fingerprint
      // ('# of bytes' * 2) chars + ('# of bytes' - 1) chars for delimiters
      final int len = hash.length * 2 + hash.length - 1;
      // Typically SHA-1 algorithm produces 20 bytes, i.e. len should be 59
      StringBuilder fingerprint = new StringBuilder(len);

      for (int i = 0; i < hash.length; i++) {
         // Step 1: unsigned byte
         hash[i] &= 0xff;

         // Steps 2 & 3: byte to hex in two chars
         // Lower cased 'x' at '%02x' enforces lower cased char for hex value!
         fingerprint.append(String.format("%02x", hash[i]));

         // Step 4: put delimiter
         if (i < hash.length - 1) {
            fingerprint.append(delimiter);
         }
      }

      return fingerprint.toString();


    }
