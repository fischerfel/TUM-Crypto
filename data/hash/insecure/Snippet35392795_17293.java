public class CSRF {
    HttpServletRequest request;
    ServletRequest req;
    String token;
    boolean ok;
    private static final Logger logger = Logger.getLogger(CSRF.class);


    public CSRF(ServletRequest request) {
        this.request = (HttpServletRequest) request;
        this.req = request;
        init();
    }

    public CSRF() {
    }


    public void setRequest(HttpServletRequest request) {
        this.request = (HttpServletRequest) request;
        this.req = request;
        init();
    }

    private void init() {
        if (request.getMethod().equals("GET")) {
            generateToken();
            addCSRFTokenToSession();
            addCSRFTokenToModelAttribute();
            ok = true;
        } else if (request.getMethod().equals("POST")) {
            if (checkPostedCsrfToken()) {
                ok = true;
            }
        }
    }

    private void generateToken() {
        String token;
        java.util.Date date = new java.util.Date();
        UUID uuid = UUID.randomUUID();
        token = uuid.toString() + String.valueOf(new Timestamp(date.getTime()));
        try {
            this.token = sha1(token);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            this.token = token;
        }
    }

    private void addCSRFTokenToSession() {
        request.getSession().setAttribute("csrf", token);
    }

    private void addCSRFTokenToModelAttribute() {
        request.setAttribute("csrf", token);
    }

    private boolean checkPostedCsrfToken() {
        System.out.println("____ CSRF CHECK POST _____");
        if (request.getParameterMap().containsKey("csrf")) {
            String csrf = request.getParameter("csrf");
            if (csrf.equals(request.getSession().getAttribute("csrf"))) {
                return true;
            }
        }else {
            //Check for multipart requests

            MultipartHttpServletRequest multiPartRequest = new DefaultMultipartHttpServletRequest((HttpServletRequest) req);
            if (multiPartRequest.getParameterMap().containsKey("csrf")) {
                String csrf = multiPartRequest.getParameter("csrf");
                if (csrf.equals(request.getSession().getAttribute("csrf"))) {
                    return true;
                }
            }
        }

        log();
        return false;
    }

    private void log() {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if(username==null){
            username = "unknown (not logged in)";
        }
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        String userAgent = request.getHeader("User-Agent");
        String address = request.getRequestURI();
        System.out.println("a CSRF attack detected from IP: " + ipAddress + " in address \"" + address + "\" - Client User Agent : " + userAgent + " Username: " + username);

        logger.error("a CSRF attack detected from IP: " + ipAddress + " in address \"" + address + "\" - Client User Agent : " + userAgent + " Username: " + username);
    }

    public boolean isOk() {
        return ok;
    }

    static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
