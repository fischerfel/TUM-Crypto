@RunWith(PowerMockRunner.class)
@PrepareForTest({SSLContext.class})
public class ATest {
    @Test
    public void testSomeMethod() throws Exception {
        // create the mock to return by getInstance()
        SSLContext context = PowerMockito.mock(SSLContext.class);

        // mock the static method getInstance() to return above created mock context
        PowerMockito.mockStatic(SSLContext.class);
        Mockito.when(SSLContext.getInstance("SSL")).thenReturn(context);

        // invoke the object under test
        new AClass().someMethod();

        //TODO - add verifications / assertions
    }
}
