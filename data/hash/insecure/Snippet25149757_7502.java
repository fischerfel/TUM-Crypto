package com.mkyong.web.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    User user = new User();
    user.name = authentication.getName();
    user.password = authentication.getCredentials().toString();
    try {
      user.id = Instance.users.getUserByEmail(user.name).getUserID();
    } catch (Exception e) {
      Instance.debug("CustomAuthenticationProvider authenticate","Error getting user" + e);
    }

    // use the credentials to try to authenticate against the third party system
    if (passVerify(user)) {
      List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
      try {
        UserRoles roles = Instance.users.getUser(user.id).roles;
        userRolesToDatabaseRoles(roles, grantedAuths);
      } catch (Exception e) {
        Instance.debug("CustomAuthenticationProvider authenticate","Error getting user" + e);
      }
      return new UsernamePasswordAuthenticationToken(user.name, user.password, grantedAuths);
    } else {
      Instance.debug("CustomAuthenticationProvider authenticate","Unable to authenticate");
      return null;
    }
  }

  private void userRolesToDatabaseRoles(UserRoles roles, List<GrantedAuthority> grantedAuths) {
    if(roles.admin){
      grantedAuths.add(new SimpleGrantedAuthority("_admin"));
    }
    if(roles.trader){
      grantedAuths.add(new SimpleGrantedAuthority("_trader"));
    }
    if(roles.analyst){
      grantedAuths.add(new SimpleGrantedAuthority("_users"));
    }


  }

  private boolean passVerify(User user) {
    StringBuffer MD5 = getMD5(user);    
    try {
      //User still has an MD5 password, so change them over to bcrypt
      if(MD5.toString().equals(Instance.users.getPasswordHash(user.name))){
        String hashedPassword = getBcrypt(user).toString();
        instance.users.changePassword(user.id, hashedPassword);
        return true;
      }
    } catch (Exception e) {
      instance.debug("CustomAuthenticationProvider passVerify","Error getting userpassword" + e);
    }

    StringBuffer bcrypt = getBcrypt(user);

    if(bcrypt.toString().equals(user.password)){
      return true;
    }



    return false;
  }

  public StringBuffer getBcrypt(User user) {
    //This sets how many rounds bcrypt will run. The high the number the longer it takes which will slow down user login, however it also slows
    //down a would be attacker. This is a key advantage of bcrypt over other algorithms. *IMPORTANT* changing the strength will result in needing to 
    //rehash all passwords. This is very doable but requires more work. 
    //See http://crypto.stackexchange.com/questions/3003/do-i-have-to-recompute-all-hashes-if-i-change-the-work-factor-in-bcrypt
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    StringBuffer hashedPassword = new StringBuffer(); 
    hashedPassword.append(passwordEncoder.encode(user.password));

    return hashedPassword;
  }

  public StringBuffer getMD5(User user) {
    StringBuffer sb = null;
    MessageDigest md;

    String original = "a";
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(original.getBytes());
      byte[] digest = md.digest();
      sb = new StringBuffer();
      for (byte b : digest) {
        sb.append(String.format("%02x", b & 0xff));
      }

    } catch (NoSuchAlgorithmException e) {
     instance.debug("CustomAuthenticationProvider hashMD5","Error getting MD5 instance" + e);
    }

    return sb;
  }


  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }

  public class User{
    public long id;
    protected String name, password;
  }



}
