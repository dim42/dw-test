package pack1.dw

import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.ClassRule
import spock.lang.Specification

import javax.ws.rs.client.ClientBuilder
import javax.ws.rs.client.Entity
import javax.ws.rs.client.WebTarget

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

    private WebTarget target

    def setup() {
        target = ClientBuilder.newClient().target(URI).path(PATH)
    }

    def "test post get"() {
        def account = 1111
        (0..2).each { int it ->
            account += it

            when:
            def entity = Entity.entity("{\"number\":\"" + account + "\"}", APPLICATION_JSON)
            def response = target.request(APPLICATION_JSON).post(entity)

            then:
            println(response)
            response.status | 200

            when:
            def name = response.readEntity(DWRepresentation.class).content
            response = target.queryParam(NAME, name).request(APPLICATION_JSON).get()

            then:
            println(response)
            response.status | 200
            response.readEntity(DWRepresentation.class).content == "Account: " + account + "!"
        }
        expect:
        1 == 1
    }

    def "test empty name"() {
        def entity = Entity.entity("{\"number\":\"Unknown\"}", APPLICATION_JSON)
        target.request(APPLICATION_JSON).post(entity)

        def response = target.queryParam(NAME, "").request(APPLICATION_JSON).get()

        expect:
        response.readEntity(DWRepresentation.class).content == "Account: Unknown!"
    }
}
