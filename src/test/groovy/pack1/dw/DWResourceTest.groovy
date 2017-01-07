package pack1.dw

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import javax.ws.rs.client.Entity

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static pack1.dw.CommonSpec.after
import static pack1.dw.CommonSpec.beforeRun
import static pack1.dw.DWResource.NAME
import static pack1.dw.ResultCode.OK

class DWResourceTest extends Specification {
    private static final Logger log = LoggerFactory.getLogger(DWResourceTest.class)

    def setupSpec() {
        beforeRun()
    }

    def cleanupSpec() {
        after()
    }

    def common = new CommonSpec()

    def setup() {
        common.createTarget()
    }

    def "test post get"() {
        common.post_get(1111)
        expect:
        "To enable test method"
    }

    private "test empty name"() {
        def defaultName = "Unknown"
        when:
        def entity = Entity.entity("{\"number\":\"$defaultName\"}".toString(), APPLICATION_JSON)
        def response = common.target.request(APPLICATION_JSON).post(entity)

        then:
        response.status | 200
        response.readEntity(DWRepresentation.class).status == OK

        when:
        response = common.target.queryParam(NAME, "").request(APPLICATION_JSON).get()

        then:
        response.status | 200
        response.readEntity(DWRepresentation.class).content == "Account: $defaultName!".toString()
    }
}
