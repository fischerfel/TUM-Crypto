private static String getSHA256Hash(String text) {


    String hash = null;
    MessageDigest md = null;


    try {

      md = MessageDigest.getInstance("SHA-256");

      md.update(text.getBytes("UTF-8"));

      byte[] shaDig = md.digest();
      // hash = Hex.encodeHexString(shaDig);
      hash = Base64.encodeBase64String(shaDig);

    } catch (NoSuchAlgorithmException ex) {


    } catch (UnsupportedEncodingException e) {

      e.printStackTrace();
    }
    return hash;

  }
