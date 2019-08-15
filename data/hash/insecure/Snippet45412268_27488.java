if (errors.isEmpty()) {
        byte[] salt = new byte[12];
        new SecureRandom().nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] intermediateHash = md.digest(passwordValue.getBytes());
        md.update(intermediateHash);
        byte[] hash = md.digest(salt);

        UserDAO.createUser(emailValue, hash, salt, companyValue, phoneValue, countryValue, zipcodeValue, addressValue); 
        User user = UserDAO.getUser(emailValue);
        SessionModel model = (SessionModel) request.getSession().getAttribute("sessionModel");
        model.login(user.getId());
        response.sendRedirect("profile.jsp");
        return;
} else {
        for (String errorMsg : errors) {
            request.getSession().setAttribute("errorMessage", errorMsg);
        }
        response.sendRedirect("index.jsp");
}
