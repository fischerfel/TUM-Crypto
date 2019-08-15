public void login() throws NoSuchAlgorithmException {
    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();

    EntityManager em = emf.createEntityManager();
    boolean committed = false;
    try {
        FacesMessage msg = null;
        EntityTransaction entr = em.getTransaction();
        entr.begin();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            password = sb.toString();
            Query query = em.createQuery("SELECT COUNT(u) FROM EntityUser u WHERE u.userName = :userName AND u.password = :password")
                    .setParameter("userName", userName).setParameter("password", password);
            long result = (long)query.getSingleResult();
            if (result == 1) {
                request.login(userName, password);
                msg = new FacesMessage();
                msg.setSeverity(FacesMessage.SEVERITY_INFO);
                msg.setSummary("You are logged in");
            }
            entr.commit();
            committed = true;
        } catch (ServletException e) {
            context.addMessage(null, new FacesMessage("wrong username or password"));
        }
        finally {
            if (!committed) entr.rollback();
        }
    } finally {
        em.close();
    }
}
