package pack1.dw;

import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import pack.db.Account;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.String.format;
import static pack1.dw.ResultCode.FAIL;
import static pack1.dw.ResultCode.OK;

@Path(DWResource.PATH)
@Produces(MediaType.APPLICATION_JSON)
public class DWResource {
    static final String PATH = "/dw-path";
    static final String NAME = "name";

    private final String template;
    private final String defaultName;
    private final AtomicLong counter;
    private final DWDao dao;

    public DWResource(String template, String defaultName) {
        this.template = template;
        this.defaultName = defaultName;
        this.counter = new AtomicLong();
        this.dao = new DWDao();
    }

    @POST
    @Timed
    public DWRepresentation processPost(PostDto dto) {
        try {
            AccountDto account = dao.newAccount(dto.getNumber());
            return new DWRepresentation(counter.incrementAndGet(), OK, account.getNumber());
        } catch (Exception e) {
            return new DWRepresentation(counter.incrementAndGet(), FAIL);
        }
    }

    @GET
    @Timed
    public DWRepresentation processGet(@QueryParam(NAME) NonEmptyStringParam name) {
        try {
            String notEmptyName = name.get().orElse(defaultName);
            Account account = dao.getAccount(notEmptyName);
            return new DWRepresentation(counter.incrementAndGet(), OK, format(template, account.getNumber()));
        } catch (Exception e) {
            return new DWRepresentation(counter.incrementAndGet(), FAIL);
        }
    }
}
