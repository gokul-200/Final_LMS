//package com.example.lms.security;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//
//    // ✅ Must be at least 256 bits (32+ characters) for HS512
//    private final String SECRET = "MySuperSecretKeyForJWTGenerationThatIsVeryLong12345";
//    private final long EXPIRATION = 86400000; // 1 day
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(SECRET.getBytes());
//    }
//
//    public String generateToken(String username, String role) {
//        return Jwts.builder()
//                .setSubject(username)
//                .claim("role", role)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .compact();
//    }
//
//    public String extractUsername(String token) {
//        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
//                .parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public String extractRole(String token) {
//        return (String) Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
//                .parseClaimsJws(token).getBody().get("role");
//    }
//
//    public boolean validateToken(String token) {
//        try {
//            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
