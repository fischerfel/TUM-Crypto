import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ViewController {

    private String sha256String;

    @RequestMapping("/")
    public String Test(){
        return "index";
    }

    @RequestMapping("/hash")
    public String index(Model model){
        model.addAttribute("hash", sha256String);
        return "hash";
    }


    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws IOException, 
    `enter code here`NoSuchAlgorithmException {

        byte[] fileBytes = file.getBytes();
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha256.digest(fileBytes);
        String sha256String = new BigInteger(1, digest).toString(16);
        this.sha256String = sha256String;

        return "redirect:/hash";
    }

    }
