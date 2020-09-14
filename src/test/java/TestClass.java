import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;

import javax.management.monitor.CounterMonitor;
import java.util.List;

public class TestClass extends BaseConfiguration {

    public TestClass()
    {
        super();
    }

    @BeforeSuite
    public void initialSetup()
    {
        initializationMethod();
    }

    @Test(priority=0)
    public void verify_LoginPage_Title()
    {
        CommonMethods.assertWaitForElementToBeClickable(driver, By.xpath("//input[@name='email']"), 50);
        CommonMethods.assertPageTitle(driver, "Cogmento CRM");
    }

    @Test(priority=1)
    public void verify_Application_Login() {
        CommonMethods.assertWaitForElementToBeClickable(driver, By.xpath("//input[@name='email']"), 50);
        CommonMethods.clearAndEnterText(driver, By.xpath("//input[@name='email']"), prop.getProperty("username"));
        CommonMethods.assertWaitForElementToBeClickable(driver, By.xpath("//input[@name='password']"), 50);
        CommonMethods.clearAndEnterText(driver, By.xpath("//input[@name='password']"), prop.getProperty("password"));
        CommonMethods.clickWebElement(driver, By.xpath("//div[text()='Login']"), 50);
        CommonMethods.assertWebElementText(driver, By.xpath("//div[@id='main-nav']/descendant::span[text()='Home']"), "Home");
    }

    @Test(priority=2)
    public void verify_NavigationMenu_Links()
    {
        List<WebElement> listItem=driver.findElements(By.xpath("//div[@id='main-nav']/descendant::span"));
        for(WebElement element : listItem)
        {
            String elementTxt=element.getText();
            System.out.println(elementTxt);
            CommonMethods.assertWebElementText(driver, By.xpath("//div[@id='main-nav']/descendant::span[text()='"+elementTxt+"']"), elementTxt);
        }
    }

    @Test(priority=3)
    public void addContacts() throws InterruptedException {
        Xls_Reader reader = new Xls_Reader("D://Learning//Workspace//ExcelReadWriteDemo//src//main//resources//SampleExcel.xlsx");
        String sheetName = "Details";
        int rowCount = reader.getRowCount(sheetName);
        reader.addColumn(sheetName,"fullname");
        reader.addColumn(sheetName, "status");

        for(int rowNum=2; rowNum<=rowCount; rowNum++) {
            Thread.sleep(3000);
            CommonMethods.clickWebElement(driver, By.xpath("//div[@id='main-nav']/descendant::span[text()='Contacts']"), 50);
            CommonMethods.assertWebElementText(driver, By.xpath("//div[@id='dashboard-toolbar']/descendant::div[text()='Contacts']"), "Contacts");
            CommonMethods.clickWebElement(driver, By.xpath("//div[@id='dashboard-toolbar']/descendant::*[text()='New']"), 50);

            String firstNameValue=reader.getCellData(sheetName, "firstname", rowNum);
            String lastNameValue=reader.getCellData(sheetName, "lastname", rowNum);
            String fullName=firstNameValue+" "+lastNameValue;
            System.out.println(firstNameValue + " " + lastNameValue);
            Thread.sleep(3000);
            CommonMethods.clearAndEnterText(driver,By.xpath("//input[@name='first_name']"),firstNameValue);
            CommonMethods.clearAndEnterText(driver,By.xpath("//input[@name='last_name']"),lastNameValue);
            CommonMethods.clickWebElement(driver,By.xpath("//div[@id='dashboard-toolbar']/descendant::*[text()='Save']"), 50);
            CommonMethods.assertWebElementContainsText(driver,By.xpath("//div[@id='dashboard-toolbar']/descendant::div[text()='"+fullName+"']"), fullName);

            reader.setCellData(sheetName,"fullname",rowNum, fullName);
            reader.setCellData(sheetName,"status",rowNum,"Completed");
            CommonMethods.clickWebElement(driver, By.xpath("//div[@id='main-nav']/descendant::span[text()='Contacts']"), 50);
        }

    }

    @AfterSuite
    public void tearDown()
    {
        driver.close();
    }


}