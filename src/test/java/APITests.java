import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static io.restassured.RestAssured.given;


public class APITests {
    String userAccessToken = "EAAol18zMe5UBAEpFX9qbFX2lEHhS988ewApyykDkgwXhirP530mK8jOW0P6igY00x2eRBgVHZAZAPekqAXQOmm0hPVTKku4nrGdXmAuw8Lkf3fbAOoZBj0C0wfOIZCRRC2keUXzfx0k3wJx6qdnicKqwvOhhMzVe2QVLq7tYGgjHqElLM18T";
    String pageId = "342920466364767";
    String pageToken;

    @Before
    public void setUP() {
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


    @Ignore
    @Test
    public void checkThatPostIsShared() {

    }
}
