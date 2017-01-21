package de.colhapa.user

import de.colhapa.user.persistence.User
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class UserControllerTest extends Specification {

    UserController userController

    MockMvc mockMvc

    void setup() {
        userController = new UserController(userService: Mock(UserService))
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build()
    }

    def "Should return User by userId provided by the service as JSON"() {
        given:
        def userId = 'userId1'
        def firstName = 'John'
        def lastName = 'Doe'

        when:
        def response = mockMvc.perform(get('/user/' + userId).contentType(MediaType.APPLICATION_JSON_UTF8))

        then:
        1 * userController.userService.getUser(userId) >> {
            new User(id: 0,
                    userId: userId,
                    firstName: "John",
                    lastName: "Doe"
            )
        }

        response
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath('$.id').doesNotExist())
                .andExpect(jsonPath('$.userId', is(userId)))
                .andExpect(jsonPath('$.firstName', is(firstName)))
                .andExpect(jsonPath('$.lastName', is(lastName)))
    }

    def "Should save User"() {
        given:
        def userId = 'userId1'
        def firstName = 'John'
        def lastName = 'Doe'

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

        then:
        // TODO how to check the actual passed values?
        1 * userController.userService.saveUser(_)
    }
}