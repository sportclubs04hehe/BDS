package HTQ.BDS.service;

import HTQ.BDS.dtos.UserDTO;
import HTQ.BDS.entity.User;

public interface UserService {
    User createUser(UserDTO userDTO);
    String login(String phoneNumber, String password);
}
