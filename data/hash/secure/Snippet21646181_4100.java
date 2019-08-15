@RequestMapping(method = RequestMethod.POST)
public String processLogin(Person user, BindingResult result, 
                           @RequestParam("userName") String username, 
                           @RequestParam("password") String password) {
    try {
        password = Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest());
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    ValidateUser(username, password);

    String destination = "";
    if (success == true) {
        destination = "redirect:/person.html";
    }
    else {
        destination = "redirect:/index.html";
    }
    return destination;
}

public boolean ValidateUser(String username, String password) {
    // Decrypt password here.
    List<Person> users = service.getAllPersons();

    for (Person allUsers : users) {
        if (allUsers.getUserName().equals(username) && 
            allUsers.getPassword().equals(password)) {
            success = true;
        }
    }
    return success;
}
