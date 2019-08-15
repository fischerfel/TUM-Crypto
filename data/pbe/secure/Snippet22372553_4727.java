PBEKeySpec pbeKeySpec = new PBEKeySpec(this.password.toCharArray());
SecretKeyFactory factory =   SecretKeyFactory.getInstance(this.algorithm);             
this.key = factory.generateSecret(pbeKeySpec);
