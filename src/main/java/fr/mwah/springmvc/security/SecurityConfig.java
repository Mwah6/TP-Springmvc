package fr.mwah.springmvc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private DataSource dataSource;
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		PasswordEncoder passwordEncoder = passwordEncoder();

		// ->permet d'indiquer où se trouve les utilisateurs dans la BdD
		// {noop}->indique de ne pas encoder le mot de passe (password("{noop}1234"))
		// Il utilise MD5 par défaut, mais il est déprécié (le hash utilisé n'est pas assez puissant)
		// Au niveau de la mémoire de l'application, c'est le hash du mdp qui est stocké
		System.out.println("****************");
		System.out.println(passwordEncoder.encode("1234"));
		System.out.println("****************");
		//pour Spring Security, il faut déclarer username comme "principal" et mot de passe comme "credentials" et active comme "active"
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select username as principal, password as credentials, active from users where username=?")
		.authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username=?")
		.passwordEncoder(passwordEncoder)
		.rolePrefix("ROLE_");
		
//		=> inMemoryAuthentication		
//		auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder.encode("1234")).roles("USER");
//		auth.inMemoryAuthentication().withUser("user2").password(passwordEncoder.encode("1234")).roles("USER");
//		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder.encode("1234")).roles("USER", "ADMIN");
// Ce n'est pas toptop mais ça fait le taff pour configurer une petite appli (users et admin fixes)
//		super.configure(auth); -> Uilise par défaut "user" et un mdp généré par Spring
	}

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
//		super.configure(http);
//		http.httpBasic(); // -> Modèle d'authentification basic, basé sur le protocole http
		// le serveur renvoie dans le header que l'appli néécessite une authentification
		// Le browser affiche alors une fenêtre flottante JS
		http.formLogin().loginPage("/login");
		http.formLogin(); //-> Renvoie vers la page d'authentification de base (formulaire html)
//		http.formLogin().loginPage("/login"); //->indique la procédure pour s'authentifier
//		**-> Expression régulière de Spring
		http.authorizeRequests().antMatchers("/login**/**", "/webjars/**").permitAll();
		http.authorizeRequests().antMatchers("/save**/**","/delete**/**", "/form**/**").hasRole("ADMIN");
		http.authorizeRequests().antMatchers("/patients**/**").hasRole("USER");
		http.authorizeRequests().anyRequest().authenticated();//->Indique que toute action nécessite une identification
		//Empeche même l'accès aux ressources statiques (Bootstrap...)
//		http.csrf().disable() //->Désactive le synchronized tocken qui prévient de la faille CSRF
//		http.csrf() //->Active le synchronized tocken
		//par défaut, le synchronized tocken est activé.
		// Il apparait dans le fichier html généré en tant que "hidden" dans le formulaire
		http.exceptionHandling().accessDeniedPage("/notAuthorized");
	}
// Le même objet encodeur va être utiliser partout dans l'application
	@Bean //L'objet retourner avec cette annotation sera dans le contexte application
	// cela permet de l'injecter dans les autres composants de l'application
	// Il suffira alors de déclarer un objet PasswordEncoder et d'uiliser l'annotation @Autowired
	public PasswordEncoder passwordEncoder() {
//		return new MD5(); //-> N'existe plus depuis Spring 5
		return new BCryptPasswordEncoder();
		
	}
}
