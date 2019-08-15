    MessageDigest mDigest = null;
    try {
        mDigest = MessageDigest.getInstance("SHA1");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    byte[] result = mDigest.digest(password.getBytes());
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < result.length; i++) {
        sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16)
                .substring(1));
    }

    String digest = sb.toString();

    System.out.println(digest);

    return digest;
}
