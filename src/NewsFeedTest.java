import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
public class NewsFeedTest {
    @Test
    public void test() throws IOException {
        new NewsFeed("hw4data");
    }
}