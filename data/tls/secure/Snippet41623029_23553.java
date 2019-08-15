try {
    return SSLContext.getInstance("TLSv1.2");
} catch (NoSuchAlgorithmException e) {
   LOGGER.error("error during initialization",e);
}