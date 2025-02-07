package com.matheusvsdev.product_notifications.config;

import com.matheusvsdev.product_notifications.config.customgrant.CustomPasswordAuthenticationConverter;
import com.matheusvsdev.product_notifications.config.customgrant.CustomPasswordAuthenticationProvider;
import com.matheusvsdev.product_notifications.config.customgrant.CustomUserAuthorities;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Configuration
public class AuthorizationServerConfig {

    // Propriedades injetadas a partir de um arquivo de configuração
    @Value("${security.client-id}")
    private String clientId;

    @Value("${security.client-secret}")
    private String clientSecret;

    @Value("${security.jwt.duration}")
    private Integer jwtDurationSeconds;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    // Configuração da cadeia de filtros de segurança para o servidor de autorização
    @Bean
    @Order(2) // Define a ordem do filtro na cadeia de segurança
    public SecurityFilterChain asSecurityFilterChain(HttpSecurity http) throws Exception {

        // Configura o endpoint do token
        // @formatter:off
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                        .accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
                        .authenticationProvider(new CustomPasswordAuthenticationProvider(authorizationService(), tokenGenerator(), userDetailsService, passwordEncoder)));

        // Configuração do servidor de recursos que usa JWT
        http.oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(Customizer.withDefaults()));

        // Permite configurações CORS
        http.cors(Customizer.withDefaults());
        // @formatter:on

        return http.build();
    }

    // Serviço de autorização em memória
    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    // Serviço de consentimento de autorização em memória
    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    // Repositório de clientes registrados em memória
    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        // Configuração de um cliente registrado com suas credenciais e permissões
        // @formatter:off
        RegisteredClient registeredClient = RegisteredClient
                .withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .clientSecret(passwordEncoder.encode(clientSecret))
                .scope("read")
                .scope("write")
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .tokenSettings(tokenSettings())
                .clientSettings(clientSettings())
                .build();
        // @formatter:on

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    // Configuração das definições do token
    @Bean
    public TokenSettings tokenSettings() {
        // @formatter:off
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofSeconds(jwtDurationSeconds))
                .build();
        // @formatter:on
    }

    // Configurações do cliente
    @Bean
    public ClientSettings clientSettings() {
        return ClientSettings.builder().build();
    }

    // Configurações do servidor de autorização
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    // Gerador de tokens que usa JWT
    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
        NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(tokenCustomizer());
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator);
    }

    // Personalização dos tokens JWT gerados
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            OAuth2ClientAuthenticationToken principal = context.getPrincipal();
            CustomUserAuthorities user = (CustomUserAuthorities) principal.getDetails();
            List<String> authorities = user.getAuthorities().stream().map(x -> x.getAuthority()).toList();
            if (context.getTokenType().getValue().equals("access_token")) {
                // @formatter:off
                context.getClaims()
                        .claim("authorities", authorities) // Adiciona as autoridades ao token
                        .claim("username", user.getUsername()); // Adiciona o nome de usuário ao token
                // @formatter:on
            }
        };
    }

    // Decodificador JWT
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // Fonte de chaves JWK
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa(); // Gera uma nova chave RSA
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    // Geração de chave RSA
    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }

    // Método auxiliar para gerar um par de chaves RSA
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048); // Tamanho da chave
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
