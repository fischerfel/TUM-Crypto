@RequestMapping(value="/login", method=RequestMethod.POST)
    public ModelAndView login(@RequestParam("username") String username, @RequestParam("password") String password) throws NoSuchAlgorithmException
    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();

        Usuario temp = new Usuario(username, convertByteToHex(digest));
        UsuarioHome tempHome = new UsuarioHome();
        List<Usuario> lista = tempHome.findByExample(temp);
        if(lista.size() == 0) {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("usuario_login");
            mav.addObject("message", "N&atilde;o foi possivel efetuar o login");
            return mav;
        }
        else {
            this.sessao = new Sessao();
            ModelAndView mav = new ModelAndView();
            mav.setViewName("usuario_start");
            mav.addObject("usuario", temp);
            return mav;
        }
    }
