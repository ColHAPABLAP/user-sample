package de.colhapa.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
	
	public User saveUser(User user) {
		if(StringUtils.isEmpty(user.getUserId())) {
			throw new IllegalArgumentException("UserId has to be specified.");
		}

		Optional<User> storedUser = userRepository.findOneByUserId(user.getUserId());
		if (storedUser.isPresent()) {
			user.setId(storedUser.get().getId());
		}

		return userRepository.save(user);
	}
}
