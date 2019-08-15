MessageDigest md = MessageDigest.getInstance("SHA1");
InputStream in = new FileInputStream("hereyourinputfilename");
byte[] buf = new byte[8192];
for (;;) {
    int len = in.read(buf);
    if (len < 0)
        break;
    md.update(buf, 0, len);
}
in.close();
byte[] hash = md.digest();
