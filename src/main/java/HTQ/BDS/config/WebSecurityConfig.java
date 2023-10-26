package HTQ.BDS.config;

import HTQ.BDS.entity.Roles;
import HTQ.BDS.filters.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.http.HttpMethod.*;

@Configuration
//@EnableMethodSecurity
@EnableWebSecurity
@EnableWebMvc
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;
    @Bean
    //Pair.of(String.format("%s/products", apiPrefix), "GET"),
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> {
                    requests
                            .requestMatchers(
                                    String.format("%s/user/register", apiPrefix),
                                    String.format("%s/user/login", apiPrefix)
                            )
                            .permitAll()

                            .requestMatchers(GET, String.format("%s/admin", apiPrefix)).hasAnyRole(Roles.ADMIN)

                            .requestMatchers(GET,String.format("%s/roles", apiPrefix)).hasAnyRole(Roles.ADMIN)

                            .anyRequest().authenticated();

                })

        ;
        return http.build();
    }
}