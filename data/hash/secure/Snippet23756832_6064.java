public String create(@Valid User user, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
    if (bindingResult.hasErrors()) {
        populateEditForm(uiModel, usuario);
        return "security/users/create";
    }
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(user.getPassword().getBytes("UTF-8"));
    byte[] digest = md.digest();
    usuario.setPassword( new String(digest, "UTF-8"));
    uiModel.asMap().clear();
    user.persist();
    return "redirect:/security/users/" + encodeUrlPathSegment(usuario.getId().toString(), httpServletRequest);
}
