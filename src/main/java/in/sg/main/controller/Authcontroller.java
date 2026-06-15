package in.sg.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.sg.main.entity.User;
import in.sg.main.service.UserService;
import jakarta.servlet.http.HttpSession;

@Controller
public class Authcontroller {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}

	@GetMapping("/register")
	public String registerPage() {
		return "register";
	}

	@PostMapping("/register")
	public String registerUser(@ModelAttribute User user) {
		userService.registerUser(user);
		return "redirect:/menu";
	}

	@PostMapping("/login")
	public String loginUser(@RequestParam String email, @RequestParam String password, HttpSession session,
			Model model) {
		User user = userService.loginUser(email, password);
		if (user != null) {
			session.setAttribute("user", user);
			session.setAttribute("userId", user.getId());
			return "redirect:/menu";
		}
		model.addAttribute("error", "Invalid Credentials");
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

}
