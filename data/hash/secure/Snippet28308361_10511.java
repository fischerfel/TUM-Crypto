private void ChangeSQLPassword(String userName,char[] pass) {
        Connection conn = null;
        String hashed = "";
        String salt = "";
        String concat = "";
        try {
            String url = "jdbc:mysql://localhost:3306/PM";
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, "Tempuser", "temppass");
            System.out.println("Database connection established");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Cannot connect to database server");
        } finally {
            if (conn != null) {
                try {
                    System.err.println("Connection not null...");
                    salt = getSalt();
                    hashed = sha256(pass,salt);
                    Statement sta3 = conn.createStatement();
                    ResultSet rs = sta3.executeQuery("SELECT * FROM pmusers"); //Select users
                    System.out.println(userName + " " + new String(pass));
                    while (rs.next() == true) { //Loop through results
                        if (rs.getString("username").equals(userName)) {
                            concat = concat.concat("UPDATE pmusers SET Hashed = '" + hashed + "', Salt = '" + salt + "' WHERE Username = '" + userName +"';");
                            sta3.executeUpdate(concat);
                        }
                    }
                } catch (Exception e) { /* ignore close errors */
                    e.printStackTrace();
                    System.err.println("Failed.");
                }
            }
        }
    }

static String sha256(char[] input, String Salt) throws NoSuchAlgorithmException {
    MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
    byte[] result;
    StringBuffer sb = new StringBuffer();
    try {
        mDigest.update(Salt.getBytes("UTF-8"));
        result = mDigest.digest(String.valueOf(input).getBytes("UTF-8"));
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
    } catch (UnsupportedEncodingException e) {
        System.out.println("Encoding failed.");
    }

    String hashed = org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(String.valueOf(sb).getBytes());

    return hashed;
}

private static String getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
    //Always use a SecureRandom generator
    SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
    //Create array for salt
    byte[] salt = new byte[16];
    //Get a random salt
    sr.nextBytes(salt);
    //return salt
    return salt.toString();
}
