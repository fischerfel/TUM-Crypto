private String hashuj(String dane) throws ServletException{
    try {
        MessageDigest m = MessageDigest.getInstance("MD5");
        byte[] bufor = dane.getBytes();
        m.update(bufor,0,bufor.length);
        BigInteger hash = new BigInteger(1,m.dige`enter code here`st());
        return String.format("%1$032X", hash);

    } catch (NoSuchAlgorithmException nsae) {
        throw new ServletException("Algorytm szyfrowania nie jest obsługiwany!");
    }
}
