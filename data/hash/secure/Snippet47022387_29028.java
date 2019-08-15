PowerMockito.mockStatic(MessageDigest.class);
PowerMockito.when(MessageDigest.getInstance(Mockito.anyString()))
      .thenThrow(new RuntimeException());
