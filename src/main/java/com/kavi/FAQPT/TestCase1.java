package com.kavi.FAQPT;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import Utilities.ExcelReader;
import Utilities.Take_Screenshot;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCase1 {

	public static WebDriver driver;
	
	// Webdriver specifically for Parallel execution
//	public ThreadLocal<WebDriver> driver = new ThreadLocal<>();

	public Take_Screenshot ts;

	public static ExtentReports ExtentReports; // TO create report
	public static String Report_Sub_Folder;
	public static com.aventstack.extentreports.ExtentTest ExtentTest;
	public String trimmedString;

	@BeforeSuite
	public void Initialize_ExtentReprts() {

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss"); // String
																							// formattedDate=myDateObj.format(myFormatObj);
		Report_Sub_Folder = myDateObj.format(myFormatObj);

		ExtentReports = new ExtentReports();
		ExtentSparkReporter SparkReporter_Alltest = new ExtentSparkReporter(
				"Report\\Report-" + Report_Sub_Folder + "\\TestCases.html");
		ExtentReports.attachReporter(SparkReporter_Alltest);
		SparkReporter_Alltest.config().setTheme(Theme.DARK);
		// to print the specific information in Report
		ExtentReports.setSystemInfo("Java_Version", System.getProperty("java.version"));
		ExtentReports.setSystemInfo("OS", System.getProperty("os.name"));

	}

	@Parameters({ "browser" })
	@BeforeClass
	public void setUp(ITestContext context, String br) throws InterruptedException {
	        WebDriverManager.chromedriver().setup();
	        
	        ChromeOptions options = new ChromeOptions();
	        options.addArguments("--headless"); // Enable headless mode
	        options.addArguments("--disable-gpu"); // Optional for Windows OS
	        options.addArguments("window-size=1920,1080"); // Ensure proper rendering in headless mode
	        
	        driver = new ChromeDriver(options);
	        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); // Use TimeUnit in Selenium 3
	        driver.manage().window().maximize();
	        
	        driver.get("https://bmrdvxoeeutl.objectstorage.ap-mumbai-1.oci.customer-oci.com/n/bmrdvxoeeutl/b/Ultratech-UAT-SDK/o/index.html");

	        ExtentTest = ExtentReports.createTest(context.getName());
	        Capabilities capabilities = ((RemoteWebDriver) driver).getCapabilities();

	        String device = capabilities.getBrowserName() + " " + capabilities.getCapability("version"); // Use "version" in Selenium 3
	        ExtentTest.assignDevice(device);
	        
	        String author = context.getCurrentXmlTest().getParameter("Author");
	        ExtentTest.assignAuthor(author);
	}


	@Test(priority = 1)
	public void OpenAVA() {
		driver.findElement(By.xpath("//div[@class='oda-chat-button-drag-handle']")).click();
		boolean status = driver.findElement(By.xpath(
				"//body/div[@class='oda-chat-wrapper oda-chat-classic oda-chat- oda-chat-expanded']/div[@role='region']/div[@class='oda-chat-conversation']/div[@class='oda-chat-conversation-pane oda-chat-bot-icon oda-chat-user-icon']/div[@role='log']/div[@class='oda-chat-message-block oda-chat-flex oda-chat-left']/div[@class='oda-chat-messages-wrapper oda-chat-flex oda-chat-col']/div[@class='oda-chat-message-list oda-chat-flex oda-chat-col']/div[1]/div[1]/div[1]"))
				.isDisplayed();
		System.out.println("Opening Msg Displayed = " + status);
	}

	@Test(priority = 2)
	public void SayHi() {
		driver.findElement(By.xpath("//textarea[@placeholder='Write your message here']")).sendKeys("HI");
		driver.findElement(By.xpath("//button[@title='Send message']")).click();
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean status = driver.findElement(By.xpath(
				"//body/div[@class='oda-chat-wrapper oda-chat-classic oda-chat- oda-chat-expanded']/div[@role='region']/div[@class='oda-chat-conversation']/div[@class='oda-chat-conversation-pane oda-chat-bot-icon oda-chat-user-icon']/div[@role='log']/div[@class='oda-chat-message-block oda-chat-flex oda-chat-left']/div[@class='oda-chat-messages-wrapper oda-chat-flex oda-chat-col']/div[@class='oda-chat-message-list oda-chat-flex oda-chat-col']/div[1]/div[1]/div[1]"))
				.isDisplayed();
		System.out.println("Main Menu Displayed = " + status);
	}

	@Test(priority = 3)
	public void InputFAQ() {
		String filePath = "E:\\ABG_AVA\\Utterance Worksheet.xlsx";
		String sheetName = "Sheet1";
		List<String> utterances = ExcelReader.getUtterances(filePath, sheetName);
		long startTime = System.currentTimeMillis();
		long duration = 1 * 60 * 1000;

		int i = 1;
		while (System.currentTimeMillis() - startTime < duration) {
			String utterance = ExcelReader.getRandomUtterance(utterances);
			driver.findElement(By.xpath("//textarea[@placeholder='Write your message here']")).sendKeys(utterance);
			driver.findElement(By.xpath("//button[@title='Send message']")).click();

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			boolean status = driver.findElement(By.xpath(
					"//body/div[@class='oda-chat-wrapper oda-chat-classic oda-chat- oda-chat-expanded']/div[@role='region']/div[@class='oda-chat-conversation']/div[@class='oda-chat-conversation-pane oda-chat-bot-icon oda-chat-user-icon']/div[@role='log']/div[3]/div[2]/div[1]/div[3]/div[1]/div[1]"))
					.isDisplayed();
			System.out.println("Response " + i + " for Utterance: " + utterance + " -> Displayed = " + status);
			i++;
		}
	}

	@AfterMethod
	public void After_TC_Failure(Method M, ITestResult result) throws IOException {
		ts = new Take_Screenshot();

		if (result.getStatus() == ITestResult.FAILURE) {
			String ScreenshotPath = ts
					.TS(result.getTestContext().getName() + "//" + result.getMethod().getMethodName());
			String path = Take_Screenshot.A;
			System.out.println("ScreenShotPath ---------------------------------------------- " + path);
			ExtentTest.addScreenCaptureFromPath("./Report - " + ts.image + ".png");
			Throwable Error_msg_of_Console = result.getThrowable();

			System.out.println("Error_msg_of_Console : " + Error_msg_of_Console);
			ExtentTest.fail(Error_msg_of_Console);

		} else if (result.getStatus() == ITestResult.SUCCESS) {
			ExtentTest.pass(M.getName() + " method is Passed");
		}
	}

	@AfterClass
	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@AfterSuite
	public void Generate_ExtentReprts() {
		ExtentReports.flush();
	}
}
