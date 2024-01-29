package codingtechniques.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
	
	
	@GetMapping("/home") 
	public String home () {
		return "home";
	}

}
