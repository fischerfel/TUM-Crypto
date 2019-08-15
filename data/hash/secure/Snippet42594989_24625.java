@Component
public class DBAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String name = authentication.getName();
    String password = authentication.getCredentials().toString();

   // your code to compare to your DB
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  /**
   * @param original <i>mandatory</i> - input to be hashed with SHA256 and HEX encoding
   * @return the hashed input
   */
  private String sha256(String original) {
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new AuthException("The processing of your password failed. Contact support.");
    }

    if (false == Strings.isNullOrEmpty(original)) {
      md.update(original.getBytes());
    }

    byte[] digest = md.digest();
    return new String(Hex.encodeHexString(digest));
  }

  private class AuthException extends AuthenticationException {
    public AuthException(final String msg) {
      super(msg);
    }
  }
}
