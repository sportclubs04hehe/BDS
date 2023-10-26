package HTQ.BDS.components;

import HTQ.BDS.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private long expiration;
    @Value("${jwt.secret-key}")
    private String secretKey;


    /**
     * Phương pháp này tạo ra một JWT (JSON Web Token) để cho User.
     *
     * @param user The user for whom the token will be generated.
     * @return JWT mã thông báo dưới dạng chuỗi hoặc null nếu có lỗi.
     */
    public String generateToken(User user){
        // Tạo Map<> để chứa các xác nhận quyền sở hữu tùy chỉnh cho JWT.
        // Claims là những mẩu thông tin được mã hóa vào token.
        Map<String,Object> claims = new HashMap<>();
        claims.put("usernames", user.getUsernames());

        try {
            // Xây dựng JWT bằng Jwts builder.
            return Jwts.builder()
                    .setClaims(claims) // set username
                    .setSubject(user.getUsernames())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))  // time now + 1000L chuyển đổi thành mili giây
                    .signWith(getSignInKey(), HS256)
                    .compact();
        }catch (Exception e){
            System.out.println("Cannot create jwt token, error: " + e.getMessage());
            return null;
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey); // giải mã thành định dạng base 64
        return Keys.hmacShaKeyFor(bytes);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token){
        Date expirationDate = extractClaims(token,Claims::getExpiration);
        return expirationDate.before(new Date()); // check
    }

    public String extractUsername(String token){
        return extractClaims(token,Claims::getSubject);
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }
}
