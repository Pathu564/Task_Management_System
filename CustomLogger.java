import java.util.logging.Level;
import java.util.logging.Logger;


public class CustomLogger {
    private static final Logger LOGGER = Logger.getLogger(Logger.class.getName());

   
    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }
}
