    public String createAccount(String firstName, String middleName, String lastName, String username,
        String password, String confirmPassword, String email, String confirmEmail, String Organization,
        String address, String state, String country, String gRecaptchaResponse) {

    boolean verified = false;
    try {
        verified = VerifyRecaptcha.verify(gRecaptchaResponse);
    } catch (IOException ex) {
        Logger.getLogger(AdminFacade.class.getName()).log(Level.SEVERE, "Captcha palava_" + ex.getMessage(), ex);
    }

    if (verified) {
        Query q = em.createNamedQuery("Admin.findByUsername");
        q.setParameter("username", username);

        if (q.getResultList().size() > 0) {
            return "Username &lt;" + username + "&gt; already exists. Pls try a different username";
        }

        if (!password.equals(confirmPassword)) {
            return "Password Mis-match";
        }

        if (!email.equals(confirmEmail)) {
            return "E-mail Mis-match";
        }

        try {
            Admin admin = new Admin();

            admin.setEmail(email);
            admin.setFirstName(firstName);
            admin.setMiddleName(middleName);
            admin.setLastName(lastName);
            admin.setOrganization(Organization);
            admin.setEmail(email);
            admin.setAddress(address);
            admin.setState(state);
            admin.setCountry(country);
            admin.setExamSessionId(UniqueValueGenerator.generate());
            admin.setResultDisplay(true);

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte byteData[] = md.digest();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            admin.setPassword(sb.toString());

            admin.setUsername(username);

            em.persist(admin);

            //set the password back to the literal password so that email sent will show the literal password
            admin.setPassword(password);

            QueueConnection connection = null;
            Session jmssession = null;
            MessageProducer messageProducer = null;
            try {

                connection = connectionFactory.createQueueConnection();
                jmssession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                messageProducer = jmssession.createProducer(queue);

                ObjectMessage message = jmssession.createObjectMessage();

                message.setObject(admin);
                System.out.println("Sending message attempt: ");
                System.out.println(messageProducer.getDestination().toString());
                System.out.println(messageProducer.getDeliveryMode());
                System.out.println(messageProducer.getPriority());
                messageProducer.send(message);

            } catch (JMSException ex) {
                Logger.getLogger(AdminFacade.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);

                return "Account was successfully created but it looks like we were unable to notify your email. Please check your "
                        + "mail NOW for notification  and if you can't find it, send us a mail at abc@abc.com";
            } finally {
                try {
                    connection.close();
                    jmssession.close();
                    messageProducer.close();
                    System.out.println("All Closed");
                } catch (JMSException ex) {
                    Logger.getLogger(AdminFacade.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }

            return "Success";

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdminFacade.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return "Something went wrong, please make sure all fields are filled correctly";
        }
    } else {
        return "You missed the Recaptcha, please retry";
    }
}
