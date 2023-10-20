import org.testng.Assert;
import org.testng.annotations.Test;

import PayLoad.PayLoad;
import io.restassured.path.json.JsonPath;

public class SumValidation {

	@Test 
	public void sumOfCourses()
	{
		JsonPath js= new JsonPath(PayLoad.complexJson());
		int count = js.getInt("courses.size()");
		int sum=0;
		int purchaseAmount= js.getInt("dashboard.purchaseAmount");
		for(int i=0; i<count;i++)
		{
			int coursePrice= js.getInt("courses["+i+"].price");
			int courseCopies= js.getInt("courses["+i+"].copies");
			sum=sum+coursePrice*courseCopies;
			
		}
		System.out.println("Total purchase amount is: "+ sum);
		Assert.assertEquals(sum, purchaseAmount);
	}
	
}
