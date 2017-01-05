package pack1.dw

import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.ClassRule
import spock.lang.Specification

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder

import static java.lang.String.format
import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static pack1.dw.DWResource.NAME
import static pack1.dw.DWResource.PATH

class DWResourceTest extends Specification {
    @ClassRule
    private static final DropwizardAppRule<DWConfiguration> RULE = new DropwizardAppRule<>(DWApplication.class, ResourceHelpers.resourceFilePath("dw.yml"))
    private static final String URI = format("http://%s:%s/", "localhost", 8080)

    def setupSpec() {
        RULE.testSupport.before()
        RULE.application.run()
    }

    private Client client

    def setup() {
        client = ClientBuilder.newClient()
    }

    def "test get"() {
        def response = client.target(URI).path(PATH).queryParam(NAME, "Name1").request(APPLICATION_JSON).get()

        expect:
        println(response)
        response.status | 200
        response.readEntity(DWRepresentation.class).getContent() == "Hello, Name1!"
    }

    def "test empty name"() {
        def response = client.target(URI).path(PATH).queryParam(NAME, "").request(APPLICATION_JSON).get()

        expect:
        response.readEntity(DWRepresentation.class).getContent() == "Hello, Unknown!"
    }
}
