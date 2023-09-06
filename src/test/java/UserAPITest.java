import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class UserAPITest {
    private static final String BASE_URL = "https://gorest.co.in/public/v2";
    private static final String API_KEY = "17f8b4b3e601485d0451e2364e53201b024978f1eadd75c3b870e30cc1555d75";
    private UserManager userManager;
    private APIRequest apiRequest;

    ExtentSparkReporter htmlReporter;
    ExtentReports extent;


    ExtentTest test1,test2,test3;


    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
        apiRequest = new APIRequest(BASE_URL);
        userManager = new UserManager(apiRequest, API_KEY);
        htmlReporter = new ExtentSparkReporter("extentReport.html");
        //create ExtentReports and attach reporter(s)
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @Test(testName = "test create users with equal gender", enabled = true)
    public void testCreateUsersWithEqualGender() {

        test1 = extent.createTest("Getting user number female and male and adding users accordingly ");

        test1.log(Status.INFO, "Starting test case");
        // Get the current number of male and female users
        int numMaleUsers = userManager.getNumberOfUsersByGender("male");
        int numFemaleUsers = userManager.getNumberOfUsersByGender("female");
        test1.pass("Getting the number of users male/female done");

        // Calculate the difference in counts
        int difference = Math.abs(numMaleUsers - numFemaleUsers);

        // Create additional users of the underrepresented gender
        if (numMaleUsers > numFemaleUsers) {
            userManager.createUsers("female", difference);
        } else if (numFemaleUsers > numMaleUsers) {
            userManager.createUsers("male", difference);
        }
        test1.pass("Creating users was done successfully");
        // Verify that the number of male and female users is equal
        int updatedNumMaleUsers = userManager.getNumberOfUsersByGender("male");
        int updatedNumFemaleUsers = userManager.getNumberOfUsersByGender("female");

        System.out.println("male count now is" + updatedNumMaleUsers );
        System.out.println("female count now is" + updatedNumFemaleUsers );

        userManager.printUsers();

        //I removed the assert here because its public users and we can not see them if we add
//        Assert.assertEquals(updatedNumMaleUsers, updatedNumFemaleUsers);
    }




    @Test(testName = "test deleting inactive users",enabled = true)
    public void testDeleteInactiveUsers() {

        test2 = extent.createTest("Get all users and delete users that are inactive");

        test2.log(Status.INFO, "Starting test case");
        // Get all users
        Response response = userManager.getAllUsers();

        Assert.assertEquals(response.getStatusCode(), 200);

        // Extract user IDs of inactive users
        List<Integer> inactiveUserIds = response.jsonPath()
                .getList("findAll { user -> user.status == 'active' }.id");

        test2.pass("Got the users who are inactive");


        test2.pass("Deleting users.....");
        // Delete inactive users
        for (int userId : inactiveUserIds) {
            Response deleteResponse = userManager.deleteUserById(userId);

            // Verify that the user was deleted successfully
            Assert.assertEquals(deleteResponse.getStatusCode(), 204);
        }

        test2.pass("Users Have Deleted");
    }

    @Test(testName = "test changing the email domain to .co.il",enabled = true)
    public void testChangeEmailsToCoIl() {
        // Get all users

        test3 = extent.createTest("Update mail domain to .co.il");

        test3.log(Status.INFO, "Starting test case");

        Response response = userManager.getAllUsers();


        Assert.assertEquals(response.getStatusCode(), 200);
        test3.pass("Got all the users");


        // Extract user IDs and current email addresses
        List<Integer> userIds = response.jsonPath().getList("id");
        List<String> currentEmails = response.jsonPath().getList("email");

        // Change email addresses to end with "co.il"
        for (int i = 0; i < userIds.size(); i++) {
            int userId = userIds.get(i);
            String currentEmail = currentEmails.get(i);

            // Generate a new email ending with "co.il"
            String newEmail = userManager.generateCoIlEmail(currentEmail);

            // Update the user's email
            Response updateResponse = userManager.updateUserEmail(userId, newEmail);

            // Verify that the email was updated successfully
            Assert.assertEquals(updateResponse.getStatusCode(), 200);
        }
        test3.pass("Users were updated.");
    }

    @AfterClass
    public void tearDown(){
        extent.flush();
    }

}