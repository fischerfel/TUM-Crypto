@Controller
public class FileUploadController {

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) throws NoSuchAlgorithmException, IOException {

        byte[] fileBytes = file.getBytes();
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha256.digest(fileBytes);
        String hashString = new BigInteger(1, digest).toString(16);
        System.out.println("File hash: " + hashString);

        return "redirect:/test.html";
    }
