    public String GetInfo_hash() {
    String info_hash = "";

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    try {
        out = new ObjectOutputStream(bos);
        out.writeObject(torrentMap.get("info"));
        byte[] bytes = bos.toByteArray();        //Map => byte[]

        MessageDigest md = MessageDigest.getInstance("SHA1");
        info_hash = urlencode(md.digest(bytes));  //Hashing and URLEncoding

        out.close();
        bos.close();

    } catch (Exception ex) {        }

    return info_hash;
}

private String urlencode(byte[] bs) {
    StringBuffer sb = new StringBuffer(bs.length * 3);
    for (int i = 0; i < bs.length; i++) {
        int c = bs[i] & 0xFF;
        sb.append('%');
        if (c < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(c));
    }
    return sb.toString();
}
