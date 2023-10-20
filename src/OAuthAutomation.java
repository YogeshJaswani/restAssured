import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
public class OAuthAutomation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String url= "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AfJohXmOXrjuXzuNwy1W63PFnnQljRBAzOIZ13tC266ShfKnPQzcoXrWyIzqb5HmNgFeJg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String partialCode= url.split("code=")[1];
		String code=partialCode.split("&scope")[0];
		System.out.println(code);
		
		String accessTokenResponse= given().urlEncodingEnabled(false).log().all().queryParams("code","4%2F0AfJohXnmPXyLwq52zsOX4ACSEpaTKlJyI8U-Tqyr1P07HkQ2yn0DshuqTG_dbswwTMZigw").queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W").queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code").when().post("https://www.googleapis.com/oauth2/v4/token")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js= new JsonPath(accessTokenResponse);
		
		String accessToken=js.getString("access_token");
	
		
		String actualresponse=given().log().all().queryParams("access_token", accessToken).when().get("https://rahulshettyacademy.com/getCourse.php")
		.then().log().all().extract().response().asString();
	}

}
