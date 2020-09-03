package com.elenice.cursomc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.elenice.cursomc.security.UserSS;

public class UserService {

	//mostra o usuario autenticado
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
}