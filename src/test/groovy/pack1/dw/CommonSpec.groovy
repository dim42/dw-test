package pack1.dw

import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.ClassRule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget

import static java.lang.String.format
import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static pack1.dw.DWResource.NAME
import static pack1.dw.DWResource.PATH
import static pack1.dw.ResultCode.OK

class CommonSpec {
    private static final Logger log = LoggerFactory.getLogger(ConcurrentResourceTest.class)
    @ClassRule
    static final DropwizardAppRule<DWConfiguration> RULE = new DropwizardAppRule<>(DWApplication.class, ResourceHelpers.resourceFilePath("dw.yml"))
    static final String URI = format("http://%s:%s/", "localhost", 8080)

    static void beforeRun() {
        RULE.testSupport.before()
        RULE.application.run()
    }

    static after() {
        RULE.testSupport.after()
    }

    private WebTarget target

    void createTarget() {
        target = ClientBuilder.newClient().target(URI).path(PATH)
    }

    def post_get = {
        when:
        def entity = Entity.entity("{\"number\":\"" + it + "\"}".toString(), APPLICATION_JSON)
        def response = target.request(APPLICATION_JSON).post(entity)

        then:
        log.debug "Post response: $response"
        def representation = response.readEntity(DWRepresentation.class)
        representation.status == OK

        when:
        def name = representation.content
        response = target.queryParam(NAME, name).request(APPLICATION_JSON).get()

        then:
        log.debug "Get response: $response"
        response.status | 200
        response.readEntity(DWRepresentation.class).content == "Account: $it!".toString()
    }
}
