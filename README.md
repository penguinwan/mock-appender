# Introduction
It could be too much boilerplate code to assert log message in unit test, how nice log message could be captured with just a few lines of code.
# Before
```
@RunWith(MockitoJUnitRunner.class)
public class Test {
    @Mock
    private Appender appender;

    @Test
    public void test() {
    	Logger logger = (Logger) LoggerFactory.getLogger(Service.class);
        logger.addAppender(appender);
        .
        .
        ArgumentCaptor<LoggingEvent> logMessageCaptor = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(mockAppender).doAppend(logMessageCaptor.capture());
        assert logMessageCaptor.getValue().getFormattedMessage() == "log message";
    }
} 
```
# After
```
public class Test {
    @Rule
    public MockAppender appender = new MockAppender(Service.class);

    @Test
    public void writeLogWhenExecute() {
        Service service = new Service();
        service.writeLogMessage();

        assert appender.getLoggingEvents().get(0).getFormattedMessage() == "debug message";
    }

}
```
