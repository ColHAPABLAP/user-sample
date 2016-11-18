package de.colhapa.user;

import de.colhapa.user.persistence.User
import de.colhapa.BackendApplication
import com.github.springtestdbunit.DbUnitTestExecutionListener
import com.github.springtestdbunit.annotation.DatabaseSetup
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener
import org.springframework.test.context.support.DirtiesContextTestExecutionListener
import org.springframework.test.context.transaction.TransactionalTestExecutionListener
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification;

@DatabaseSetup(["/users.xml"])
@SpringApplicationConfiguration(classes = BackendApplication.class)
@TestExecutionListeners([
	DependencyInjectionTestExecutionListener.class,
//	DirtiesContextTestExecutionListener.class,
//	TransactionalTestExecutionListener.class,
	DbUnitTestExecutionListener.class
])
class UserRepositoryTest extends Specification {
	
	@Autowired
	UserRepository userRepository;
	
	def "Should contain users"() {
		
		when:
			def numUsers = userRepository.count();
			
		then:
			numUsers == 3;
	}
	
	def "Should find user by userId"() {
		given:
			def userId = "id1"
		
		when:
			Optional<User> userOptional = userRepository.findOneByUserId(userId);
			User user = userOptional.orElse(null)
			
		then:
			user.getUserId() == userId;
			user.getFirstName() == "John"
			user.getLastName() == "Doe"
	}
	
	def "Should find users by firstName"() {
		given:
			def firstName = "John"
		
		when:
			List<User> users = userRepository.findAllByFirstName(firstName);
		
		then:
			users.size() == 2;
//			users.
//			user.getUserId() == userId;
//			user.getFirstName() == "John"
//			user.getLastName() == "Doe"
	}

	def "Should not save multiple users with the same userId"() {
		given:
			def existingUserId = "id1"
			def newUser = new User(existingUserId)
		
		when:
			userRepository.save(newUser)
			
		then:
			thrown DataIntegrityViolationException
	}
}