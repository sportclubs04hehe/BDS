package HTQ.BDS.filters;

import HTQ.BDS.components.JwtTokenUtils;
import HTQ.BDS.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService detailsService;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
       try{
           if (isBypassToken(request)) {
               filterChain.doFilter(request, response); // enable bypass
               return;
           }

           final String authHeader = request.getHeader("Authorization");
           if(authHeader == null && !authHeader.startsWith("Bearer ")){
               response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
               return;
           }
               final String token = authHeader.substring(7);
               final String username = jwtTokenUtils.extractUsername(token);

               if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                   User user = (User) detailsService.loadUserByUsername(username);
                   log.info("logger = {}",user.getAuthorities().toString());
                   if (jwtTokenUtils.validateToken(token, user)) {
                       UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                               user,
                               null,
                               user.getAuthorities()
                       );
                       authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                       SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                   }
               }
           filterChain.doFilter(request, response);

    }catch (Exception e){
           response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"UNAUTHORIZED");
       }
    }

    private boolean isBypassToken(@NonNull HttpServletRequest request) {
        final List<Pair<String, String>> byPassTokens = Arrays.asList(
                Pair.of(String.format("%s/user/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/user/login", apiPrefix), "POST"),
                Pair.of(String.format("%s/hi", apiPrefix), "GET")
        );

        for (Pair<String, String> bypassToken : byPassTokens) {
            if (request.getServletPath().contains(bypassToken.getFirst()) && request.getMethod().equals(bypassToken.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
