import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zusn.domain.User;
import com.zusn.repository.UserRepository;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public User createUser(@RequestBody User newUesr) throws NoSuchAlgorithmException{
        User user = userRepository.findByUidAndDevice(newUesr.getUid(), newUesr.getDevice());
        if(user == null){
            MessageDigest md = MessageDigest.getInstance("SHA");
            Long now = System.nanoTime();
            md.update(now.byteValue());
            String random = RandomStringUtils.randomAlphabetic(32);
            md.update(random.getBytes());
            newUesr.setConsumerKey(String.valueOf(Hex.encode(md.digest())));
            return userRepository.save(newUesr);
        }else{
            return user;
        }
    }
}
