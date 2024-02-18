package AutomationAssignments;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class JulesAssignment {
	WebDriver driver;

	@BeforeTest
	public void setUp() throws InterruptedException {
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\yoges\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://demo.haroldwaste.com/authentication");
		driver.manage().window().maximize();
		login("qa@julesai.com", "QaJULES2023!");
	}

	@Test(priority = 1)
	public void addPurchase() throws InterruptedException {
		driver.findElement(By.xpath("//span[contains(text(),'Add a purchase')]")).click();
		driver.findElement(By.xpath("(//div/label[@class='radiobutton'])[2]")).click();
		Thread.sleep(3000);
		WebElement supplier = driver.findElement(By.xpath("(//div[@role='combobox'])[4]//div//div//input"));
		supplier.sendKeys("Yogesh");
		supplier.sendKeys(Keys.ENTER);
		WebElement supplierSite = driver.findElement(By.xpath("(//div[@role='combobox'])[5]//div//div//input"));
		supplierSite.sendKeys(Keys.ARROW_DOWN);
		supplierSite.sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		driver.findElement(By.xpath("//span[contains(text(),'Next')]")).click();
		Thread.sleep(5000);
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
		WebElement material = driver.findElement(By.xpath("(//div[@role='combobox'])[4]//div//div//input"));
		material.sendKeys("Iron");
		material.sendKeys(Keys.ARROW_DOWN);
		material.sendKeys(Keys.ENTER);
		WebElement Incoterm = driver.findElement(By.xpath("(//div[@role='combobox'])[9]//div//div//input"));
		Incoterm.sendKeys(Keys.ARROW_DOWN);
		Incoterm.sendKeys(Keys.ENTER);
		driver.findElement(By.xpath("//span[contains(text(),'Save')]")).click();
	}

	@AfterTest
	public void tearDown() {
		driver.close();
	}

	public void login(String email, String password) throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@name=\"email\"]")).sendKeys(email);
		driver.findElement(By.xpath("//input[@name=\"password\"]")).sendKeys(password);
		driver.findElement(By.xpath("//span[contains(text(),'Log in')]")).click();
		Thread.sleep(3000);
	}
}
