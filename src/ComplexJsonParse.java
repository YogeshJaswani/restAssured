import org.testng.Assert;

import PayLoad.PayLoad;
import io.restassured.path.json.*;
public class ComplexJsonParse {

	public static void main(String[] args) {
		JsonPath js=new JsonPath(PayLoad.complexJson());
		int count= js.getInt("courses.size()");
		int purchaseAmount= js.getInt("dashboard.purchaseAmount");
		int sum=0;
		System.out.println("No of courses are "+count);
		System.out.println("Purchase amount is "  +js.getInt("dashboard.purchaseAmount"));
		System.out.println("title of first course "+js.getString("courses[0].title"));
	
		
		for(int i=0;i<count;i++)
		{
			String courseTitle= js.getString("courses["+i+"].title");
			//int coursePrice=js.getInt("courses["+i+"].price");
			//System.out.println("Course Title: "+courseTitle+ "  and Course Price: "+coursePrice);
			if(courseTitle.equalsIgnoreCase("RPA"))
			{
				System.out.println("Price for RPA course is "+js.getInt("courses["+i+"].price"));
				
			}
			int coursePrice=js.getInt("courses["+i+"].price");
			int copies= js.getInt("courses["+i+"].copies");
			sum= sum+(coursePrice*copies);
		}

		System.out.println("Total Purchase Amount is: "+sum);
		Assert.assertEquals(sum, purchaseAmount);

	}

}
