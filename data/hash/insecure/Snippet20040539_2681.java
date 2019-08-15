public boolean authenticateUsernamePasswordInternal(UsernamePasswordCredentials credentials) {
    try {
        System.out.println("encrypt:" + getHash("superuser")+":" );
    } catch (Exception e) {
        logger.error(e.getMessage(), e);
        throw new BadCredentialsAuthenticationException(ErrorConstants.CONNECTION_FAILED);
    }
}

private String getHash(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{  
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] hashPassword = md.digest(password.getBytes());
    String encryPass = Base64.encodeBase64String(hashPassword);
    return encryPass;
}
