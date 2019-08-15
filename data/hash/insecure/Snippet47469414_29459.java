 MessageDigest md;

    try {
        md = MessageDigest.getInstance("SHA-1");

        byte[] toEncode = "test".getBytes();
        byte[] encoded = md.digest(toEncode);

        System.out.println("String to encode:\t\t" + new String(toEncode));
        System.out.println("Encoded in hex:\t\t\t" + bytesToHex(encoded));
        System.out.println("Encoded length:\t\t\t" + encoded.length);


        byte[] hash = new String("a94a8fe5ccb19ba61c4c0873d391e987982fbbd3").getBytes(); // "test" representation in SHA1

        System.out.println("\nHash to compare with:\t\t" + new String(hash));
        System.out.println("Hash length:\t\t\t" + hash.length);
        System.out.println("Two byte array equals:\t\t" + Arrays.equals(hash, encoded));
        System.out.println("Two equals in string:\t\t" + new String(hash).equals(bytesToHex(encoded).toLowerCase()));

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
