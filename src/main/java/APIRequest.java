import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIRequest {
    private final String BASE_URL;

    public APIRequest(String baseUrl) {
        this.BASE_URL = baseUrl;
    }

    public Response sendGetRequest(String endpoint) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .get(endpoint);
    }

    public Response sendPostRequest(String endpoint, String requestBody, String apiKey) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(endpoint);
    }

    public Response sendPutRequest(String endpoint, String requestBody, String apiKey) {
        return RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .put(endpoint);
    }

    public Response sendDeleteRequest(String endpoint, String apiKey) {
        return RestAssured.given()
                .header("Authorization", "Bearer " + apiKey)
                .baseUri(BASE_URL)
                .delete(endpoint);
    }
}
