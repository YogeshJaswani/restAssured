import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import PayLoad.PayLoad;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DynamicJson {

	@Test(dataProvider="AddBookDetails")
	public void addBook(String isbn, String aisle)
	{
		RestAssured.baseURI="http://216.10.245.166";
		String addBookResponse=given().log().all().body(PayLoad.libraryAddBook(isbn,aisle))
								.when().post("/Library/Addbook.php")
								.then().assertThat().statusCode(200).extract().response().asString();
		
		JsonPath js= new JsonPath(addBookResponse);
		
		String addBookId= js.getString("ID");
		String msg = js.getString("Msg");
		System.out.println("ID for the added book is: "+addBookId);
		System.out.println("message is: "+msg);
		
	
	}
	
	@DataProvider(name="AddBookDetails")
	public Object[][] bookDetails()
	{
		return new Object[][] {{"isbn1","1111"},{"isbn2","2222"},{"isbn3","3333"},{"isbn4","4444"}};
	}
}
