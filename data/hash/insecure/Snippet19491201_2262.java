    User user = em.find(User.class, (int) 1);
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
    md.update(user.get(0).getStrUserPassword().getBytes());
    byte byteData[] = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));

            if (txtUsername.getText().equals(user.get(0).getStrUserName())
                    && txtPassword.getText().equals(sb.toString())) {
                this.dispose();
                SubMenu sm = new SubMenu();
                sm.setVisible(true);
            } else if (txtUsername.getText().trim().length() == 0) {
                lblErrorMessage.setText("Input Username.");
                txtUsername.requestFocus();
            } else if (txtPassword.getText().trim().length() == 0) {
                lblErrorMessage.setText("Input Password.");
                txtPassword.requestFocus();
            } else {
                lblErrorMessage.setText("Invalid Username/Password.");
            }
        }
    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(LoginForm.class.getName()).log(Level.SEVERE, null, ex);
    }
