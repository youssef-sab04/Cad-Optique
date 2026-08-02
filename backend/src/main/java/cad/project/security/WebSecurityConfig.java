package cad.project.security;

import cad.project.model.AppRole;
import cad.project.model.Role;
import cad.project.model.User;
import cad.project.repositries.RoleRepository;
import cad.project.repositries.UserRepositry;
import cad.project.security.jwt.AuthEntryPointJwt;
import cad.project.security.jwt.AuthTokenFilter;
import cad.project.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;
import java.util.Set;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepositry userRepositry;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    // Configure le mécanisme d'authentification (charger user + vérifier password)
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                //.requestMatchers("/api/admin/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers(headers -> headers.frameOptions(
                frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"));
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepositry userRepositry, PasswordEncoder passwordEncoder) {
        return args -> {
            // Retrieve or create roles
            Role responsableRole = roleRepository.findByRoleName(AppRole.ROLE_RESPONSABLE)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(AppRole.ROLE_RESPONSABLE);
                        return roleRepository.save(newRole);
                    });

            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setRoleName(AppRole.ROLE_ADMIN);
                        return roleRepository.save(newRole);
                    });

            Set<Role> responsableRoles = Set.of(responsableRole);
            Set<Role> adminRoles = Set.of( adminRole);

            // Create users if not already present
            if (!userRepositry.existsByUserName("responsable1")) {
                User responsable = new User();
                responsable.setUserName("responsable1");
                responsable.setNom("Responsable");
                responsable.setPrenom("Magasin");
                responsable.setEmail("responsable1@cadoptique.com");
                responsable.setCin("RESP0001");
                responsable.setPassword(passwordEncoder.encode("password1"));
                responsable.setUserRoles(responsableRoles);
                userRepositry.save(responsable);
            }

            if (!userRepositry.existsByUserName("admin")) {
                User admin = new User();
                admin.setUserName("admin");
                admin.setNom("Admin");
                admin.setPrenom("Cad-Optique");
                admin.setEmail("admin@cadoptique.com");
                admin.setCin("ADMIN0001");
                admin.setPassword(passwordEncoder.encode("adminPass"));
                admin.setUserRoles(adminRoles);
                userRepositry.save(admin);
            }
        };
    }

}
