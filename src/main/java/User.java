import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("status")
    private String status;

    @Override
    public String toString() {
        return
                "{" +
                        "\"email\": \"" + email + "\"," +
                        "\"name\": \"" + name + "\"," +
                        "\"gender\": \"" + gender + "\"," +
                        "\"status\": \"" + status + "\"" +
                        "}";
    }
}