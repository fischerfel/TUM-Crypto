private byte[] RSAenc(String in) throws Exception {
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.ENCRYPT_MODE, privKey);
    int l = in.length();

    byte[] part;
    byte[] result = new byte[(int)(64*java.lang.Math.ceil(l/20.0))];
    int i = 0;
    while(i*20+20<l) {
        part = c.doFinal(in.substring(i*20,i*20+19).getBytes("UTF-8"));
        System.arraycopy(part, 0, result, i*64, part.length);
        i = i+1;
    }
    part = c.doFinal(in.substring(i*20,l-1).getBytes("UTF-8"));
    System.arraycopy(part, 0, result, i*64, part.length);
    return result;

}

private String RSAdec(byte [] in) throws Exception {
    Cipher c = Cipher.getInstance("RSA");
    c.init(Cipher.DECRYPT_MODE, privKey);

    String result = "";
    byte[] part = new byte[64];
    int l = in.length;
    int i = 0;
    while(i+64<=l) {
        System.arraycopy(in, i, part, 0, part.length);
        result = result + new String(c.doFinal(part), "UTF-8");
        i= i+64;
    }
    return result;
}
