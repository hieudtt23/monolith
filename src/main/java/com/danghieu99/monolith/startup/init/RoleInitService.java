package com.danghieu99.monolith.startup.init;

import com.danghieu99.monolith.security.entity.Role;
import com.danghieu99.monolith.security.constant.ERole;
import com.danghieu99.monolith.security.repository.jpa.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class RoleInitService {

    private final RoleRepository roleRepository;

    @Transactional
    public void init() {
        Arrays.stream(ERole.values()).forEach(role -> {
            if (roleRepository.findByRole(role).isEmpty()) {
                roleRepository.save(new Role(role, role.getDescription()));
            }
        });
    }
}