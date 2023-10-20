import io.restassured.builder.RequestSpecBuilder;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.*;

import org.testng.Assert;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

public class ECommerceAPITest2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RequestSpecification reqSpec= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
		.setContentType(ContentType.JSON).build();
		LoginRequest  loginRequest= new LoginRequest();
		loginRequest.setUserEmail("yogeshj45@gamil.com");
		loginRequest.setUserPassword("Yog101094!");
		
		RequestSpecification loginSpec=given().log().all().spec(reqSpec).body(loginRequest);
		
		
		
				LoginResponse loginResponse=loginSpec.when().post("/api/ecom/auth/login")
											.then().log().all().extract().response().as(LoginResponse.class);
				String token= loginResponse.getToken();
				String userID= loginResponse.getUserId();
				
				//Create Product
				RequestSpecification createProductBaseSpec= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
														.addHeader("Authorization", token).build();
				
				RequestSpecification createProductSpec = given().log().all().spec(createProductBaseSpec)
						.param("productName", "Laptop").param("productAddedBy", userID)
						.param("productCategory","electronics")
						.param("productSubCategory","computers").param("productPrice","11500")
						.param("productDescription","HP").param("productFor","Unisesx").multiPart("productImage",new File( "F:\\Study\\Laptop.jpg"));
						
						

					String createProductResponse= createProductSpec.when().post("/api/ecom/product/add-product")
							.then().log().all().extract()
							.response().asString();
					
					JsonPath js =new JsonPath(createProductResponse);
					String productId= js.getString("productId");
					System.out.println("ID of added product: "+productId);
					//Place Order
					
					OrderDetails orderDetailsObj= new OrderDetails();
					orderDetailsObj.setCountry("India");
					orderDetailsObj.setProductOrderedId(productId);
					
					List<OrderDetails> orderDetailsList= new ArrayList<OrderDetails>();
					orderDetailsList.add(orderDetailsObj);
					
					Orders ordersObj= new Orders();;
					ordersObj.setOrders(orderDetailsList);
					
						
					
					RequestSpecification placeOrderBaseSpec= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
							.setContentType(ContentType.JSON).addHeader("Authorization", token).build();
					
					RequestSpecification placeOrderSpec= given().log().all().spec(placeOrderBaseSpec)
							.body(ordersObj);
					
					String placeOrderResponse=placeOrderSpec.when().post("api/ecom/order/create-order").then().log().all()
					.extract().response().asString();
					
					System.out.println(placeOrderResponse);
					
					//Delete Product
					
					RequestSpecification deleteOrderBaseSpec= new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
							.addHeader("Authorization", token).build();
					
					RequestSpecification deleteOrderSpec =given().log().all().spec(deleteOrderBaseSpec).pathParam("productId", productId);
					
					String deleteProductResponse= 
							deleteOrderSpec.when().delete("/api/ecom/product/delete-product/{productId}").then().log().all()
					.extract().response().asString();
					
					JsonPath js1= new JsonPath(deleteProductResponse);
					Assert.assertEquals("Product Deleted Successfully", js1.getString("message"));
	}

}
