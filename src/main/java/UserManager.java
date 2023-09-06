
import io.restassured.response.Response;

public class UserManager {

    private APIRequest apiRequest;
    private final String API_KEY;


    public UserManager(APIRequest apiRequest, String apiKey){
        this.apiRequest = apiRequest;
        this.API_KEY = apiKey;
    }

    // Function to generate a random ID
    private int generateRandomId() {
        return (int) (Math.random() * 1000580); // You can adjust the range as needed
    }
    public Response createUser(String gender) {
        String createUserEndpoint = "/users";

        int id = generateRandomId();

        User user = new User();

        user.setEmail("user" + id + "@example.com");
        user.setName("User " + id);
        user.setGender(gender);
        user.setStatus("active");

        System.out.println(user);
        return apiRequest.sendPostRequest(createUserEndpoint, user.toString(), API_KEY);
    }
    public void createUsers(String gender, int count) {

        for (int i = 0; i < count; i++) {
            Response response = createUser(gender);
            System.out.print(response.getBody().asString());
            response.then().statusCode(201);
        }
    }


    public int getNumberOfUsersByGender(String gender) {
        String getUsersEndpoint = "/users";
        Response response = apiRequest.sendGetRequest(getUsersEndpoint);
        response.then().statusCode(200);
        return response.jsonPath().getList("findAll { it.gender == '" + gender + "' }").size();
    }

    public Response getAllUsers() {
        String getUsersEndpoint = "/users";
        Response response = apiRequest.sendGetRequest(getUsersEndpoint);
        return response;
    }

    public Response deleteUserById(int userId) {
        String deleteUserEndpoint = "/users/" + userId;
        Response response = apiRequest.sendDeleteRequest(deleteUserEndpoint, API_KEY);
        return response;
    }

    public String generateCoIlEmail(String currentEmail) {
        // Replace the domain of the current email with "co.il"
        return currentEmail.replaceFirst("@[a-zA-Z.]+", "@co.il");
    }

    public Response updateUserEmail(int userId, String newEmail) {
        String updateUserEndpoint = "/users/" + userId;
        String requestBody = "{ \"email\": \"" + newEmail + "\" }";

        Response response = apiRequest.sendPutRequest(updateUserEndpoint, requestBody, API_KEY);
        return response;
    }

    public void printUsers() {
        Response response = getAllUsers();
        // Extract and print user details
        if (response.getStatusCode() == 200) {
            System.out.println("List of Users:");

            String responseBody = response.getBody().asString();
            System.out.println(responseBody);
            } else{
                System.err.println("Failed to retrieve user data. Status Code: " + response.getStatusCode());
            }

        }
    }