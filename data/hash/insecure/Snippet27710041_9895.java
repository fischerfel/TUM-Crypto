public void handle(String s, Request req, HttpServletRequest hreq,
            HttpServletResponse hres) throws IOException, ServletException {
        hres.setContentType("text/plain");
        hres.setStatus(HttpServletResponse.SC_OK);
        req.setHandled(true);
        if (!running) {

            //Validate password
            String pass = hreq.getParameter("password");
            hres.getWriter().println(pass);
            byte[] data = pass.getBytes();
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            md.update(data);
            MessageDigest passMD = null;
            try {
                passMD = (MessageDigest) md.clone();
            } catch (CloneNotSupportedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            byte[] passHash = passMD.digest();
            hres.getWriter().println(passHash.toString());
            if (passHash.toString().equals(hash)) {
               //dostuff
            } else {
                hres.getWriter().println("invalid password");
            }
        } 
    }
}
