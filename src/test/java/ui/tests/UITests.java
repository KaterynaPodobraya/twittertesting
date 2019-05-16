package ui.tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import ui.pages.LoginPage;
import ui.pages.ProfilePage;
import ui.pages.WelcomePage;
import ui.wait.Waits;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UITests {
    private WebDriver driver;
    private LoginPage loginPage;
    private WelcomePage welcomePage;
    private ProfilePage profilePage;
    private static String postText = "Test message " + System.currentTimeMillis();
    private static final String user = "sophia_wooupse_dinglesky@tfbnw.net";
    private static final String pass = "testtesttest";

    @BeforeEach
    public void setUp(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        welcomePage = new WelcomePage(driver);
        profilePage = new ProfilePage(driver);
    }

    @Test
    @Order(1)
    public void createFbPost() {
        loginPage.open();
        loginPage.login(user, pass);

        assertEquals("Welcome to Facebook, Sophia", welcomePage.title.getText());
        Waits.forElementToBeVisible(driver, welcomePage.profile);
        welcomePage.profile.click();

        Waits.forElementToBeVisible(driver, profilePage.createPost);
        profilePage.createPost.click();
        profilePage.setPostText(postText);
        profilePage.shareButton.click();
        Waits.forElementToBeInvisible(driver, profilePage.shareButton);
    }

    @Test
    @Order(2)
    public void updateFbPost() {
        loginPage.open();
        loginPage.login(user, pass);

        assertEquals("Welcome to Facebook, Sophia", welcomePage.title.getText());
        Waits.forElementToBeVisible(driver, welcomePage.profile);
        welcomePage.profile.click();

        Waits.forElementToBeVisible(driver, profilePage.createPost);
        profilePage.clickPostOptionsByMessageText(postText);
        Waits.forElementToBeVisible(driver, profilePage.editOptions);
        profilePage.editOptions.click();

        postText += " - Updated!";
        profilePage.setPostText(postText);

        Waits.forElementToBeVisible(driver, profilePage.saveEditPostButton);
        profilePage.saveEditPostButton.click();
        Waits.forElementToBeVisible(driver, profilePage.postsList.get(0));
        assertTrue(profilePage.postsList.stream().filter(p -> p.getText().equals(postText)).collect(Collectors.toList()).size() == 1);
    }

    @Test
    @Order(3)
    public void deleteFbPost(){
        loginPage.open();
        loginPage.login(user, pass);

        assertEquals("Welcome to Facebook, Sophia", welcomePage.title.getText());
        Waits.forElementToBeVisible(driver, welcomePage.profile);
        welcomePage.profile.click();

        Waits.forElementToBeVisible(driver, profilePage.createPost);
        profilePage.clickPostOptionsByMessageText(postText);
        Waits.forElementToBeVisible(driver, profilePage.deleteOptions);
        profilePage.deleteOptions.click();
        Waits.forElementToBeVisible(driver, profilePage.deleteConfirmationButton);
        profilePage.deleteConfirmationButton.submit();
        Waits.forElementToBeVisible(driver, profilePage.postsList.get(0));
        assertTrue(profilePage.postsList.stream().filter(p-> p.getText().equals(postText)).collect(Collectors.toList()).size() == 0);
    }

    @AfterEach
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}
