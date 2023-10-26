package HTQ.BDS.service.impl;

import HTQ.BDS.components.JwtTokenUtils;
import HTQ.BDS.dtos.UserDTO;
import HTQ.BDS.entity.Roles;
import HTQ.BDS.entity.User;
import HTQ.BDS.exception.AppException;
import HTQ.BDS.repository.RoleRepository;
import HTQ.BDS.repository.UserRepository;
import HTQ.BDS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final ModelMapper modelMapper;
    @Override
    public User createUser(UserDTO userDTO) {
        String username = userDTO.getUsernames()    ;

        if(repository.existsByUsernames(username)){ // kiểm tra số username đã tồn tại hay chưa
            throw new AppException(BAD_REQUEST, "Username already exists");
        }

        Roles role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new AppException(NOT_FOUND,"Role Not Found"));

        if(role.getName().toUpperCase().equals("ADMIN")){
            throw new AppException(BAD_REQUEST,"Your cannot register an account has role admin. Please send an email to vegakinvietnam@gmail.com for support");
        }

        User newUSer = User.builder()
                .usernames(userDTO.getUsernames())
                .email(userDTO.getEmail())
                .profilePicture(userDTO.getProfilePicture())
//                .lastLoginAt(new Date())
                .build();
        newUSer.setRole(role);
        String password = userDTO.getPassword();
        String encodedPassword = encoder.encode(password);
        newUSer.setPassword(encodedPassword);
        return repository.save(newUSer);
    }

    @Override
    public String login(String usernames, String password) {
        User user = repository.findByUsernames(usernames)
                .orElseThrow(() -> new AppException(NOT_FOUND,"Invalid Username / Password."));
        if(!encoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("Wrong phone number of password");
        }
        user.setOnlyUpdateLastLogin(true); //
        user.setLastLoginAt(LocalDateTime.now());
        repository.save(user);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                usernames, password, user.getAuthorities()
        );
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtils.generateToken(user);
    }
}
