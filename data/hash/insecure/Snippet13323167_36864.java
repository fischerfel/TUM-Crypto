PreparedStatement pst = conn.prepareStatement("SELECT id, password FROM userDetails");
ResultSet rs = pst.executeQuery();
String randomPassword = "";
StringBuffer sb;
boolean myPassCheck = true;
while (rs.next()) {
    myPassCheck = true;
    while (myPassCheck) {
        // this will generate random password
        randomPassword = generateRandomPassword();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(randomPassword.getBytes());
        sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
        }
        if (sb.toString().equals(rs.getString(2))) {
            // this is password
            myPassCheck = false;
            System.out.print("id=" + rs.getString(1) + ", password=" + sb.toString());
        }
    }
}
