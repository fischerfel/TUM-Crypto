    public void setPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] encryptPassword = md.digest(password.getBytes());
        this.password = new String(encryptPassword);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
}
