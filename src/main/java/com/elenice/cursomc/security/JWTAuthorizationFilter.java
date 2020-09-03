package com.elenice.cursomc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JWTUtil jwtUtil;

	private UserDetailsService userDetailsService;

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	//Intercepta a requisicao e ve se o perfil esta autorizado, metodo padrao, executa algo antes da req continuar 
	@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) { //test pra ver se o caecalho comeca com Beare + espaco
			UsernamePasswordAuthenticationToken auth = getAuthentication(header.substring(7));  //getAuthentication - mando o valor que esta na frente do Bearer e ele vai retonrar um objeto do tipo UsernamePasswordAuthenticationToken se o token for valido, se n for, retonra null
			if (auth != null) {
				SecurityContextHolder.getContext().setAuthentication(auth); //funcao pra liberar o acesso ao filtro
			}
		}
		chain.doFilter(request, response); //avisar ao metodo doFilterInternal que pode seguir com a req
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		if (jwtUtil.tokenValido(token)) {
			String username = jwtUtil.getUsername(token); //pega o username dentro do token
			UserDetails user = userDetailsService.loadUserByUsername(username); //busca no bd
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		}
		return null;
	}
}