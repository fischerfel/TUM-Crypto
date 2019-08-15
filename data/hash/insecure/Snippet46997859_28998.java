public static Map<String, List<String>> hashing(List<File> list) throws Exception {
    Map<String, List<String>> map = new HashMap<>();
    for (File f : list) {
        String path = f.getAbsolutePath();
        FileInputStream in = new FileInputStream(path);
        byte[] dataBytes = new byte[1024];

        MessageDigest md = MessageDigest.getInstance("MD5");
        int n = 0;
        while ((n = in.read(dataBytes)) != -1) {
            md.update(dataBytes, 0, n);
        }
        byte[] mdBytes = md.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mdBytes.length; i++) {
            sb.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        String hash = sb.toString();
        if (!map.containsKey(hash)) {
            map.put(hash, new ArrayList<>());
        }
        map.get(hash).add(path);
    }
    return map;
}
