package de.colhapa.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.colhapa.user.persistence.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	
	List<User> findAllByFirstName(String firstName);
	
	Optional<User> findOneByUserId(String userId);

}
