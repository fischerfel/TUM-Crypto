private String userName;
private String password;
    private String answerOne;
    private String answerTwo;
    private String answerThree;
    private static File file = new File("C:\\Users.txt");

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public User(String un, String pw, String a1, String a2, String a3) {
        setUserName(un);
        setPassword(pw);
        setAnswerOne(a1);
        setAnswerTwo(a2);
        setAnswerThree(a3);

    }

    public void addUser(User user) throws IOException {
        FileWriter fw = new FileWriter(getFile());
        try (BufferedWriter bfw = new BufferedWriter(fw)) {
            bfw.write(user.userName);
            bfw.write(user.password);
            bfw.write(user.answerOne);
            bfw.write(user.answerTwo);
            bfw.write(user.answerThree);
            bfw.newLine();
        }

    }

    public static boolean verifyUserExists(String userName) throws FileNotFoundException {
        Scanner scannedFile = new Scanner(getFile());
        while (scannedFile.hasNext()) {
            String search = scannedFile.next();
            return search.equals(userName);
        }
        return false;
    }

    public static Boolean verifyPassword(String userName, String password) throws FileNotFoundException {
        Scanner scannedFile = new Scanner(getFile());
        while (scannedFile.hasNext()) {
            String search = scannedFile.next();
            if (search.equals(userName)) {
                return scannedFile.nextLine().equals(getMD5(password));
            }
        }
        return false;
    }

    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static File getFile() {
        return file;
    }
