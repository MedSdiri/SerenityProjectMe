package b21.github;



import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static net.serenitybdd.rest.SerenityRest.*;
@SerenityTest
public class GitHubUserTest {

    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "https://api.github.com";
        //basePath= "";
    }

@DisplayName("Testing GET /users/{user_id} endpoint")
    @Test
    public void testGitHubUser(){

        given()
                .pathParam("user_id", "CybertekSchool")
                .log().all()
        .when()
        .get("/users/{user_id}")
        .then()
                .log().all()
                .statusCode(200)
                ;

    }
    @Test
    public void testGitHubUser2(){

        given()
                .pathParam("user_id", "CybertekSchool")
                .log().all()
                .when()
                .get("/users/{user_id}");
// if you send a request using SerenityRest,
// the response object can be obtained from the method called lastResponse() without saving it seperately


        System.out.println("SerenityRest.lastResponse().statusCode() = " + lastResponse().statusCode());
        System.out.println("lastResponse().header(\"Date\") = " + lastResponse().header("Date"));

        System.out.println("lastResponse().path(\"login\") = " + lastResponse().path("login"));
        System.out.println("lastResponse().jsonPath().getInt(\"id\") = " + lastResponse().jsonPath().getInt("id"));

    }

    @Test
    public void testGitHubUser3(){

        given()
                .pathParam("user_id", "CybertekSchool")
                .log().all()
                .when()
                .get("/users/{user_id}")
                ;
        Ensure.that("Request ran successfully", vRes -> vRes.statusCode(200));
        Ensure.that("name field value is Cybertek School",
                p-> p.body("name", is("Cybertek School")));

    }









    @AfterAll
    public static void cleanUp(){
        reset();
    }
}
