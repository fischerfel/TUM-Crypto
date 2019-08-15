public final boolean authenticate(User user)

    MessageDigest md = MessageDigest.getInstance(SHA1);

    byte[] saltPassword = (user.getSalt() + user.getPassword()).getBytes();
    byte[] encryptedPassword = md.digest(saltPassword);

    String pass = byteArrayToHexString(encryptedPassword);

    if (pass.equals(user.getDbPassword())) 
    {
        return true;
    }
    else
    {
        return false;
    }
}

private String byteArrayToHexString(byte[] array) {
    String result = "";
    for (int i = 0; i < array.length; i++) {
        result
                += Integer.toString((array[i] & 0xff) + 0x100, 16).substring(1);
    }
    return result;
}
