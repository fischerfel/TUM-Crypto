public class MD5PasswordEncoder implements PasswordEncoder {

     @Override
     public String encode(CharSequence charSequence) {
         String encPass = "";
        try {
             MessageDigest md = MessageDigest.getInstance("MD5");
             byte[] digest = md.digest(charSequence.toString().getBytes());
             byte[] b64 = Base64.encodeBase64(digest);
             encPass = new String(b64);
             encPass = encPass.replaceAll("=", "");
         }catch(Exception ex){
             logger.error("An exception trying to encode a password", ex);
         }
         return encPass;
     }

     @Override
     public boolean matches(CharSequence charSequence, String s) {
         return encode(charSequence).equals(s);
     }
}

public interface UserDetailsService {
    UserDetails loadUserByUsername(String var1) throws UsernameNotFoundException;
}


@Service
public class UserDetailSecurityService implements UserDetailsService{

    //Here your user service implementation
    @Autowired
    UserService userService;

    //yuou need to oeverride this method name
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // you need to create a method in your service to find users by name
        return userService.findByUsername(username);
    }
}
