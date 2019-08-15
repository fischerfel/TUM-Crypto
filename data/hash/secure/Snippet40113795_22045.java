//Get old password hash
def oldPassword=user?.password
        String authPassword
        if (oldPassword) {
            def uid = user.password + new UID().toString() + prng.nextLong() + System.currentTimeMillis()
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(uid.getBytes("UTF-8"));
            def token1 = hash.encodeBase64()
            user.password = token1
            //Generate a random password
            authPassword = token1
            //Update user Password to be random Password
            UsernamePasswordAuthenticationToken uat1 = new UsernamePasswordAuthenticationToken(user.username, authPassword, null)
            uat1.setDetails(user)
            SecurityContext context = SecurityContextHolder.getContext()
            //Re-setAuthentication of springSecurity using new Password
            context.setAuthentication(uat1)
            if (oldPassword) {
                //Now we are authenticated let's set back the original Hash as the hash we collected in oldPassword 
                //just before doing the hack
                user.rawPassword = oldPassword
                user.save(flush: true)
            }
            springSecurityService.reauthenticate user.username
        }
