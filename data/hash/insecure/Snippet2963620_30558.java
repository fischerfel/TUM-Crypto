                <%@page import="com.jSurvey.entity.*"    %>
    <%@page import="java.security.MessageDigest" %>
    <%@page import="java.security.NoSuchAlgorithmException" %>
    <%@page import="java.math.BigInteger" %>
    <%@page import="com.jSurvey.controller.*" %>
    <%@page import="sun.misc.BASE64Encoder" %>
    <%try {
                    String user = request.getParameter("Username");
                    String pass = request.getParameter("Password1");
                    String name = request.getParameter("Name");
                    String mail = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String add1 = request.getParameter("address1");
                    String add2 = request.getParameter("address2");
                    String country = request.getParameter("country");
                    Login login = new Login();
                    Account account = new Account();

                    login.setId(user);
                    login.setPassword(pass);
                    if (!(add1.equals(""))) {
                        account.setAddress1(add1);
                    }
                    if (!(add2.equals(""))) {
                        account.setAddress2(add2);
                    }
                    if (!(country.equals(""))) {
                        account.setCountry(country);
                    }
                    account.setId(user);
                    account.setMail_id(mail);
                    if (!(phone.equals(""))) {
                        account.setPhone_no(Long.parseLong(phone));
                    }
                    account.setName(name);
                    java.security.MessageDigest d = null;
                    d = java.security.MessageDigest.getInstance("SHA-1");
                    d.reset();
                    d.update(pass.getBytes("UTF-8"));
                    byte b[] = d.digest();
                    String tmp = (new BASE64Encoder()).encode(b);

                    account.setPassword(tmp);
                    account.setPrivilege(1);
                    LoginJpaController logcon = new LoginJpaController();
                    AccountJpaController acccon = new AccountJpaController();
                    logcon.create(login);
                    acccon.create(account);
                    session.setAttribute("user", user);
                    response.sendRedirect("dashboard.jsp");
                } catch (NumberFormatException ex) {
                    out.println("Invalid data");
                }
    %>
