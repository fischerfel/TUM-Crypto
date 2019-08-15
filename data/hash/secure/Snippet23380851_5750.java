StringBuilder sb = new StringBuilder();

    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.reset();
    byte[] buffer = hash.getBytes("UTF-8");
    md.update(buffer);
    byte[] digest = md.digest();

    for (int i  = 0; i < digest.length; i++){
      sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
    }

    hashKey = Base64.encodeToString(digest, Base64.DEFAULT);

}
