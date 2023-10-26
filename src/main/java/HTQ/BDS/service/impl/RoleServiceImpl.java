package HTQ.BDS.service.impl;

import HTQ.BDS.entity.Roles;
import HTQ.BDS.repository.RoleRepository;
import HTQ.BDS.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public List<Roles> getAllRoles() {
        return repository.findAll();
    }
}
