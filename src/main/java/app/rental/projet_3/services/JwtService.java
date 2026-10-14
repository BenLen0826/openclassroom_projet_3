package app.rental.projet_3.services;

import java.time.Duration;
import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private static final Duration TOKEN_DURATION = Duration.ofHours(24);

	private final JwtEncoder jwtEncoder;

	public JwtService(JwtEncoder jwtEncoder) {
		this.jwtEncoder = jwtEncoder;
	}

	public String generateToken(String email) {
		Instant now = Instant.now();

		JwtClaimsSet claims = JwtClaimsSet.builder()
				.subject(email)
				.issuedAt(now)
				.expiresAt(now.plus(TOKEN_DURATION))
				.build();

		// Obligatoire : sans en-tete, l'encodeur utiliserait RS256 et echouerait avec notre cle HS256
		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}
}
