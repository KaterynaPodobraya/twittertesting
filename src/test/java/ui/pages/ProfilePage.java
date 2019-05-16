package ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class ProfilePage {
    WebDriver driver;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath="//span/span[text()='Create Post']")
    public WebElement createPost;

    @FindBy(xpath="//div[@data-contents='true']//span[@data-text='true']")
    public WebElement postTextInput;

    @FindBy(css="span[data-offset-key]")
    public WebElement postContent;

    @FindBy(css="button[data-testid='react-composer-post-button']")
    public WebElement shareButton;

    @FindBy(css="div[data-testid='post_message']")
    public List<WebElement> postsList;

    @FindBy(xpath="//li[@data-feed-option-name='FeedDeleteOption']")
    public WebElement deleteOptions;

    @FindBy(xpath="//form//button[ text()='Delete']")
    public WebElement deleteConfirmationButton;

    @FindBy(xpath="//li[@data-feed-option-name='FeedEditOption']")
    public WebElement editOptions;

    @FindBy(xpath="//button[text()='Save']")
    public WebElement saveEditPostButton;

    public void setPostText(String postText){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("el = document.querySelector('span[data-offset-key]');" +
                            "el.dispatchEvent(new Event('click', {'bubbles': true, 'cancelable': false}));" +
                            "el.innerText='" + postText +"';" +
                            "el.dispatchEvent(new Event('input', {'bubbles': true, 'cancelable': false}));"
                            );
    }

    public void clickPostOptionsByMessageText(String postText){
        System.out.println(postText);
        driver.findElement(By.xpath("//div[@data-testid='post_message']/div/p[text()='" + postText + "']/../../../..//a")).click();
    }
 }
