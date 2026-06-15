package in.sg.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.sg.main.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}