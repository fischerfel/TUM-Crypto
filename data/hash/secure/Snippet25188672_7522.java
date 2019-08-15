public class YourPasswordStorage
{
    Properties users = new Properties();

    public YourPasswordStorage (File file)
    {
       user.load(file);
    }

    void insertUser (String user, String password)
    {
        users.put(user,
            Base64.getEncoder().encodeToString(
                MessageDigest.getInstance("sha-512").digest(password)));
    }

    boolean checkUser (String user, String password)
    {
        String hash = Base64.getEncoder().encodeToString(
            MessageDigest.getInstance("sha-512").digest(password));
        return (hash.equals(users.get(user)); // note that users.get(user) could be null
    }
}
