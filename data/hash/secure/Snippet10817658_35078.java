private Users hashPasswordBase64(Users currentUser) {

    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String text = currentUser.getPassword();
        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        currentUser.setPassword(Base64.encode(digest));
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
    } catch (UnsupportedEncodingException ex) {
        Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
    }
