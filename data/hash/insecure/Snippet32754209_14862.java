public class WebLoginModule extends UsernamePasswordLoginModule{

    private SecurityPrincipal sp = null;
     Subject sub ;

    public boolean logout() throws LoginException
    {
        sub.getPrincipals().remove(sp);
        System.out.println("Logging out");
        return super.logout();
    }


    @Override
    protected boolean validatePassword(String username, String password)
    {
        Principal p = this.getIdentity();
        sub = new Subject();

        System.out.println("password(as param) : "+password);
        if(password==null){
            System.out.println("password empty");
            password = username;
            username = p.getName();
        }else{
            //Do nothing
        }

          if(p instanceof SecurityPrincipal) {                    

              try {
                  sp = (SecurityPrincipal)p;
                  sp.setUsername(p.getName());
                  sp.setPassword(password);             
                  sp.setSubj(sub);  
                  sp.setColRole(null);; // TODO: fix this.
                  System.out.println("username: "+username);
                  return isValidUser(username, password);
              }
              catch(Exception e) {
                  e.printStackTrace();

              }
          }

        return false;
    }

    @Override
    protected String getUsersPassword() throws LoginException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isValidUser(String username, String password)  {
        boolean result=false;
        try{
            final Hashtable jndiProperties = new Hashtable();
            jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
            final Context context = new InitialContext(jndiProperties);
            System.out.println("getting value");
            EmployeeEJBIf lif = (EmployeeEJBIf) context.lookup("java:global/!EmployeeEJBIf");
            System.out.println("loading data");
            password=hashPassword(password);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return result;
    }


    @Override
    protected Group[] getRoleSets() throws LoginException {
        try {           
            Group callerPrincipal = new SimpleGroup("CallerPrincipal");
            Group roles = new SimpleGroup("Roles");
            Group[] groups = {roles,callerPrincipal};           
            roles.addMember(new SecurityPrincipal("SecurityAdmin"));
            callerPrincipal.addMember(sp);
            return groups;
        }
        catch(Exception e) {
            e.printStackTrace();
            throw new LoginException(e.getMessage());
        }

    }
     private String hashPassword(String password) {
            String hashword = null;
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");            
                md5.update(password.getBytes());
                BigInteger hash = new BigInteger(1, md5.digest());
                hashword = hash.toString(16);
            }catch (Exception e) {
                e.printStackTrace();
             }          
            return hashword;
    }
