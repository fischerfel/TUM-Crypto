    SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);

How do I create this using BC? Is that the equivalent of the SecretKeySpec:

    KeyParameter key = ParameterUtilities.CreateKeyParameter("AES", K);

If it is, can I pass the "AES/CCM/NoPadding" instead of AES as it is done in Java?
