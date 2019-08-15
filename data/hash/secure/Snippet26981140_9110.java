private String scrambleName(String name, String surname)
{
MessageDigest messageDigest;
String cryptedName;

StringBuilder builder = new StringBuilder();
builder.append(name);
builder.append(" ");
builder.append(surname);

String fullName = builder.toString();

try
{
    messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(fullName.getBytes());
    cryptedName = new String(messageDigest.digest());
    return cryptedName;
} catch (NoSuchAlgorithmException ex)
{
    Logger.getLogger(Scrambler.class.getName()).log(Level.SEVERE, null, ex);
}
return null;
}
