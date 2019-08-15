public TestFileIO() {
    try {
        writeUser(new File("User.inf"), "Test", "Password".toCharArray());
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

public void writeUser(File file, String name, char[] password) throws IOException, NoSuchAlgorithmException {

    BufferedWriter bw = null;

    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] bytes = new byte[password.length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) password[i];
        }
        md.update(bytes);
        byte[] mdbytes = md.digest();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
            sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        bw = new BufferedWriter(new FileWriter(file));
        bw.write(name);
        bw.newLine();
        bw.write(sb.toString());
        bw.flush();
    } finally {
        try {
            bw.close();
        } catch (Exception e) {
        }
    }
}
