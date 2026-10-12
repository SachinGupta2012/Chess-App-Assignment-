package chess.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig{
    @Value("${FRONTEND_URL:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public InMemoryUserDetailsManager users(){
        UserDetails white=User.withUsername("white")
            .password("{noop}white123")
            .roles("USER")
            .build();

        UserDetails black=User.withUsername("black")
            .password("{noop}black123")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(white,black);
    }

    @Bean
    public SecurityFilterChain security(HttpSecurity http) throws Exception{
        http.csrf(c->c.disable());
        http.cors(c->c.configurationSource(cors()));
        http.authorizeHttpRequests(a->a
            .requestMatchers("/ws/**").permitAll()
            .anyRequest().authenticated()
        );
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource cors(){
        CorsConfiguration c=new CorsConfiguration();
        c.setAllowedOrigins(List.of(frontendUrl));
        c.setAllowedMethods(List.of("GET","POST","OPTIONS"));
        c.setAllowedHeaders(List.of("*"));
        c.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",c);
        return source;
    }
}
