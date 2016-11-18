package de.colhapa.user;

import java.util.Optional;

import de.colhapa.user.UserService;
import de.colhapa.user.persistence.User;
import java.lang.IllegalArgumentException;
import spock.lang.Specification

class UserServiceTest extends Specification {
	
	UserService userService;
	User storedUser;
	
	void setup() {
      userService = new UserService(userRepository: Mock(UserRepository))
      storedUser = new User(
	      		id: 0,
					userId: 'ColHAPA',
					firstName: 'Horst',
					lastName: 'Frehmann'
      		)
   }
	
	def "Should return a user for a given id"() {
		def userId = 'ColHAPA'
			
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.of(storedUser)
			user.id == 0L;
			user.userId == userId;
			user.firstName == 'Horst';
			user.lastName == 'Frehmann';
	}
	
	def "Should return a user for a given userId"() {
		def userId = 'ColHAPA'
			
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.of(storedUser)
			user.id == 0L;
			user.userId == userId;
			user.firstName == 'Horst';
			user.lastName == 'Frehmann';
	}
	
	def "Should return a new user if requested user does not exist"() {
		def userId = 'newUser'
				
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.ofNullable(null)
			user.id == null;
			user.userId == userId;
			user.firstName == null;
			user.lastName == null;
	}

	def "Should save an empty user with default values"() {
		def newUserId = "newId"
		def newUser = new User(newUserId)
				
		when:
			User savedUser = userService.saveUser(newUser)
			
		then:
			1 * userService.userRepository.save(newUser) >> newUser
			savedUser.userId == newUserId
			savedUser.firstName == ""
			savedUser.lastName == ""
	}
	
	def "Should not save a user without userId"() {
		def newUser = new User()
				
		when:
			User user = userService.saveUser(newUser)
			
		then:
			newUser.userId == null
			0 * userService.userRepository.save(newUser)
			thrown IllegalArgumentException
	}
}
