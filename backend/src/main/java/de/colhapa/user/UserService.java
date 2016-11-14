package de.colhapa.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.colhapa.user.persistence.User;

@Component
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	public User getUser(Long id) {
		User user = userRepository.findOne(id);
		if(user == null) {
			user = new User();
		}
		return user;
	}

	public User getUser(String userId) {
		Optional<User> userOptional = userRepository.findOneByUserId(userId);
		User user = userOptional.orElse(new User(userId));
		
		return user;
	}
}
