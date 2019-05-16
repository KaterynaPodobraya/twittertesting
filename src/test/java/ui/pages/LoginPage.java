package ui.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;

    @FindBy(css="#globalContainer #login_form input#email")
    public WebElement email;

    @FindBy(css="#globalContainer #login_form input#pass")
    public WebElement password;

    @FindBy(css="#globalContainer #login_form #loginbutton")
    public WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void open() {
        driver.get("https://www.facebook.com/pg/Sophia-Alcffgjidcdeh-Dingles-342920466364767/");
    }

    public void login(String user, String pass) {
        email.sendKeys(user);
        password.sendKeys(pass);
        loginButton.click();
    }
}
