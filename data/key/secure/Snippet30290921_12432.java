@RequestMapping(value="/{userId}/downloadfile/{sharingId}", method = RequestMethod.GET, produces="application/json")
    public ResponseEntity<?> downloadAsymmetricFile(@PathVariable("sharingId") int sharingId, @PathVariable("userId") int userId, HttpServletResponse response) throws IOException, SQLException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {     

    AsymetricSharing file= resourseService.getFile(sharingId);

    if(file!=null){ 
        Profile receiverProfile=  userService.getUserProfile(userId);

        byte [] receiverPrivateKey=receiverProfile.getPrivateKey();         

        PrivateKey testPvtKey=Converter.encodedByteToKey(receiverPrivateKey);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, testPvtKey);

        byte[] symetricKeyBytes = cipher.doFinal(file.getSymetricKey());    

        SecretKey symetricKey = new SecretKeySpec(symetricKeyBytes, "AES");

        Cipher cipher1 = Cipher.getInstance("AES");
        cipher1.init(Cipher.DECRYPT_MODE, symetricKey);
        byte[] plainText = cipher.doFinal(file.getResourceFile());

        response.setContentLength(plainText.length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + file.getResourceName() +"\"");
        FileCopyUtils.copy(plainText, response.getOutputStream());
        return new ResponseEntity<>(file, HttpStatus.OK);
    }
    else
    {
        //if no entity present against id, return not found and bad request Http status.
        return new ResponseEntity<>("Not found", HttpStatus.BAD_REQUEST);
    }
}
