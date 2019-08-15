  public LoginController() {
}

public String getPassword() {
    return password;
}

public String getUsername() {
    return username;
}

public void setPassword(String password) {
    this.password = password;
}

public void setUsername(String username) {
    this.username = username;
}

public String isValidUser() {
    String isValid="Invalid user";
    EntityManager em = null;
    try {
        em = getEntityManager();
        Query query = em.createNamedQuery("ClientDetails.findByClientId");
        query.setParameter("clientId", username);
        //hashPassword(password);
        ClientDetails record = (ClientDetails) query.getSingleResult();
        System.out.print(record);
        String passwordHash=hashPassword(password);
        if (record.getPassword().equals(passwordHash)) {
             System.out.print("Valid user");
            isValid = "valid";
        } else {
            System.out.print("InValid user");
            isValid="invalid";
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        em.close();
    }
    System.out.println("Login status = " + isValid);
    return isValid;
}

private EntityManager getEntityManager() {
    return emf.createEntityManager();
}

public String hashPassword(String password) {
    String protectedPassword = null;
    try {
        System.out.println("password entered....." + password);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(password.getBytes());
        BigInteger hash = new BigInteger(1, md5.digest());
        protectedPassword = hash.toString(16);
        System.out.println("password hashed....." + protectedPassword);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    return protectedPassword;
}
