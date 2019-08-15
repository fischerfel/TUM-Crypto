try {
    setPassword(txtPassword.getText());
    setUsername(txtUserName.getText());

    MessageDigest md;

    md = MessageDigest.getInstance("MD5");
    byte messgeDigest[] = md.digest(getPassword().getBytes());
    BigInteger number = new BigInteger(1, messgeDigest);
    String hashtext = number.toString(16);

    String qry = "SELECT * FROM users WHERE username=? AND password=?";
    pst = mscon.conn().prepareStatement(qry);
    pst.setString(1, getUsername());
    pst.setString(2, hashtext);
    rs = pst.executeQuery();

    if (rs.next()) {
        JOptionPane.showMessageDialog(null, "Login Successful");
        System.out.println("Login");
    }
    else{
        System.out.println("Tri again");
    }
} catch (NoSuchAlgorithmException ex) {

} catch (SQLException ex) {
    Logger.getLogger(MS_Login.class.getName()).log(Level.SEVERE, null, ex);
}
