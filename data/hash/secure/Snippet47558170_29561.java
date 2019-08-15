public class Main {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Path x = Paths.get("D:\\work\\some.zip");
        System.out.println(getChecksum(x));
    }

    public static String getChecksum(Path path) throws IOException, NoSuchAlgorithmException {
        byte[] bytes = Files.readAllBytes(path);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] thedigest = md.digest(bytes);
        return Base64.getEncoder().encodeToString(thedigest);
    }

}
