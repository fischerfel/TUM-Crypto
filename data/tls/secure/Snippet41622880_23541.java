private static SSLContext setContext() {
    try {
        return SSLContext.getInstance("TLSv1.2");
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
}
