package api;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class APITests {
    private final static String userAccessToken = "EAAol18zMe5UBACxwgvwOMblPF8jMZB0FlJXYu1ruwsh3y22riJISZCDDz0TswBD05SAxkZBcKHk399JY0Qtmh02ZAfuTDCFDaOksW7S0CSxjRm0T58ZAPoxOqy052si4feqoowQZAJZB1ZAZCJGP2g3BnhR1LnZAvPoTbzolZAaFWZAZCIgM5uURnZAPAx";
    private final static String pageId = "342920466364767";
    private String pageToken;

    @BeforeAll
    public void setUp() {
        RestAssured.baseURI = "https://graph.facebook.com";
        RestAssured.defaultParser = Parser.JSON;
        pageToken = getPageToken();
    }

    public String getPageToken() {
       return given().auth()
            .oauth2(userAccessToken)
            .when()
            .get("/" + pageId + "?fields=access_token")
            .then()
            .statusCode(200)
            .extract()
            .path("access_token");
    }

    public String createPost() {
        return given().auth()
            .oauth2(userAccessToken)
            .params("message", "Hello World !!! " + System.currentTimeMillis(), "access_token", pageToken)
            .when()
            .post(pageId + "/feed?message")
            .then()
            .statusCode(200)
            .extract()
            .path("id");
    }

    @Test
    public void checkThatPostIsCreated() {
        String fbPostId = createPost();

        given().auth()
            .oauth2(userAccessToken)
            .when()
            .get("/" + fbPostId)
            .then()
            .statusCode(200);
    }

    @Test
    public void checkThatPostIsUpdated() {
       String fbPostId = createPost();

       given().auth()
            .oauth2(userAccessToken)
            .params("message", "Updated text !!!", "access_token", pageToken)
            .when()
            .post("/" + fbPostId)
            .then()
            .statusCode(200);

       given().auth()
            .oauth2(userAccessToken)
            .when()
            .get("/" + fbPostId)
            .then()
            .statusCode(200)
            .and()
            .extract()
            .path("message")
            .equals("Updated text !!!");
    }

    @Test
    public void checkThatPostIsDeleted() {
        String fbPostId = createPost();

        given().auth()
            .oauth2(userAccessToken)
            .params("access_token", pageToken)
            .when()
            .delete("/" + fbPostId)
            .then()
            .statusCode(200);

        given().auth()
            .oauth2(userAccessToken)
            .when()
            .get("/" + fbPostId)
            .then()
            .statusCode(400);
    }
}
