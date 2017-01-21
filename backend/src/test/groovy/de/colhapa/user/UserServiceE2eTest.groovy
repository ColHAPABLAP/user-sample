package de.colhapa.user

import de.colhapa.user.persistence.User
import de.colhapa.BackendApplication
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import spock.lang.Specification

@DatabaseSetup(["/users.xml"])
@SpringApplicationConfiguration(classes = BackendApplication.class)
@TestExecutionListeners([
	DependencyInjectionTestExecutionListener.class,
//	DirtiesContextTestExecutionListener.class,
//	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class
])
class UserServiceE2eTest extends Specification {
	
	@Autowired
	private UserService userService
	
	def "Should return a user for a given id"() {
		given:
			def id = 1L
			
		when:
			User user = userService.getUser(id)
			
		then:
			user.getId() == id
			user.getUserId() == 'id1'
			user.getFirstName() == 'John'
			user.getLastName() == 'Doe'
	}
	
	def "Should return a user for a given userId"() {
		given:
			def userId = 'id1'
			
		when:
			User user = userService.getUser(userId)
			
		then:
			user.getId() == 1L
			user.getUserId() == userId
			user.getFirstName() == 'John'
			user.getLastName() == 'Doe'
	}

	def "Should save new user and load it by id afterwards"() {
		given:
		def newUser = new User(userId: "newId",
				firstName: "Horst",
				lastName: "Frehmann"
		)

		when:
		User savedUser = userService.saveUser(newUser)

		then:
		savedUser == newUser

		when:
		User loadedUser = userService.getUser(savedUser.id)

		then:
		loadedUser.id == savedUser.id
		loadedUser.userId == savedUser.userId
		loadedUser.firstName == savedUser.firstName
		loadedUser.lastName == savedUser.lastName
	}

	def "Should save new user and load it by userId afterwards"() {
		given:
		def newUser = new User(userId: "gid",
				firstName: "Gordon",
				lastName: "Freeman"
		)

		when:
		User savedUser = userService.saveUser(newUser)

		then:
		savedUser == newUser

		when:
		User loadedUser = userService.getUser("gid")

		then:
		loadedUser.id == savedUser.id
		loadedUser.userId == savedUser.userId
		loadedUser.firstName == savedUser.firstName
		loadedUser.lastName == savedUser.lastName
	}
}