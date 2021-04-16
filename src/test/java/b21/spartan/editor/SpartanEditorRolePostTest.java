package b21.spartan.editor;

import config_util.ConfigReader;
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
import org.junit.jupiter.params.provider.CsvSource;
import spartan_util.SpartanUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanEditorRolePostTest {

    @BeforeAll
    public static void init(){

        RestAssured.baseURI = "http://54.92.150.105:7000" ;
        RestAssured.basePath = "/api" ;

    }

    @DisplayName("Editor Should be able Post Valid Data")
    @Test
    public void testEditorPostData(){
        Map<String, Object> bodyMap = SpartanUtil.getRandomSpartanMap();

        SerenityRest.given()
                .auth().basic("editor", "editor")
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap)
                .when()
                .post("/spartans").prettyPeek()
                ;
        Ensure.that("It ran successfully", p-> p.statusCode(201));
        Ensure.that("Response format", p-> p.contentType(ContentType.JSON));

        Ensure.that("Success message is correct", p-> p.body("success", is("A Spartan is Born!")));
        Ensure.that("Id was generated", p-> p.body("data.id", notNullValue()));

        Ensure.that("name is correct "+lastResponse().path("data.name"), p-> p.body("data.name", is(bodyMap.get("name"))));
        Ensure.that("gender is correct "+lastResponse().path("data.gender"), p-> p.body("data.gender", is(bodyMap.get("gender"))));
        Ensure.that("phone is correct "+lastResponse().path("data.phone"), p-> p.body("data.phone", is(bodyMap.get("phone"))));

        String newId = lastResponse().path("data.id").toString();

        Ensure.that("location header ends with "+newId, p-> p.header("location", endsWith(newId)));


    }


    @AfterAll
    public static void cleanup(){

        RestAssured.reset();

    }

    // TODO : Add Parameterized Test for Positive Valid Data
    // We will need to provide name , gender , phone for each iteration
    @ParameterizedTest
    @CsvSource({
            "Ercan Civi , Male , 7133306302",
            "Muhammad , Male , 9293215645",
            "Inci, Female, 7038142311"
    })
    public void testPostValidDateWithCSVSource(String nameArg, String genderArg, long phone){

        System.out.println("nameArg = " + nameArg);
        System.out.println("genderArg = " + genderArg);
        System.out.println("phone = " + phone);

        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name" , nameArg) ;
        bodyMap.put("gender" , genderArg) ;
        bodyMap.put("phone" , phone) ;

        System.out.println("bodyMap = " + bodyMap);

        SerenityRest.given()
                .auth().basic(ConfigReader.getProperty("serenity.project.name"),ConfigReader.getProperty("serenity.project.name"))
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap).
                when()
                .post("/spartans").prettyPeek() ;
        // Do all assertions here
        Ensure.that("It ran successfully", thenSection-> thenSection.statusCode( equalTo(201)  ) );
        Ensure.that("Response format is correct" , thenSection -> thenSection.contentType(ContentType.JSON) );

        Ensure.that("success message is correct" , v -> v.body("success", is("A Spartan is Born!")   )       ) ;
        Ensure.that("ID is generated and not null" , v-> v.body("data.id" , notNullValue()      ) ) ;
        // checking actual data
        Ensure.that("name is correct" ,
                v-> v.body("data.name" ,  is(bodyMap.get("name")) )
        ) ;
        Ensure.that("gender is correct" ,
                v-> v.body("data.gender" ,  is(bodyMap.get("gender")) )
        ) ;
        Ensure.that("phone is correct" ,
                v-> v.body("data.phone" ,  is(bodyMap.get("phone")) )
        ) ;
        // check Location header end with newly generated id
        String newId = lastResponse().path("data.id").toString() ;
        System.out.println(   lastResponse().header("Location")   );

        Ensure.that("location header end with "+ newId ,
                v-> v.header("Location" , endsWith(newId)  )
        )  ;


    }


    // TODO : Add Parameterized Test for negative invalid Data
    @ParameterizedTest
    @CsvSource({
            "E , male , 713302 , 3",
            "Muhammad , Male , 123 , 1",
            "Inci is from Batch 21, female, 7038142311 , 2"
    })
    public void testInvalidData(String nameArg, String genderArg, long phone , int expectedErrorCount) {
        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name", nameArg);
        bodyMap.put("gender", genderArg);
        bodyMap.put("phone", phone);
        System.out.println("bodyMap = " + bodyMap);
        SerenityRest.given()
                .auth().basic(ConfigReader.getProperty("serenity.project.name"), ConfigReader.getProperty("serenity.project.name"))
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap).
                when()
                .post("/spartans").prettyPeek();

        // all iterations response status should be 400 BAD Request
        Ensure.that("Status code is 400", p-> p.statusCode(400));
        // And error count should match the expected error count
        Ensure.that("Error count is 3", p-> p.body("errors", hasSize(expectedErrorCount)));
        // message field should report correct error count
        //Ensure.that("")
        // for example if you have 3 errors it should be (Validation failed for object='spartan'. Error count: 3)


    }


    @Test
    public void testingConfigReaderUtility(){

        System.out.println(ConfigReader.getProperty("serenity.project.name"));
        System.out.println(ConfigReader.getProperty("spartan.rest.url"));
        System.out.println(ConfigReader.getProperty("spartan.rest.url"));


    }
}
