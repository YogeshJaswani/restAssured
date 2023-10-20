package PayLoad;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import  static io.restassured.RestAssured.*;

import org.testng.Assert;

public class jiraTest {

	public static void main(String[] args) {
		
		RestAssured.baseURI= "http://localhost:8080";
		SessionFilter session= new SessionFilter();
		
		given().log().all().header("Content-Type","application/json").body("{ \"username\": \"yogesh_jaswani\", \r\n"
				+ "\"password\": \"Yog@11304544!\" \r\n"
				+ "}").filter(session)
		.when().post("/rest/auth/1/session").then().log().all().assertThat().statusCode(200);
		
		String expectedComment="Hi,This is a test comment for TestNG assertion";
		String  addCommentResponse= given().pathParam("key", "RES-5").header("Content-Type","application/json").body("{\r\n"
				+ "    \"body\": \""+expectedComment+"\"\r\n"
				+ "   \r\n"
				+ "    }\r\n"
				+ "").log().all().filter(session).when().post("/rest/api/2/issue/{key}/comment")
		.then().assertThat().assertThat().statusCode(201).extract().response().asString();
		
		JsonPath js= new JsonPath(addCommentResponse);
		int addCommentID= js.getInt("id");
		System.out.println("addCommentID :"+addCommentID);
		
		String getIssueResponse=given().pathParam("key", "RES-5").queryParam("fields", "comment").filter(session).when().get("/rest/api/2/issue/{key}")
		.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
		//System.out.println(getIssueResponse);
		
		JsonPath js1= new JsonPath(getIssueResponse);
		int commentsCount=js1.getInt("fields.comment.comments.size()");
		System.out.println("total comments are: "+commentsCount);
		for(int i=0;i<commentsCount;i++)
		{
			int commentId= js1.getInt("fields.comment.comments["+i+"].id");
			System.out.println("comment ids are: "+commentId);
			if(commentId==addCommentID)
			{
				String actualComment= js1.getString("fields.comment.comments["+i+"].body"); 
				Assert.assertEquals(expectedComment, actualComment);
			}
		}
		
		
	}
	

}
