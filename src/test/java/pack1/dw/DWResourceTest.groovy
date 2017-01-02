package pack1.dw

import com.fasterxml.jackson.jaxrs.annotation.JacksonFeatures
import org.glassfish.jersey.client.ClientConfig
import spock.lang.Specification

import javax.ws.rs.client.Client
import javax.ws.rs.client.ClientBuilder

import static java.lang.String.format
import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static pack1.dw.DWResource.NAME
import static pack1.dw.DWResource.PATH

class DWResourceTest extends Specification {
    void setup() {
        println "Server should be started"
    }

    void cleanup() {
    }

    def "SayHello"() {
        def url = PATH + "?" + NAME + "=Name1"
        println(url)

        expect:
        a == b

        where:
        a | b
        2 | 2
    }

    def "test get"() {
        Client client = ClientBuilder.newClient(new ClientConfig().register(JacksonFeatures.class))
        String uri = format("http://%s:%s/", "localhost", 8080)
        def path = PATH

        def response = client.target(uri).path(path).queryParam(NAME, "Name1").request(APPLICATION_JSON).get()

        expect:
        println(response)
        response.status | 200
        response.readEntity(DWRepresentation.class).getContent() == "Hello, Name1!"
    }
}
