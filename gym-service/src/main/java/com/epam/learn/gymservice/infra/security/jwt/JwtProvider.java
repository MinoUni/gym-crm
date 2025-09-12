package com.epam.learn.gymservice.infra.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Getter
@Service
public class JwtProvider {

  private final SecretKey key;
  private final String tokenSecretKey;

  @Getter(AccessLevel.NONE)
  private final Integer tokeLifetimeInMinutes;

  public JwtProvider(
      @Value("${jwt.secret-key}") String tokenSecretKey,
      @Value("${jwt.lifetime}") Integer tokeLifetimeInMinutes) {
    Assert.notNull(tokeLifetimeInMinutes, "'accessTokeLifetime' cannot be null");
    Assert.hasLength(tokenSecretKey, "'accessTokenSecretKey' cannot be null or empty");
    this.tokenSecretKey = tokenSecretKey;
    this.tokeLifetimeInMinutes = tokeLifetimeInMinutes;
    this.key = Keys.hmacShaKeyFor(tokenSecretKey.getBytes(StandardCharsets.UTF_8));
  }

  public String generateAccessToken(Map<String, Object> claims) {
    Calendar calendar = Calendar.getInstance();
    Date now = new Date();
    calendar.setTime(now);
    calendar.add(Calendar.MINUTE, tokeLifetimeInMinutes);
    return Jwts.builder()
        .subject((String) claims.get("username"))
        .issuedAt(now)
        .expiration(calendar.getTime())
        .claims(claims)
        .signWith(key, Jwts.SIG.HS256)
        .compact();
  }
}
