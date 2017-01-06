package pack1.dw;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class DWRepresentation {
    private long id;

    @Length(max = 3)
    private String status;
    private String content;

    public DWRepresentation() {
        // Jackson deserialization
    }

    public DWRepresentation(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public DWRepresentation(long id, String status, String content) {
        this.id = id;
        this.status = status;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}

