package pack1.dw;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.NonEmptyStringParam;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

@Path(DWResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class DWResource {
    static final String PATH = "/dw-path";
    static final String NAME = "name";

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public DWResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public DWRepresentation processGet(@QueryParam(NAME) NonEmptyStringParam name) {
        final String value = String.format(template, name.get().orElse(defaultName));
        return new DWRepresentation(counter.incrementAndGet(), value);
    }
}
