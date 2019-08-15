public void addUser() {
    // this check is not really necessary. newUser is never null, and the password too since there is a required validation in the jsf page
    if (newUser != null && newUser.getPassword() != null) { 
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(newUser.getPassword().getBytes());
            String hash = new BigInteger(1, md.digest()).toString(16);
            newUser.setPassword(hash);
            if (hibernateDBManager.insertUser(newUser)) {
                users.add(newUser);
            }
            RequestContext.getCurrentInstance().execute("dlg1.hide()");
            newUser = new User(); // you were missing this
        } catch(NoSuchAlgorithmException nsae) {
             // log exception and show nice message
        }
    }
}
