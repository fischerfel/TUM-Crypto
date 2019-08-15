@Override
public boolean login(User user) {
    // Check if we have a valid user/pass pair
    Query query = (Query) entityManager.createQuery("SELECT u FROM User u WHERE u.username=:userName AND u.password=:password");
    ((javax.persistence.Query) query).setParameter("userName", user.getUsername());
    // We need to hash the password first before comparing it(as we only store the SHA-512 hash)
    String tohash = user.getPassword();
    String hash = null;
    try {
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        // Add password bytes to digest
        md.update(tohash.getBytes());
        // Get the hash's bytes
        byte[] bytes = md.digest();
        // This bytes[] has bytes in decimal format;
        // Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,
                    16).substring(1));
        }
        // Get complete hashed password in hex format
        hash = sb.toString();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    ((javax.persistence.Query) query).setParameter("password", hash);
    List<User> users = castList(User.class, ((javax.persistence.Query) query).getResultList());
    if(users != null && !users.isEmpty()) {
        // return "Welcome " + user.getUsername() + "!";
        return true;
    } else {
        // return "Username or password are not valid";
        return false;
    }
}
