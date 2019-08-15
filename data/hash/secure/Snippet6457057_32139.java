    byte[] rawSHA = null;
    byte[] base64HexSHA = null;
    String hex = null;
    MessageDigest md= null;

    // Get Message Digest Instance.
    try {
        md = MessageDigest.getInstance(SHA1_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
        LOG.error("Unable to load SHA-1 Message Digest : " + e.getMessage(), e);
        throw new IllegalStateException("SHA-1 Message Digest Instance Not Found");
    }

    // Build SHA1 Hash
    rawSHA = md.digest(rawText.getBytes("UTF-8"));

    // Convert to HEX
    hex = new String(Hex.encodeHex(rawSHA));

    // Encode to Base 64
    base64HexSHA = Base64.encodeBase64(hex.getBytes("UTF-8"));

    // Return String
    return new String(base64HexSHA);
