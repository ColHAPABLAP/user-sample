package de.colhapa.user

import de.colhapa.user.persistence.User
import spock.lang.Specification

class UserServiceTest extends Specification {
	
	UserService userService
	User storedUser
	
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
			user.id == 0L
			user.userId == userId
			user.firstName == 'Horst'
			user.lastName == 'Frehmann'
	}
	
	def "Should return a user for a given userId"() {
		def userId = 'ColHAPA'
			
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.of(storedUser)
			user.id == 0L
			user.userId == userId
			user.firstName == 'Horst'
			user.lastName == 'Frehmann'
	}
	
	def "Should return a new user if requested user does not exist"() {
		def userId = 'newUser'
				
		when:
			User user = userService.getUser(userId)
			
		then:
			1 * userService.userRepository.findOneByUserId(userId) >> Optional.ofNullable(null)
			user.id == null
			user.userId == userId
			user.firstName == null
			user.lastName == null
	}

	def "Should save an empty user with default values"() {
		def newUserId = "newId"
		def newUser = new User(newUserId)
				
		when:
			User savedUser = userService.saveUser(newUser)
			
		then:
			1 * userService.userRepository.findOneByUserId(newUserId) >> Optional.ofNullable(null)
			1 * userService.userRepository.save(newUser) >> newUser
			savedUser.userId == newUserId
	}
	
	def "Should not save a user without userId"() {
		def newUser = new User()
				
		when:
			userService.saveUser(newUser)
			
		then:
			newUser.userId == null
			0 * userService.userRepository.save(newUser)
			thrown IllegalArgumentException
	}

	def "Should update an existing user"() {
		def userId = "userId"
		def existingId = 0L
		def modifiedUser = new User(userId: userId,
				firstName: "Jack",
				lastName: "Bauer"
		)
		def existingUser = new User(id: existingId,
				userId: userId,
				firstName: "John",
				lastName: "Doe"
		)

		when:
		User savedUser = userService.saveUser(modifiedUser)

		then:
		1 * userService.userRepository.findOneByUserId(userId) >> Optional.ofNullable(existingUser)
		1 * userService.userRepository.save(modifiedUser) >> modifiedUser
		savedUser.id == existingId
		savedUser.userId == userId
		savedUser.firstName == "Jack"
		savedUser.lastName == "Bauer"
	}
}
