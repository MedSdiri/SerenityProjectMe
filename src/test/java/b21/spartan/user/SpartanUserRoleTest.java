package b21.spartan.user;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spartan_util.SpartanUtil;

import java.util.HashMap;
import java.util.Map;

@SerenityTest
public class SpartanUserRoleTest {
    @BeforeAll
    public static void init(){

        RestAssured.baseURI = "http://54.92.150.105:7000" ;
        RestAssured.basePath = "/api" ;

    }

    @ParameterizedTest(name = "Test {{index}}  : GET /spartans/{0}")
    @ValueSource(ints = {1, 3, 5, 7, 8, 99})
    public void testUserGetSingleSpartan(int spartanIdArg){
        System.out.println("spartanIdArg = " + spartanIdArg);

        SerenityRest.given()
                .pathParam("id", spartanIdArg)
                .auth().basic("user", "user")
                .when()
                .get("/spartans/{id}")
        ;

        Ensure.that("status code is 200", p-> p.statusCode(200));

    }





    @AfterAll
    public static void cleanup(){

        RestAssured.reset();

    }


}




// @ParameterizedTest(name = "{index} ==> trying to get spartan by id: {0}")