import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.path.json.JsonPath;

import org.testng.Assert;



import PayLoad.PayLoad;




public class Basics {

	public static void main(String[] args) {
		
		
		RestAssured.baseURI= "https://rahulshettyacademy.com";
		
		String response=given().log().all().queryParam("key", "qaclick123")
		.body(PayLoad.AddPlace())
		.when().post("/maps/api/place/add/json")
		.then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		JsonPath js = new JsonPath(response);
		String placeId= js.getString("place_id");
		String newAddress= "70 Summer walk, USA";
		
		given().queryParam("key", "qaclick123").body("{\r\n"
				+ "\"place_id\":\""+placeId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("/maps/api/place/update/json")
		.then().assertThat().and().statusCode(200).body("msg",equalTo("Address successfully updated"));
		
		String getPlaceResponse= given().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		.when().get("/maps/api/place/get/json")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js1=new JsonPath(getPlaceResponse);
		String actualAddress=js1.getString("address");
		Assert.assertEquals(actualAddress,newAddress);
		
	}

}
