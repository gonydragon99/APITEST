package basicRestAssured;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateProjectTest {

    /*
     *  given() --> configuration: headers/ params / auth / body
     *  when() --> method : url
     *  then() --> response: headers/ body / response code / msg code / etc
     *             verification
     *             extract
     *  log
     * */
    @Test
    public void verifyCreateProject(){
        JSONObject body= new JSONObject();
        body.put("Content","New Itemmm");

        //OBTIENE EL TOKEN
        Response response=given()
                .auth()
                .preemptive()
                .basic("bootAPI@bootAPI.com","12345")
                .log().all()
                .when()
                .get("https://todo.ly/api/authentication/token.json");

        response.then()
                .log().all();
        //EXTRAE EL TOKEN
        String tok = response.then().extract().path("TokenString");

        //CREATE
        response=given()
                .headers("token",tok)
                .body(body.toString())
                .log().all()
                .when()
                .post("https://todo.ly/api/items.json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Content",equalTo("New Itemmm"));

        int idItem = response.then().extract().path("Id");


        body.put("Content","updateeeee");

        //UPDATE
        response=given()
                .headers("token",tok)
                .body(body.toString())
                .log().all()
                .when()
                .post("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Content",equalTo("updateeeee"));


        //DELETE
        response=given()
                .headers("token",tok)
                .body(body.toString())
                .log().all()
                .when()
                .delete("https://todo.ly/api/items/"+idItem+".json");

        response.then()
                .log().all()
                .statusCode(200)
                .body("Deleted",equalTo(true))
                .body("Content",equalTo("updateeeee"));

    }

}
