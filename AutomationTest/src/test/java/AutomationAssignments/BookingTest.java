package AutomationAssignments;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class BookingTest {
    WebDriver driver;
    WebDriverWait wait;
    Actions actions;

    private static final String DESTINATION_INPUT = "//div[@data-testid='destination-container']//input";
    private static final String FIRST_SUGGESTION = "//div[@data-testid='autocomplete-result' and contains(.,'New York')]";
    private static final String CHECKIN_DATE = "(//tr//td[not(contains(@class,'--outside-month')) and not(@aria-disabled='true')])[16]";
    private static final String CHECKOUT_DATE = "(//tr//td[not(contains(@class,'--outside-month')) and not(@aria-disabled='true')])[21]";
    private static final String SEARCH_BUTTON = "//span[text()='Search']";
    private static final String FIRST_HOTEL_AVAILABILITY = "(//div[@data-testid='property-card']//a[contains(.,'See availability') or contains(.,'View Deal')])[1]";
    private static final String RESERVE_BUTTON = "(//span[contains(text(),'Reserve')])[1]";
    private static final String ILL_RESERVE_BUTTON = "//div[@class='hprt-reservation-cta']//button";
    private static final String FIRSTNAME_INPUT = "firstname";
    private static final String LASTNAME_INPUT = "lastname";
    private static final String EMAIL_INPUT = "email";
    private static final String PHONE_INPUT = "//input[@name='phoneNumber']";
    private static final String CONTINUE_BUTTON = "//*[text()=' Next: Final details ']";
    private static final String PAYMENT_HEADER = "//h2[@data-testid='payment-timing-header']";

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        actions = new Actions(driver);
        driver.manage().window().maximize();
        driver.get("https://www.booking.com/");
    }

    @Test(priority = 1)
    public void searchHotelsInNY() throws InterruptedException {
        enterDestination("New York");
        selectDates();
        clickSearch();
        WebElement resultSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-testid='property-card']")));
        Assert.assertTrue(resultSection.isDisplayed(), "Search results are not displayed.");
    }

    @Test(priority = 2, dependsOnMethods = "searchHotelsInNY")
    public void openHotelAndReserve() throws InterruptedException {
        openFirstHotel();
        switchToHotelTab();
        clickReserve();
        clickIllReserve();
        WebElement firstNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(FIRSTNAME_INPUT)));
        Assert.assertTrue(firstNameField.isDisplayed(), "Guest form was not opened properly.");
    }

    @Test(priority = 3, dependsOnMethods = "openHotelAndReserve")
    public void fillDetailsAndProceed() {
        fillGuestDetails("Yogesh", "Upadhyay", "yogesh@gmail.com", "7007889977");
        clickContinue();
        WebElement paymentHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PAYMENT_HEADER)));
        Assert.assertTrue(paymentHeader.isDisplayed(), "Did not reach payment page after submitting guest details.");
    }

    private void enterDestination(String city) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(DESTINATION_INPUT))).sendKeys(city);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(FIRST_SUGGESTION))).click();
    }

    private void selectDates() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//nav[@data-testid='datepicker-tabs']")));
        Thread.sleep(1000);
        WebElement checkIn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CHECKIN_DATE)));
        WebElement checkOut = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CHECKOUT_DATE)));
        actions.moveToElement(checkIn).click().perform();
        actions.moveToElement(checkOut).click().perform();
    }

    private void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SEARCH_BUTTON))).click();
    }

    private void openFirstHotel() throws InterruptedException {
        WebElement availabilityLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(FIRST_HOTEL_AVAILABILITY)));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", availabilityLink);
        Thread.sleep(1000);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", availabilityLink);
    }

    private void switchToHotelTab() {
        String originalWindow = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    private void clickReserve() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(RESERVE_BUTTON))).click();
        Thread.sleep(2000);
    }

    private void clickIllReserve() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(ILL_RESERVE_BUTTON))).click();
    }

    private void fillGuestDetails(String first, String last, String email, String phone) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name(FIRSTNAME_INPUT))).sendKeys(first);
        driver.findElement(By.name(LASTNAME_INPUT)).sendKeys(last);
        driver.findElement(By.name(EMAIL_INPUT)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(PHONE_INPUT))).sendKeys(phone);
    }

    private void clickContinue() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(CONTINUE_BUTTON))).click();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
