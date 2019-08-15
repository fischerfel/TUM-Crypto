try{
            ServletContext context = getServletContext();

            String currentPassword = request.getParameter("currentPassword");
            String newPassword = request.getParameter("newPassword");
            String tryNewPassword = request.getParameter("retypePassword");
            String pswrd = "";

            //For Password Encryption into md5
            String md5 = null;
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(currentPassword.getBytes(), 0, currentPassword.length());
            md5 = new BigInteger(1, digest.digest()).toString(16);

            //Database Connection
            Class.forName(context.getAttribute("dbdriver").toString());
            String db = context.getAttribute("connstr").toString();
            Connection conn = DriverManager.getConnection(db);
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();

            //Check Database Connection
            if (conn != null) {
                JOptionPane.showMessageDialog(null, "Connected!");
            } else {
                JOptionPane.showMessageDialog(null, "Not Connected!");
            }

            String query = "SELECT password FROM accounts WHERE password='"+md5+"'";
            ResultSet rs = stmt.executeQuery(query);


            JOptionPane.showMessageDialog(null, "Entering while loop");
            while(rs.next()){
            if(md5CurrentPasswrd.equals(rs.getString(1))){
                if(newPassword == tryNewPassword){
                    digest.update(newPassword.getBytes(), 0, newPassword.length());
                    md5NewPasswrd = new BigInteger(1, digest.digest()).toString(16);
                    String queryUpdate = "UPDATE accounts SET password='"+md5NewPasswrd+"' /n"
                            + " WHERE password='"+md5CurrentPasswrd+"'";
                    PreparedStatement prepStmt = conn.prepareStatement(queryUpdate);
                    prepStmt.executeUpdate();

                    response.sendRedirect("/Project1/PasswordSaved.jsp");
                }else{
                    response.sendRedirect("/Project1/PasswordNotSaved.jsp");
                }
            }else{
               response.sendRedirect("/Project1/PasswordNotSaved.jsp");
            }
            pswrd = rs.getString(1);
            JOptionPane.showMessageDialog(null, "Running while loop");
        }

            JOptionPane.showMessageDialog(null, "End of while loop");

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ChangePassword.class.getName()).log(Level.SEVERE, null, ex);
        }
