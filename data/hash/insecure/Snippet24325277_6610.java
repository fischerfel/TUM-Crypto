public class LoginServlet extends HttpServlet {

private static final long serialVersionUID = -776218596462464850L;
private static final Log LOG = LogFactory.getLog(LoginServlet.class);
private static final int HEX_FF = 0xFF;

/** 
 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
 * @param response der HttpResponse
 * @param request der HttpRequest
 * @throws ServletException ex
 * @throws IOException ex
 */
@Override
@Transactional
protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {


    if (!"Registrieren".equals(request.getParameter("register"))) {

        final SessionFactory factory = HibernateUtil.getSessionFactory();
        final Session session = factory.getCurrentSession();

        // Generiert einen HashWert aus dem eingegebenen Passwort
        try {
            final byte[] pwBytes = request.getParameter("password").getBytes();
            final MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(pwBytes);
            final byte[] messageDigest = algorithm.digest();

            final StringBuffer hexString = new StringBuffer();

            for (byte digest : messageDigest) {
                hexString.append(Integer.toHexString(HEX_FF & digest));
            }

            final LoginDAO dao = new LoginDAO();
            final List<UserVO> list = dao.checkLogin(request, session);

            final UserVO userVO = list.get(0);

            if (userVO.getPassword().equals(hexString.toString())) {

                request.getSession().setAttribute("userid", userVO.getId());
                final RequestDispatcher dispatcher = request.getRequestDispatcher("main.jsp");
                dispatcher.forward(request, response);

            } else {

                final RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
                dispatcher.forward(request, response);
            }

        } catch (NoSuchAlgorithmException nsax) {
            LOG.debug("Keine MessageDigestSpi fuer den entsprechenden Alogrithmus gefunden");

        } catch (NullPointerException npx) {
            final RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);

        } catch (IndexOutOfBoundsException ioobx) {
            final RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
    } else {
        final RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }
}

/** 
 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
 * @param response der HttpResponse
 * @param request der HttpRequest
 * @throws ServletException ex
 * @throws IOException ex
 */
@Override
protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
    doPost(request, response);
}

}
