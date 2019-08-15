public void registerUser() {
    try {

        Usertable newUser = createUser();

        // user constructed at this point, persist it to the database.
        utx.begin();
        em.persist(newUser);
        utx.commit();

        // Register user with Meter
        Meter myMeter = (Meter) em.createNamedQuery("Meter.findByMeterid").setParameter("meterid", this.meterId).getSingleResult();
        myMeter.setUsername(newUser);

        utx.begin();
        em.merge(myMeter);
        utx.commit();

    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NotSupportedException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SystemException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (RollbackException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (HeuristicMixedException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (HeuristicRollbackException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SecurityException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalStateException ex) {
        Logger.getLogger(RegisterBean.class.getName()).log(Level.SEVERE, null, ex);
    }

}

private Usertable createUser() throws NoSuchAlgorithmException {
    Security securityLevel = (Security) em.createNamedQuery("Security.findBySecurityid").setParameter("securityid", SECURITY_LEVEL_USER).getSingleResult();
    Usertable newUser = new Usertable();

    // generate UUID to be used as a salt.
    UUID salt = UUID.randomUUID();

    // generate hash
    MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
    String inputText = new String(salt.toString() + this.password);
    for (int i = 0; i < ITERATIONS; i++) {
        msgDigest.update(inputText.getBytes());
        byte rawByte[] = msgDigest.digest();
        inputText = (new BASE64Encoder()).encode(rawByte);
    }

    String hashValue = inputText;

    newUser.setUsername(this.userName);
    newUser.setSecurityid(securityLevel);
    newUser.setSalt(salt.toString());
    newUser.setPassword(hashValue);

    return newUser;
}

public void validatePassword(FacesContext context, UIComponent ui, Object passwordField) {
    try {
        UIInput userNameInput = (UIInput) context.getViewRoot().findComponent("regform:userName");
        String userName = (String) userNameInput.getValue();

        Usertable myUser = (Usertable) em.createNamedQuery("Usertable.findByUsername").setParameter("username", userName).getSingleResult();

        // generate hash
        MessageDigest msgDigest = MessageDigest.getInstance("SHA-1");
        String inputText = new String(myUser.getSalt() + this.password);
        for (int i = 0; i < ITERATIONS; i++) {
            msgDigest.update(inputText.getBytes());
            byte rawByte[] = msgDigest.digest();
            inputText = (new BASE64Encoder()).encode(rawByte);
        }

        if (!inputText.equals(myUser.getPassword())) {
            String message = "Username or password incorrect";
            throw new ValidatorException(new FacesMessage(message));
        } else {
            // password is valid, store user into session and mark logged in.
            this.myUser = myUser;
        }

    } catch (NoSuchAlgorithmException ex) {
        Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NoResultException ex) {
        String message = "Username or password incorrect";
        throw new ValidatorException(new FacesMessage(message));
    }

}
