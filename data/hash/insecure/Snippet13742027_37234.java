    public static void main(String[] args) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] bytes = new byte[] { 1, 2, 10 };
        byte[] buffer2 = sha1.digest(bytes);
        for(byte b : buffer2){
            System.out.println(b);
        }
    }

    static void Main(string[] args)
    {
        var bytes = new byte[] { 1, 2, 10 };
        var buffer = new SHA1CryptoServiceProvider().ComputeHash(bytes);
        foreach (var b in buffer)
        {
            Console.WriteLine((sbyte)b); //attention to cast           
        }
        Console.Read();
    }
