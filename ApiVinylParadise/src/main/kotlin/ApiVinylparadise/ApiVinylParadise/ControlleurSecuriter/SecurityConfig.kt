package ApiVinylparadise.ApiVinylParadise.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable() 
            .authorizeHttpRequests { auth ->
                auth

                .requestMatchers("/swagger-ui.html", "/api-docs/**", "/swagger-ui/**").permitAll()
               
                .requestMatchers("/vinyls/**").permitAll()
                    .requestMatchers("/admin/**").hasAuthority("delete:any_account") 
                    .requestMatchers("/swagger-ui.html", "/api-docs/**", "/swagger-ui/**").permitAll()
                    .requestMatchers("/users/**").hasAnyAuthority("read:users", "update:own_profile")
                    .requestMatchers("/vinyls/**").hasAnyAuthority("read:vinyls", "update:own_vinyls")
                    
                    .anyRequest().authenticated() 
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt()
            }
        return http.build()
    }

    @Bean
    fun jwtDecoder(): JwtDecoder {
        
        return NimbusJwtDecoder.withJwkSetUri("https://dev-a57qm4dynql006nr.ca.auth0.com/.well-known/jwks.json").build()
    }
}
