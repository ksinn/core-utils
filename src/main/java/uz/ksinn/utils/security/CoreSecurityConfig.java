package uz.ksinn.utils.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanIsAbstractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import uz.ksinn.utils.security.jwt.JwtAuthenticationProvider;
import uz.ksinn.utils.security.propertis.JwtUsingProperties;

import java.util.List;


public class CoreSecurityConfig extends WebSecurityConfigurerAdapter {

    private RestAccessDeniedHandler accessDeniedHandler;
    private CoreAuthenticationEntryPoint authenticationEntryPoint;
    private AuthenticationProvider authenticationProvider;
    private CoreTokenAuthenticationFilter tokenAuthenticationFilter;
    private ErrorResponseWriter errorResponseWriter;
    private ObjectMapper objectMapper;
    private CorsConfigurationSource corsConfigurationSource;
    private JwtUsingProperties jwtUsingProperties;

    private String serviceName;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureLogout(http);
        http
                .cors(corsCustomizer())
                .csrf().disable()
                .addFilterBefore(getJWTTokenAuthenticationFilter(), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(getAuthenticationEntryPoint())
                .accessDeniedHandler(getAccessDeniedHandler());
    }

    private void configureLogout(HttpSecurity http) throws Exception {
        http.logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .permitAll();
    }

    @Autowired(required = false)
    public void setServiceName(@Value("${ksinn.service.name}") String serviceName) {
        this.serviceName = serviceName;
    }

    @Autowired(required = false)
    public void setAccessDeniedHandler(RestAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Autowired(required = false)
    public void setAuthenticationEntryPoint(CoreAuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Autowired(required = false)
    public void setAuthenticationProvider(JwtAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Autowired(required = false)
    public void setJWTTokenAuthenticationFilter(CoreTokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Autowired(required = false)
    public void setErrorResponseWriter(ErrorResponseWriter errorResponseWriter) {
        this.errorResponseWriter = errorResponseWriter;
    }

    @Autowired(required = false)
    public void setCorsConfigurationSource(CorsConfigurationSource corsConfigurationSource) {
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Autowired(required = false)
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired(required = false)
    public void setJwtUsingProperties(@Value("${ksinn.security.jwt.key}") String key) {
        this.jwtUsingProperties = new JwtUsingProperties(key);
    }

    public AccessDeniedHandler getAccessDeniedHandler() {
        if (this.accessDeniedHandler == null) {
            this.accessDeniedHandler = new RestAccessDeniedHandler(getErrorResponseWriter());
        }
        return this.accessDeniedHandler;
    }

    public AuthenticationEntryPoint getAuthenticationEntryPoint() {
        if (this.authenticationEntryPoint == null) {
            this.authenticationEntryPoint = new CoreAuthenticationEntryPoint(getErrorResponseWriter());
        }
        return this.authenticationEntryPoint;
    }

    public AuthenticationProvider getAuthenticationProvider() {
        if (this.authenticationProvider == null) {
            this.authenticationProvider = new JwtAuthenticationProvider(getJwtUsingProperties().getKey());
        }
        return this.authenticationProvider;
    }

    public CoreTokenAuthenticationFilter getJWTTokenAuthenticationFilter() throws Exception {
        if (this.tokenAuthenticationFilter == null) {
            this.tokenAuthenticationFilter = new CoreTokenAuthenticationFilter(getErrorResponseWriter(), super.authenticationManager());
        }
        return this.tokenAuthenticationFilter;
    }

    public ErrorResponseWriter getErrorResponseWriter() {
        if (this.errorResponseWriter == null) {
            this.errorResponseWriter = new CoreErrorResponseWriter(getObjectMapper(), serviceName != null ? "SRV_" + serviceName.toUpperCase() + "__" : "");
        }
        return errorResponseWriter;
    }

    public ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public JwtUsingProperties getJwtUsingProperties() {
        if (this.jwtUsingProperties == null) {
            throw new BeanIsAbstractException(JwtUsingProperties.class.getCanonicalName());
        }
        return jwtUsingProperties;
    }

    //    @Bean
    public CorsConfigurationSource getCorsConfigurationSource() {
        if (this.corsConfigurationSource == null) {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setAllowedHeaders(List.of("Content-Type", "Accept", "X-Requested-With", "remember-me", "Authorization"));
            configuration.setAllowedMethods(List.of("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            configuration.setAllowedOrigins(List.of("*"));
            configuration.setAllowCredentials(true);
            configuration.setMaxAge(3600L);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);

            return source;
        } else {
            return this.corsConfigurationSource;
        }
    }

    public Customizer<CorsConfigurer<HttpSecurity>> corsCustomizer() {
        return t -> {
            t.configurationSource(getCorsConfigurationSource());
        };
    }

}
