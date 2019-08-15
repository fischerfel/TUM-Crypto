import books.pointejb.User;
import books.pointejb.UserViewBean;

/**
 * Servlet implementation class ValidateLoginServlet
 */
@WebServlet(description = "Validate Register Servlet", urlPatterns = {
        "/Register", "/Register.do" })
public class Register extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final int MAX_LEN = 30;
    private static final int MIN_LEN = 6;

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        // get request parameters for username and password
        String user = request.getParameter("username");
        String firstName = request.getParameter("first_name");
        String lastName = request.getParameter("last_name");
        String email = request.getParameter("email");
        String pwd = request.getParameter("password");
        String pwd2 = request.getParameter("password2");
        boolean ok = true;
        // Validate parameters and forward them to the ejb
        Map<String, String> messages = new HashMap<String, String>();

        if (user != null) {
            // Validate user name
            if (user.trim().isEmpty()) {
                messages.put("userName", "Please enter username");
                ok = false;
            } else if (!user.matches("^[a-zA-Z0-9]$")) {
                messages.put("userName",
                        "Invalid username. The username should contain only aplhanumerical values");
                ok = false;
            } else if (user.length() > MAX_LEN) {
                messages.put("userName",
                        "Invalid username. Username should not be more than "
                                + MAX_LEN + " characters");
                ok = false;
            }
        }
        if (firstName != null) {
            // Validate first name
            if (firstName.trim().isEmpty()) {
                messages.put("firstName", "Please enter your first name");
                ok = false;
            } else if (!firstName.matches("^[a-zA-Z]$")) {
                messages.put("firstName",
                        "Invalid name. Use alpha values only.");
                ok = false;
            } else if (firstName.length() > MAX_LEN) {
                messages.put("firstName",
                        "Invalid name. Name should be at most " + MAX_LEN
                                + " characters long");
                ok = false;
            }
        }
        if (lastName != null) {
            // Validate last name
            if (lastName.trim().isEmpty()) {
                messages.put("lastName", "Please enter your last name");
                ok = false;
            } else if (!lastName.matches("^[a-zA-Z]$")) {
                messages.put("lastName", "Invalid name. Use alpha values only.");
                ok = false;
            } else if (lastName.length() > MAX_LEN) {
                messages.put("lastName",
                        "Invalid name. Name should be at most " + MAX_LEN
                                + " characters long");
                ok = false;
            }
        }
        if (email != null) {
            // Email is specified as request parameter, do the business logic
            // here.
            if (email.trim().isEmpty()) {
                messages.put("email", "Please enter email");
                ok = false;
            } else if (!email
                    .matches("([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)")) {
                messages.put("email", "Invalid email, please try again.");
                ok = false;
            }
        }
        if (pwd != null) {
            // Validate password
            if (pwd.trim().isEmpty()) {
                messages.put("password", "Please enter password");
                ok = false;
            } else if (pwd.length() < MIN_LEN && pwd.length() > MAX_LEN) {
                messages.put("password",
                        "Invalid password. It must have at least " + MIN_LEN
                                + " characters, but not more than " + MAX_LEN);
                ok = false;
            }
            if (pwd2 != null) {
                // Validate password
                if (pwd2.trim().isEmpty()) {
                    messages.put("password2", "Please enter password");
                    ok = false;
                } else if (pwd2.length() < MIN_LEN && pwd2.length() > MAX_LEN) {
                    messages.put("password2",
                            "Invalid password. It must have at least "
                                    + MIN_LEN
                                    + " characters, but not more than "
                                    + MAX_LEN);
                    ok = false;
                } else if (!pwd2.equals(pwd)) {
                    messages.put("password3", "Passwords do not match");
                    ok = false;
                }
            }
            if (!ok) {
                // Put messages in request scope so that it's accessible in EL
                // by
                // ${messages}.
                request.setAttribute("messages", messages);
                // Forward request to JSP for display.
                request.getRequestDispatcher("/register.jsp").forward(request,
                        response);
            } else {
                // Send data to ejb
                UserViewBean userBean = Lookup.doLookupUser();
                User registerUser = new User();
                // Set the username
                registerUser.setUsername(user);
                // Set the first name
                registerUser.setFirstName(firstName);
                // Set last name
                registerUser.setLastName(lastName);
                // Set email
                registerUser.setEmail(email);
                // Set the password(the bean computes the hash as well)
                registerUser.setPassword(pwd);
                // Now we try to register the new user. Additionally we log him in.
                if (userBean.register(registerUser)) {
                    // Add a session variable based on the local date, remote
                    // address,
                    // remote port
                    // and username if the provided credentials(user/pass) are
                    // valid
                    String sessionData = now();

                    sessionData = sessionData + " " + request.getRemoteAddr();
                    sessionData = sessionData + " " + request.getRemotePort();
                    sessionData = sessionData + " " + user;

                    String generatedSession = null;
                    try {
                        // Create MessageDigest instance for MD5
                        MessageDigest md = MessageDigest.getInstance("SHA-512");
                        // Add password bytes to digest
                        md.update(sessionData.getBytes());
                        // Get the hash's bytes
                        byte[] bytes = md.digest();
                        // This bytes[] has bytes in decimal format;
                        // Convert it to hexadecimal format
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < bytes.length; i++) {
                            sb.append(Integer.toString(
                                    (bytes[i] & 0xff) + 0x100, 16).substring(1));
                        }
                        // Get complete hashed password in hex format
                        generatedSession = sb.toString();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    // Get the request session and set new variables
                    request.getSession().setAttribute(cookieid,
                            generatedSession);
                    // Also store cookie in a safe place(the cookie jar)
                    CookieJar.userCookie = generatedSession;
                    // Send the request back with the session set
                    request.getRequestDispatcher("/register.jsp").forward(request,
                            response);
                } else {
                    // Code to be executed if registration goes wrong
                }
            }
        }
    }

    private static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
    private static final String cookieid = "CookieId";
        ...
}
