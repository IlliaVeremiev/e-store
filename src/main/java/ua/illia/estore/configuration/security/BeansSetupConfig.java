package ua.illia.estore.configuration.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigInteger;

@Configuration
public class BeansSetupConfig {

    @Value("${security.encoder.password.strength:-1}")
    private int passwordEncoderStrength;

    @Value("${security.encryptor.text.password}")
    private String securityTokenTextEncryptorPassword;

    @Value("${security.encryptor.text.salt}")
    private String securityTokenTextEncryptorSalt;

    @Value("${security.encryptor.bytes.password}")
    private String securityTokenBytesEncryptorPassword;

    @Value("${security.encryptor.bytes.salt}")
    private String securityTokenBytesEncryptorSalt;

    @Value("${security.encoder.token-hash.strength:-1}")
    private int passwordTokenHashStrength;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(passwordEncoderStrength);
    }

    @Bean
    public TextEncryptor securityTokenTextEncryptor() {
        return Encryptors.text(securityTokenTextEncryptorPassword, new BigInteger(securityTokenTextEncryptorSalt).toString(16));
    }

    @Bean
    public BytesEncryptor securityTokenBytesEncryptor() {
        return Encryptors.stronger(securityTokenBytesEncryptorPassword, new BigInteger(securityTokenBytesEncryptorSalt).toString(16));
    }

    @Bean
    public PasswordEncoder publicTokenHashEncoder() {
        return new BCryptPasswordEncoder(passwordTokenHashStrength);
    }
}
