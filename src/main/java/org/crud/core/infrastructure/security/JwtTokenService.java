package org.crud.core.infrastructure.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.port.TokenService;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class JwtTokenService implements TokenService {

    private PrivateKey privateKey;

    @PostConstruct
    void init() {
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream is = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("jwt-keystore.p12")) {
                keyStore.load(is, "password".toCharArray());
            }
            privateKey = (PrivateKey) keyStore.getKey("jwt", "password".toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar la llave privada JWT", e);
        }
    }

    @Override
    public String generarToken(Empleado empleado) {
        Set<String> groups = new HashSet<>();
        groups.add(empleado.getRol() != null ? empleado.getRol() : "USER");

        return Jwt.issuer("crud-quarkus")
                .subject(empleado.getUsername())
                .claim("id", empleado.getId())
                .groups(groups)
                .expiresIn(Duration.ofHours(24))
                .sign(privateKey);
    }
}
