package b21.spartan.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@Disabled
@SerenityTest
public class SpartanAdminTest {

    @BeforeAll
    public static void init(){

        RestAssured.baseURI = "http://54.92.150.105:7000" ;
        RestAssured.basePath = "/api" ;

    }

    @DisplayName("Test Adming GEt/spartans endpoint")
    @Test
    @Disabled
    public void testAllSpartans(){
        SerenityRest.given()
                .auth().basic("admin", "admin")
                .log().all()
                .when()
                .get("/spartans") ;


        Ensure.that("200 status code", p-> p.statusCode(200));
        Ensure.that("Content Type is JSON", p-> p.contentType(ContentType.JSON));
        Ensure.that("Size is 121", p-> p.body("", hasSize(103)));
        Ensure.that("Response has correct size of 121" ,  vRes-> vRes.body("", hasSize(103) )      ) ;

    }

    @DisplayName("Test public user GET /spartans endpoint")
    @Test
    public void testPublicUserGetData(){
        SerenityRest.get("/spartans");
        // ensure the status code is 401
        Ensure.that("Public user should not be able to get all spartans",
                p-> p.statusCode(401));
    }








    @AfterAll
    public static void cleanup(){

        RestAssured.reset();

    }

}
