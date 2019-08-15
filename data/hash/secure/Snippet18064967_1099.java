public class Executable
{
    private int index = 0;
    private int testsize = 1111111;
    private byte[][] plain = new byte[testsize][];
    private byte[][] hashed = new byte[testsize][];
    private SecureRandom securerandom;
    private MessageDigest messagedigest;

    public Executable()
    {
        this.securerandom = new SecureRandom();
        this.messagedigest = MessageDigest.getInstance("SHA-256");
        for (int i = 0; i <= testsize - 1; i++)
        {
            this.plain[i] = new byte[8];
            this.securerandom.nextBytes(this.plain[i]);
        }
    }

    public void execute()
    {
        messagedigest.update(this.plain[index]);
        this.hashed[index] = messagedigest.digest();
        index++;
    }
}
