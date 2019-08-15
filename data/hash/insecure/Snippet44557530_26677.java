   package com.example;

   import com.example.*;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.validation.BindingResult;
   import org.springframework.validation.Errors;
   import org.springframework.web.bind.annotation.*;
   import org.springframework.web.context.request.WebRequest;

   import javax.validation.Valid;
   import java.io.UnsupportedEncodingException;
   import java.math.BigInteger;
   import java.security.MessageDigest;
   import java.security.NoSuchAlgorithmException;
   import java.util.Date;

   import java.util.UUID;

   import static jdk.nashorn.internal.objects.NativeString.substr;

    @Controller

    public class RegisterController {

    @Autowired
    private Default_profilesRepository profilesRepository;

@Autowired
private Default_usersRepository usersRepository;

@RequestMapping("/login")
public String Login(Model model)
{
    model.addAttribute("user", new Default_users());

    return "login";
}

@RequestMapping("/loginProcess")
public String loginProcess(@ModelAttribute(value="user") @Valid Default_users user, BindingResult bindingResultUser)
{
    if(bindingResultUser.hasErrors())
    {
        return "login";
    }

    Default_users user2 = usersRepository.findByEmail(user.getEmail());
    if (user2 != null) {


        String passwordToHash = user.getPassword();
        String saltDB = user2.getSalt();
        String Password = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(passwordToHash.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String uuid = UUID.randomUUID().toString();
        MessageDigest crypt = null;
        try {
            crypt = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        crypt.reset();
        try {
            String tets = passwordToHash + saltDB;
            crypt.update(tets.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Password = new BigInteger(1, crypt.digest()).toString(16);
        System.out.println(user2.getPassword());
        System.out.println(Password);
        System.out.println(saltDB);

        if (Password.equals(user2.getPassword())) {


            return "/home";
        } else {
            return "/login";
        }
    }
    return "/home";
}

@GetMapping("/register")
public String Register(Model model)

{
    model.addAttribute("profile", new Default_profiles());
    model.addAttribute("user", new Default_users());

    return "register";
}

@RequestMapping("/save")
public String Process(@ModelAttribute(value="user") @Valid Default_users user, BindingResult bindingResultUser, WebRequest request, Errors errors, @ModelAttribute(value="profile") @Valid Default_profiles profile, BindingResult bindingResultProfile)

{

    Date date = new Date();
    int unixTime = (int) date.getTime() / 1000;

    Default_users userExists = usersRepository.findByEmail(user.getEmail());
    System.out.println(userExists);
    if (userExists != null) {
        bindingResultUser
                .rejectValue("email", "error.user",
                        "There is already a user registered with the email provided");
    }
    if(bindingResultUser.hasErrors() || bindingResultProfile.hasErrors())
    {
        return "register";
    }

    String passwordToHash = user.getPassword();
    String salt = null;
    String Password = null;
    try {
        // Create MessageDigest instance for MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        //Add password bytes to digest
        md.update(passwordToHash.getBytes());
        //Get the hash's bytes
        byte[] bytes = md.digest();
        //This bytes[] has bytes in decimal format;
        //Convert it to hexadecimal format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        //Get complete hashed password in hex format
        salt = sb.toString();
        salt = substr(salt, 0, 6);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    String uuid = UUID.randomUUID().toString();
    MessageDigest crypt = null;
    try {
        crypt = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    crypt.reset();
    try {
        String tets = passwordToHash + salt;
        crypt.update(tets.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    Password = new BigInteger(1, crypt.digest()).toString(16);
    user.setPassword(Password);
    user.setSalt(salt);
    user.setGroup_id(2);
    user.setIp_address("");
    user.setActive(1);
    user.setActivation_code("");
    user.setCreated_on(unixTime);
    user.setLast_login(unixTime);
    user.setForgotten_password_code("");
    user.setRemember_code("hgshd");

    profile.setCreated(date);
    profile.setUpdated(date);
    profile.setCreated_by(unixTime);
    profile.setOrdering_count(0);
    profile.setDisplay_name(user.getEmail());
    profile.setDob(0);
    profile.setGender("");
    profile.setPhone("");
    profile.setAddress_line1("");
    profile.setAddress_line2("");
    profile.setAddress_line3("");
    profile.setPostcode("");
    profile.setUpdated_on(0);
    profile.setCountry("MY");
    profile.setUser_id(user);

    profilesRepository.save(profile);

    return "/result";

      }

     `` }
