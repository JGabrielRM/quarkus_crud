package org.crud.core.infrastructure.security;

import jakarta.enterprise.context.ApplicationScoped;
import org.crud.core.domain.port.PasswordService;
import org.wildfly.security.password.PasswordFactory;
import org.wildfly.security.password.interfaces.BCryptPassword;
import org.wildfly.security.password.util.ModularCrypt;
import org.wildfly.security.password.spec.EncryptablePasswordSpec;

@ApplicationScoped
public class BcryptPasswordService implements PasswordService {

    @Override
    public String hashear(String password) {
        try {
            PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);
            EncryptablePasswordSpec spec = new EncryptablePasswordSpec(
                    password.toCharArray(), null);
            BCryptPassword bcrypt = (BCryptPassword) factory.generatePassword(spec);
            return ModularCrypt.encodeAsString(bcrypt);
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear password", e);
        }
    }

    @Override
    public boolean verificar(String passwordPlano, String passwordHasheado) {
        try {
            PasswordFactory factory = PasswordFactory.getInstance(BCryptPassword.ALGORITHM_BCRYPT);
            BCryptPassword decoded = (BCryptPassword) factory.translate(
                    ModularCrypt.decode(passwordHasheado));
            return factory.verify(decoded, passwordPlano.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar password", e);
        }
    }
}
