package com.backend.pnta.Configuration;

import com.backend.pnta.Models.User.UserP;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    //this class manages the token, for example, extracting data from it

    private static final String SECRET_KEY = "3884F25D3E2D12DC73AA38B1F531E3884F25D3E2D12DC73AA38B1F531E3884F25D3E2D12DC73AA38B1F531E";
    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims,T> claimsTResolver){
        final Claims claims = extractAllClaims(token);
        return claimsTResolver.apply(claims);
    }
    public String generateToken(Map<String,Object> extraClaims, UserDetails userDetails){
        extraClaims.put("userId", getUserIdFromUserDetails(userDetails));
        extraClaims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24 * 7))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Long getUserIdFromUserDetails(UserDetails userDetails) {
        // Assuming you have a method to retrieve the user ID from the UserDetails object
        // For example, if your UserDetails object implements UserDetails interface, you can cast it
        // to your custom UserP class and retrieve the user ID from there
        if (userDetails instanceof UserP) {
            return ((UserP) userDetails).getUserId();
        } else {
            // Handle the case where UserDetails does not contain the user ID
            throw new IllegalArgumentException("UserDetails does not contain user ID");
        }
    }
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()))&& !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().
                setSigningKey(getSignInKey()).build().
                parseClaimsJws(token).getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
