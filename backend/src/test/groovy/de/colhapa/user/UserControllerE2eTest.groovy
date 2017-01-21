package de.colhapa.user

import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import de.colhapa.BackendApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebIntegrationTest(randomPort = true)
@DatabaseSetup(["/users.xml"])
@SpringApplicationConfiguration(classes = BackendApplication.class)
@TestExecutionListeners([
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class
])
class UserControllerE2eTest extends Specification {

    @Autowired
    private WebApplicationContext webApplicationContext

    @Autowired
    private UserController userController

    MockMvc mockMvc

    def setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    def "Should return User by userId provided by the service as JSON"() {
        given:
        def userId = 'id1'
        def firstName = 'John'
        def lastName = 'Doe'

        when:
        def response = mockMvc.perform(get('/user/' + userId).contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.id').doesNotExist())
                .andExpect(jsonPath('$.userId', is(userId)))
                .andExpect(jsonPath('$.firstName', is(firstName)))
                .andExpect(jsonPath('$.lastName', is(lastName)))
    }

    def "Should save a new User and load it afterwards."() {
        given:
        def userId = 'newUser'
        def firstName = 'Horst'
        def lastName = 'Frehmann'

        when:
        mockMvc.perform(put('/user').content(
                """
                    {
                        "userId": "${userId}",
                        "firstName": "${firstName}",
                        "lastName": "${lastName}"
                    }
                """
        ).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted())

        and:
        def response = mockMvc.perform(get('/user/' + userId).contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.userId', is(userId)))
                .andExpect(jsonPath('$.firstName', is(firstName)))
                .andExpect(jsonPath('$.lastName', is(lastName)))
    }

    def "Should save changes to existing user"() {
        given:
        def userId = 'id1'
        def firstName = 'Jack'
        def lastName = 'Bauer'

        def responseForExistingUser = mockMvc.perform(get('/user/' + userId).contentType(MediaType.APPLICATION_JSON_UTF8))
        responseForExistingUser
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.userId', is(userId)))
                .andExpect(jsonPath('$.firstName', is("John")))
                .andExpect(jsonPath('$.lastName', is("Doe")))

        when:
        mockMvc.perform(put('/user').content(
                """
                    {
                        "userId": "${userId}",
                        "firstName": "${firstName}",
                        "lastName": "${lastName}"
                    }
                """
        ).contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isAccepted())

        and:
        def response = mockMvc.perform(get('/user/' + userId).contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.userId', is(userId)))
                .andExpect(jsonPath('$.firstName', is(firstName)))
                .andExpect(jsonPath('$.lastName', is(lastName)))
    }
}
