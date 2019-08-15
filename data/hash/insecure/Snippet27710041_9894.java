    String hash = "<hashOfMyPassword";
    String pass = "<myPassword>";
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
    System.out.println(passHash.toString().equals(hash));
