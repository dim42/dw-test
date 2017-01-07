package pack1.dw;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

public class DWRepresentation {
    private long id;

    private ResultCode status;
    @Length(max = 3)
    private String content;

    public DWRepresentation() {
        // Jackson deserialization
    }

    public DWRepresentation(long id, ResultCode status) {
        this.id = id;
        this.status = status;
    }

    public DWRepresentation(long id, ResultCode status, String content) {
        this.id = id;
        this.status = status;
        this.content = content;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty("status")
    public ResultCode getStatus() {
        return status;
    }

    @JsonProperty
    public String getContent() {
        return content;
    }
}

