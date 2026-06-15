package in.sg.main.service_impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.sg.main.entity.User;
import in.sg.main.repository.UserRepository;
import in.sg.main.service.UserService;

@Service
public class UserService_impl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public User registerUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public User loginUser(String email, String password) {
		User user = userRepository.findByEmail(email);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		}
		return null;
	}
}