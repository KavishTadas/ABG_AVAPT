package com.kavi.FAQPT;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import Utilities.ExcelReader;

public class TestCase {

	WebDriver driver;

	@Test(priority = 1)
	public void OpenApp() {
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.get(
				"https://bmrdvxoeeutl.objectstorage.ap-mumbai-1.oci.customer-oci.com/n/bmrdvxoeeutl/b/Ultratech-UAT-SDK/o/index.html");
		driver.manage().window().maximize();
	}

	@Test(priority = 3)
	public void OpenAVA() {
		driver.findElement(By.xpath("//div[@class='oda-chat-button-drag-handle']")).click();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		boolean status = driver.findElement(By.xpath(
				"//body/div[@class='oda-chat-wrapper oda-chat-classic oda-chat- oda-chat-expanded']/div[@role='region']/div[@class='oda-chat-conversation']/div[@class='oda-chat-conversation-pane oda-chat-bot-icon oda-chat-user-icon']/div[@role='log']/div[@class='oda-chat-message-block oda-chat-flex oda-chat-left']/div[@class='oda-chat-messages-wrapper oda-chat-flex oda-chat-col']/div[@class='oda-chat-message-list oda-chat-flex oda-chat-col']/div[1]/div[1]/div[1]"))
				.isDisplayed();
		System.out.println("Opening Msg Displayed = " + status);

	}

	@Test(priority = 5)
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

	@Test(priority = 7)
	public void InputFAQ() {
		String filePath = "E:\\ABG_AVA\\Utterance Worksheet.xlsx"; // Provide correct path
		String sheetName = "Sheet1"; // Provide sheet name
		List<String> utterances = ExcelReader.getUtterances(filePath, sheetName);
		long startTime = System.currentTimeMillis(); // Capture start time
		long duration = 20 * 60 * 1000; // 20 minutes in milliseconds

		int i = 1;
		while (System.currentTimeMillis() - startTime < duration) {
			String utterance = ExcelReader.getRandomUtterance(utterances); // Get a random utterance

			driver.findElement(By.xpath("//textarea[@placeholder='Write your message here']")).sendKeys(utterance);
			driver.findElement(By.xpath("//button[@title='Send message']")).click();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
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

	@Test(priority = 13)
	public void TearDown() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.close();
	}

}
