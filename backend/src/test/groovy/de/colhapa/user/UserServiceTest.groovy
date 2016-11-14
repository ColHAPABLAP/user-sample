package de.colhapa.user;

import java.util.Optional;

import de.colhapa.user.UserService;
import de.colhapa.user.persistence.User;
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
			user.getId() == 0L;
			user.getUserId() == userId;
			user.getFirstName() == 'Horst';
			user.getLastName() == 'Frehmann';
	}
	
	def "Should return a user for a given userId"() {
		def userId = 'ColHAPA'
			
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.of(storedUser)
			user.getId() == 0L;
			user.getUserId() == userId;
			user.getFirstName() == 'Horst';
			user.getLastName() == 'Frehmann';
	}
	
	def "Should return a new user if requested user does not exist"() {
		def userId = 'newUser'
				
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.ofNullable(null)
			user.getId() == null;
			user.getUserId() == userId;
			user.getFirstName() == null;
			user.getLastName() == null;
	}

	def "Should save an empty user with default values"() {
		def userId = 'newUser'
				
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.ofNullable(null)
			user.getId() == null;
			user.getUserId() == userId;
			user.getFirstName() == null;
			user.getLastName() == null;
	}
	
	def "Should not save a user without userId"() {
		def newUser = new User();
				
		when:
			User user = userService.saveUser(newUser)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.ofNullable(null)
//			user.getId() == null;
//			user.getUserId() == userId;
//			user.getFirstName() == null;
//			user.getLastName() == null;
	}
}
