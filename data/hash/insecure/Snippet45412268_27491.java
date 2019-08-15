if (errors.isEmpty()) {
        User user = UserDAO.getUser(emailValue);
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] intermediateHash = md.digest(passwordValue.getBytes());
        md.update(intermediateHash);
        byte[] givenHash = md.digest(user.getSalt());
        boolean match = Arrays.equals(givenHash, user.getHash());
        System.out.println(givenHash.length);
        System.out.println(user.getHash().length);
        System.out.println(match);

        if (match) {        
                SessionModel model = (SessionModel) request.getSession().getAttribute("sessionModel");
                model.login(user.getId());
                response.sendRedirect("profile.jsp");
                return;
        } else {
            errors.add("Fejl i email eller password");
            response.sendRedirect("index.jsp");
            return;
        }
}
