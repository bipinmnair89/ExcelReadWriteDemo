import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;
import java.rmi.Remote;
import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CommonMethods {

    public static void waitForDocumentReady(RemoteWebDriver driver, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, seconds);

        wait.pollingEvery(Duration.ofMillis(250L)).withTimeout(Duration.ofSeconds(seconds)).until(input -> {
            RemoteWebDriver driver1 = (RemoteWebDriver) input;
            return (boolean) driver1.executeScript("return document.readyState === 'complete'");
        });
    }

    public static void assertWaitForElementToDisplay(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(findBy));
        assertTrue(element.isDisplayed());
    }

    public static void assertWaitPresenceOfElementLocated(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        assertTrue(element.isDisplayed());
    }

    public static void assertWaitForElementToBeClickable(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(findBy));
        assertTrue(element.isEnabled());
    }

    public static void assertWebElementText(RemoteWebDriver driver, By findBy, String elementText) {
        CommonMethods.assertWaitForElementToDisplay(driver, findBy, 20);
        List<WebElement> elements = driver.findElements(findBy);
        assertTrue(elements.size() == 1, "Find /selection criterion (By) must find exactly one instance!");
        String name = elements.get(0).getText();
        assertEquals(name, elementText);
    }

    public static void assertWebElementContainsText(RemoteWebDriver driver, By findBy, String elementText) {
        assertWaitForElementToDisplay(driver, findBy, 20);
        List<WebElement> elements = driver.findElements(findBy);
        assertTrue(elements.size() == 1, "Find /selection criterion (By) must find exactly one instance!");
        String name = elements.get(0).getText();
        assertTrue(name.contains(elementText));
    }

    public static void clickWebElement(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        wait.until(ExpectedConditions.elementToBeClickable(findBy));
        element.click();
        CommonMethods.waitForDocumentReady(driver, 20);
    }

    public static void clickALink(RemoteWebDriver driver, By findBy, String text) {
        List<WebElement> elements = driver.findElements(findBy);
        assertTrue(elements.size() == 1, "Find /selection criterion (By) must find exactly one instance!");
        WebElement element = elements.get(0);
        if (element.getText().contains(text)) {
            element.click();
            CommonMethods.waitForDocumentReady(driver, 30);
        }
    }

    public static void assertButtonTextAndVisibility(RemoteWebDriver driver, By findBy, String expectedButtonText) {
        List<WebElement> elements = driver.findElements(findBy);
        assertTrue(elements.size() == 1, "Find /selection criterion (By) must find exactly one instance!");

        WebElement element = elements.get(0);
        String actualButtonText = element.getAttribute("value");
        assertEquals(actualButtonText, expectedButtonText);

        boolean isVisible = element.isDisplayed();
        assertTrue(isVisible);
    }

    public static void assertElementVisibility(RemoteWebDriver driver, By findBy) {
        assertWaitForElementToDisplay(driver, findBy, 30);
        WebElement element = driver.findElement(findBy);
        assertTrue(element.isDisplayed());
    }

    public static void assertPageTitle(RemoteWebDriver driver, String text) {
        assertTrue(driver.getTitle().contains(text),"Page title is not displayed correctly");
    }

    public static void clearAndEnterText(RemoteWebDriver driver, By findBy, String text) {
        WebDriverWait wait = new WebDriverWait(driver, 50);
        wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        List<WebElement> elements = driver.findElements(findBy);
        assertTrue(elements.size() == 1, "Find /selection criterion (By) must find exactly one instance!");
        WebElement element = elements.get(0);
        element.clear();
        element.sendKeys(text);
    }

    public static void clearAndEnterTextJSE(RemoteWebDriver driver, By findBy, String text) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        List<WebElement> elements = driver.findElements(findBy);
        assertTrue(elements.size() == 1, "Find /selection criterion (By) must find exactly one instance!");
        WebElement element = elements.get(0);
        element.click();
        element.clear();
        WebElement elementValue=driver.findElement(findBy);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value='"+text+"';", elementValue);

    }

    public static String getattrubitevalue(RemoteWebDriver driver, By findBy) {

        CommonMethods.assertElementVisibility(driver, findBy);
        String attributevalue = driver.findElement(findBy).getAttribute("value");
        return attributevalue;
    }

    public static void mouseHoverElement(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        assertTrue(element.isDisplayed());
        Actions actions = new Actions(driver);
        actions.moveToElement(driver.findElement(findBy)).build().perform();
    }

    public static void clickAndHoldAndRelease(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        assertTrue(element.isDisplayed());
        Actions actions=new Actions(driver);
        actions.clickAndHold(driver.findElement(findBy)).build().perform();
        actions.release().build().perform();
    }

    public static void verifyTableDataExistsWithSoftAssert(RemoteWebDriver driver, By findBy) {
        CommonMethods.assertWaitPresenceOfElementLocated(driver, findBy, 10);
        SoftAssert softAssert=new SoftAssert();
        List<WebElement> listOfRows = driver.findElements(findBy);
        softAssert.assertTrue(listOfRows.size() > 1);
        softAssert.assertAll();
    }

    public static void verifyTableDataIsEmpty(RemoteWebDriver driver, By findBy) {
        List<WebElement> elementList=driver.findElements(findBy);
        assertEquals(elementList.size(), 0, "The table is empty by default");
    }

    public static void acceptJSAlert(RemoteWebDriver driver)
    {
        Alert alert=driver.switchTo().alert();
        System.out.println("The alert is : "+alert.getText());
        alert.accept();

    }

    //JavascriptExecutorScrollTillElement method
    public static void scrollTillElementJSE(RemoteWebDriver driver, By findBy)
    {
        scrollToTopJSE(driver);
        WebElement element = driver.findElement(findBy);
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    //JavascriptExecutorScrollToTop method
    public static void scrollToTopJSE(RemoteWebDriver driver)
    {
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0, 0)");
    }

    //JavascriptExecutorScrollToBottom method
    public static void scrollToBottomJSE(RemoteWebDriver driver)
    {
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
    }

    //Method to handle StaleElementReferenceException
    public static boolean retryingClick(RemoteWebDriver driver, By findBy) {
        boolean result = false;
        int attempts = 0;
        while(attempts < 2) {
            try {

                driver.findElement(findBy).click();
                result = true;
                break;
            } catch(StaleElementReferenceException e) {
            }
            attempts++;
        }
        return result;
    }
    public static void multipleClick(RemoteWebDriver driver, By findBy, int waitTimeInSeconds) {
        int count=0;
        WebDriverWait wait = new WebDriverWait(driver, waitTimeInSeconds);
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(findBy));
        assertTrue(element.isDisplayed());
        do {
            CommonMethods.clickWebElement(driver,findBy,50);
            count++;
        }while(count<5);
    }

    public static void clickJSE(RemoteWebDriver driver, By findBy)
    {
        WebElement element=driver.findElement(findBy);
        JavascriptExecutor js=(JavascriptExecutor)driver;
        js.executeScript("arguments[0].click()", element);
    }

    //JavascriptExecutor method to highlight an element
    public static void highlightElement(RemoteWebDriver driver, By findBy)
    {
        for (int i = 0; i <1; i++)
        {
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                WebElement element=driver.findElement(findBy);
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "color: black; border: 4px solid red;");
                Thread.sleep(200);
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, "");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }






}
