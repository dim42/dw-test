package pack1.dw

import spock.lang.Specification

import javax.ws.rs.client.Entity

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static pack1.dw.CommonSpec.after
import static pack1.dw.CommonSpec.beforeRun
import static pack1.dw.DWResource.NAME
import static pack1.dw.ResultCode.OK

class DWResourceTest extends Specification {

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
        given:
        common.post_get(1111)
    }

    private "test empty name"() {
        def defaultName = "Unknown"
        when:
        def entity = Entity.entity("{\"number\":\"$defaultName\"}".toString(), APPLICATION_JSON)
        def response = common.target.request(APPLICATION_JSON).post(entity)

        then:
        def representation = response.readEntity(DWRepresentation.class)
        assert response.status | 200, representation.content
        assert representation.status == OK, representation.content

        when:
        response = common.target.queryParam(NAME, "").request(APPLICATION_JSON).get()

        then:
        response.status | 200
        response.readEntity(DWRepresentation.class).content == "Account: $defaultName!".toString()
    }
}
