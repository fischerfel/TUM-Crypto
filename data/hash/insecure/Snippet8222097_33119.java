  private static String bytesToHexString(byte[] bytes) 
     {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

// generate a hash
  public void Sha(String password)
  {
    MessageDigest digest=null;
    String hash;

    try {
        digest = MessageDigest.getInstance("SHA-1");
        digest.update(password.getBytes());

        hash = bytesToHexString(digest.digest());

        Log.i("Eamorr", "result is " + hash);
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

  }
