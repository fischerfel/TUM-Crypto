public void justATestFunction()
        throws Exception
{
    final char[] password = "myP4ssW0rd42".toCharArray();
    MessageDigest md = MessageDigest.getInstance("SHA-256");

    // according to the Utility doc, if the Charset parameter is null or empty,
    // it will call the Charset.defaultCharset() function to define the charset to use
    byte[] hashedPassword= Utility.convertCharArrayToByteArray(password, null);
    hashedPassword = md.digest(hashedPassword);
    Utility.convertByteArrayToCharArray(hashedPassword, null); // throw a MalformatedInputException
}
