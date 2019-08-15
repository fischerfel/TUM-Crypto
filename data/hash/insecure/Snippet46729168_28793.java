public class SpringPasswordEncoder implements PasswordEncoder {

@Override
public String encode(CharSequence rawPassword) {
    try {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(rawPassword.toString().getBytes());
        String encodedPassword = HexUtils.bufferToHex(md.digest());
        LoggerFactory.getLogger(getClass()).info("Generated encoded '{}' for '{}'.", encodedPassword, rawPassword);    
        return encodedPassword;

    } catch (Exception e) {
        throw new RuntimeException(e);
    }
}

@Override
public boolean matches(CharSequence rawPassword, String encodedPassword) {
    LoggerFactory.getLogger(getClass()).info("Validating encoded '{}' against '{}'.", encodedPassword, rawPassword);
    if (rawPassword == null || encodedPassword == null){
        return false;
    }
    return encodedPassword.equals(encode(rawPassword));
}
