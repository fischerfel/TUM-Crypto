public static void main(String[] args) {
    String message1 = "abc";
    String message2 = "abc";

    int x=0;
    if (message1.equals(message2)) {
        String temp1 = message1 + x;
        String temp2 = x + message2;            
        String result1 = sha1Hashing(temp1);
        String result2 = sha1Hashing(temp2);
        while (!result1.equals(result2)){               
            temp1 = message1 + x;
            temp2 = x + message2;               
            result1 = sha1Hashing(temp1);
            result2 = sha1Hashing(temp2);
            System.out.println("First message = " + temp1 + " Second message = " + temp2 + "\n");
            System.out.println("First hash = " + result1);
            System.out.println("Second hash = " + result2 + "\n");
            x++;        
            if(result1.equals(result2)){
                System.out.println("FOUND!!");
                System.exit(0);
            }
        }
    }
}
public static String sha1Hashing (String message) {
    String sha1 = "";
    StringBuffer sb = new StringBuffer();
    try {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(message.getBytes());

        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
    } catch(NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    //return first 8 char
    sha1 = sb.toString().substring(0,8);
    return sha1;
}
