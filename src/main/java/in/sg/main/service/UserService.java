package in.sg.main.service;

import in.sg.main.entity.User;

public interface UserService {
	User registerUser(User user);

	User loginUser(String email, String password);
}

