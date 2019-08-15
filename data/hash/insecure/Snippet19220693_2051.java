@RunWith(PowerMockRunner.class)
@PrepareForTest({MyClass.class, MessageDigest.class})
public class MyClassTest {

    private MyClass myClass = new MyClass();
    @Mock private MessageDigest messageDigestMock;

    @Test
    public void shouldDoMethodCall() throws Exception {
        setupMessageDigest();

        String value = myClass.myMethodCall();

        // I use FestAssert here, you can use any framework you like, but you get
        // the general idea
        Assertions.assertThat(value).isEqualToIgnoringCase("hashed_value");
    }

    public void setupMessageDigest() throws Exception {
        PowerMockito.mockStatic(MessageDigest.class);
        when(MessageDigest.getInstance("SHA1")).thenReturn(messageDigestMock);
        when(messageDigestMock.digest(Matchers.<byte[]>anyObject())).thenReturn("hashed_value".getBytes());
    }

}
