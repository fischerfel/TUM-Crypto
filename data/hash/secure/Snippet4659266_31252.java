public static void main(String[] args) {
    try {
        String s1 = "1";
        File f1 = new File("f1");
        write (s1, f1);
        System.out.println(read(f1).equals(s1));

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        String s2 = foo(new File("1.jpg"), md);
        File f2 = new File("f2");
        write (s2, f2);
        System.out.println(read(f2).equals(s2));
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}

// Hash <i>f</i> by <i>md</i>
static String foo (File f, MessageDigest md) throws IOException {
    FileInputStream fis = new FileInputStream(f);
    DigestInputStream dis = new DigestInputStream(fis, md);
    byte[] b = new byte[1024];
    while (dis.read(b, 0, 1024) != -1) {
    }
    md = dis.getMessageDigest();
    String s = new String(md.digest());
    dis.close();
    fis.close();
    return s;
}

static void write (String s, File f) throws IOException {
    FileWriter fw = new FileWriter(f);
    BufferedWriter bw = new BufferedWriter(fw);
    bw.write(s);
    bw.newLine();
    bw.close();
    fw.close();
}

static String read (File f) throws IOException {
    FileReader fr = new FileReader(f);
    BufferedReader bf = new BufferedReader(fr);
    String s;
    s = bf.readLine();
    bf.close();
    fr.close();
    return s;
}
