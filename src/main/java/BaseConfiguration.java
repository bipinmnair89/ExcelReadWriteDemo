import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseConfiguration {

    public static Properties prop;
    public static RemoteWebDriver driver;
    public WebDriverWait wait;

    //constructor of base class with the code for properties file
    public BaseConfiguration()
    {
        try {
            prop=new Properties();
            FileInputStream ip = new FileInputStream("./src/main/resources/config.properties");
            prop.load(ip);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    //initialization method to initialize browser, load the url
    public static void initializationMethod()
    {
        String browser=prop.getProperty("browser");
        if(browser.equalsIgnoreCase("chrome"))
        {
            ChromeOptions options=new ChromeOptions();
//            options.setBinary("C://TEMP//Programs//Softwares//GoogleChromePortable//App//Chrome-bin//chrome.exe");
            System.setProperty("webdriver.chrome.driver", "D://Learning//JARFiles//chromedriver.exe");
            if(prop.getProperty("headless").equals("yes"))
            {
                ChromeOptions optionsHeadless=new ChromeOptions();
                options.setHeadless(true);
                driver=new ChromeDriver(optionsHeadless);
            }
            else
            {
                driver=new ChromeDriver(options);
            }

        }else if(browser.equalsIgnoreCase("firefox"))
        {
            System.setProperty("webdriver.gecko.driver", "D://Learning//JARFiles//geckodriver.exe");
            if(prop.getProperty("headless").equals("yes"))
            {
                FirefoxOptions options=new FirefoxOptions();
                options.setHeadless(true);
                driver=new FirefoxDriver(options);
            }
            else
            {
                driver=new FirefoxDriver();
            }

        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.get(prop.getProperty("url"));


    }

}
