package b21.library;

import config_util.ConfigReader;
import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.Test;
import config_util.ConfigReader;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import static net.serenitybdd.rest.SerenityRest.*;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@SerenityTest
public class LibraryTest {

    @BeforeAll
    public static void init(){
        baseURI  = ConfigReader.getProperty("base.url");
        basePath = ConfigReader.getProperty("base.path") ;
//        token =  getToken();
    }



    @DisplayName("Login and Get Dashboard info")
    @Test
    public void testDashboardStats(){

        String token = given()
                .contentType(ContentType.URLENC)
                .formParam("email", ConfigReader.getProperty("librarian.username"))
                .formParam("password", ConfigReader.getProperty("librarian.password"))
                .when()
                .post("/login").prettyPeek()
                .path("token");
        System.out.println("token = " + token);
        // send GET /dashboard_stats Request and manually verify the number

        SerenityRest.given()
                .header("X-LIBRARY-TOKEN", token)
                .when()
                .get("/dashboard_stats").prettyPeek()
                ;





    }


    @Test
    public void testStash(){
        // oops I did it in the master
    }







    @AfterAll()
    public static void cleanup() {
        reset();
    }
}
