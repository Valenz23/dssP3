package dss.pvalenz23.practica1.configuracion;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class ConfigSeguridad {
    
    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorizeRequests -> {
                authorizeRequests
                    .requestMatchers("/", "/productos", "/productos/**", "/carrito", "/carrito/**").hasRole("USER")
                    .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")
                    .requestMatchers("/h2-console/**").permitAll()  // Permitir acceso a la consola H2
                    .anyRequest().authenticated();
            })
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) 
            .headers(headers -> headers.frameOptions().sameOrigin()); 
    
        return http.build();
    }

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {

        UserDetails admin = User.withUsername("admin")
			.password(passwordEncoder().encode("admin"))
			.roles("ADMIN")
			.build();

		UserDetails user = User.withUsername("user")
            .password(passwordEncoder().encode("user"))
            .roles("USER")
            .build();

		return new InMemoryUserDetailsManager(admin, user);
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                // Obtenemos los roles del usuario
                Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
                
                // Redirigimos dependiendo del rol
                for (GrantedAuthority role : roles) {
                    String roleName = role.getAuthority();
                    if (roleName.equals("ROLE_ADMIN")) {
                        response.sendRedirect("/admin");  // Redirige a la página de admin
                        return; // Salimos de la función
                    } else if (roleName.equals("ROLE_USER")) {
                        response.sendRedirect("/productos");  // Redirige a la página de usuario
                        return; // Salimos de la función
                    }
                }
                
                // Redirige a una página por defecto si no hay roles válidos
                response.sendRedirect("/");
            }
        };
    }

}
