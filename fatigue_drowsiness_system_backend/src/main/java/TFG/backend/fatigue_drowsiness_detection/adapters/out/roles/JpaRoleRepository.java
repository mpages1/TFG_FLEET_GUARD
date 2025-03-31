package TFG.backend.fatigue_drowsiness_detection.adapters.out.roles;

import org.springframework.stereotype.Repository;

import TFG.backend.fatigue_drowsiness_detection.application.out.RoleRepository;
import TFG.backend.fatigue_drowsiness_detection.exceptions.RoleNotFoundException;
import TFG.backend.fatigue_drowsiness_detection.model.role.RoleModel;

@Repository
public class JpaRoleRepository implements RoleRepository {

    private final SpringDataRoleRepositoy roleRepository;

    public JpaRoleRepository(SpringDataRoleRepositoy roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void save(RoleModel role) {
        roleRepository.save(RoleMapper.toEntity(role));
    }

    @Override
    public String getRoleName(Integer id) {
        if (roleRepository.findById(id).isPresent()) {
            return roleRepository.findById(id).get().getName();
        }
        throw new RoleNotFoundException("Role with id " + id + " not found");
    }

}
