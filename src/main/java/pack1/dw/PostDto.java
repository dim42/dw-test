package pack1.dw;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostDto {
    private String number;

    public PostDto() {
        // Jackson deserialization
    }

    public PostDto(String number) {
        this.number = number;
    }

    @JsonProperty
    public String getNumber() {
        return number;
    }
}
