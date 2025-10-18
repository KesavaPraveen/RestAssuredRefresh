package RequestResponseApi;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;  // ✅ import this
import java.util.Date;

public class BaseApiTest {

    @BeforeClass
    public void setUp()
    {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        PrintStream logFile;
        try {
            logFile= new PrintStream(new FileOutputStream("logs/api_log_" + timestamp + ".txt"));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException("failed to create the log file....");
        }

        RestAssured.filters(new RequestLoggingFilter(logFile),new ResponseLoggingFilter(logFile));
        //RestAssured.requestSpecification= SpecBuilderUtil.getRequestSpecBuilder();
    }
}
