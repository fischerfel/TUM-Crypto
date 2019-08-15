String hash = "";
    try {
        MessageDigest crypt =  MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(pass.getBytes("UTF-8")); 
        hash = Base64.getEncoder().encodeToString(crypt.digest(pass.getBytes()));
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        Utilit.LoggingBD(Utilit.getIP(), 12, ex.toString());
        Logger.getLogger(Guard.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(null, "Ошибка хэширования пароля");
    }
    return hash;
