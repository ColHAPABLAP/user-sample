package de.colhapa.user;

import de.colhapa.user.persistence.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET, path = "/{userId}")
	public User getUser(@PathVariable("userId") String userId) {
		LOGGER.info("Calling getUser()");

		return userService.getUser(userId);
	}

	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity saveUser(@RequestBody User user) {
		userService.saveUser(user);

		return new ResponseEntity<String>(HttpStatus.ACCEPTED);
	}
}
