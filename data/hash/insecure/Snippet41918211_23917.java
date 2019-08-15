static Hashtable<byte[], Path> htable = new Hashtable<byte[], Path>();

public static void main(String[] args) throws NoSuchAlgorithmException, IOException {

    String file = "Z:\\file.txt";
    String file2 = "Z:\\file2.txt";

    // First file
    Path filePath = Paths.get(file);
    doHashStore(filePath);

    // Second file
    filePath = Paths.get(file2);
    doHashStore(filePath);

    // First again, should notify already exists
    filePath = Paths.get(file);
    doHashStore(filePath);
}

// Add to table
private static void doHashStore(Path p) throws NoSuchAlgorithmException, IOException{
    byte[] md5 = doMD5(p);
    if(htable.containsKey(md5)) {
        System.out.println(p.toString()+" already in table! "+toHexadecimal(md5));
    } else {
        htable.put(md5, p);
        System.out.println(p.toString()+" added to table "+toHexadecimal(md5));
    }
}

// Perform the md5 hash
private static byte[] doMD5(Path filePath) throws IOException, NoSuchAlgorithmException{
    byte[] b = Files.readAllBytes(filePath);
    byte[] c = MessageDigest.getInstance("MD5").digest(b);
    return c;
}

// Convert the byte[] from digest into a human readable hash string
private static String toHexadecimal(byte[] digest){
    String hash = "";
    for(byte aux : digest) {
        int b = aux & 0xff;
        if (Integer.toHexString(b).length() == 1)
            hash += "0";
        hash += Integer.toHexString(b);
    }
    return hash;
}
