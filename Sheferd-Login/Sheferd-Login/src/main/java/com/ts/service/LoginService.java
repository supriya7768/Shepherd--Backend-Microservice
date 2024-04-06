package com.ts.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ts.repository.LoginRepository;
import com.ts.model.Login;
import com.ts.model.OurUsers;

@Service
public class LoginService {
	
	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private OurUsersService ourUsersService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Login saveLogin(Login login) {
		return loginRepository.save(login);
	}

	public List<Login> findAllUsers() {
		return loginRepository.findAll();
	}

	public Login findLoginByEmail(String email) {
		return loginRepository.findByEmail(email);
	}

	public void updateLoginDetails(Login login) {
		loginRepository.save(login);
	}

	public Login findLoginByEmailAndPassword(String email, String password) {
		return loginRepository.findByEmailAndPassword(email, password);
	}

	@Transactional
	public void updatePasswordInOurUsers(String email, String encryptedPassword) {
		OurUsers user = ourUsersService.findUserByEmail(email);
		if (user != null) {
			user.setPassword(encryptedPassword);
			ourUsersService.updateUserDetails(user); // Update password in OurUsers table
		}
	}

	public ResponseEntity<String> loginByEmail(Login loginRequest) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Login existingLogin = loginRepository.findByEmail(email);
		if (existingLogin == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found");
		}

		// Decode the hashed password stored in the database
		String storedPassword = existingLogin.getPassword();
		boolean passwordMatches = passwordEncoder.matches(password, storedPassword);

		if (!passwordMatches) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password");
		}

		return ResponseEntity.ok("Logged in successfully");
	}

}
