package com.danghieu99.monolith.security.service.role;

import com.danghieu99.monolith.common.exception.ResourceNotFoundException;
import com.danghieu99.monolith.security.constant.ERole;
import com.danghieu99.monolith.security.repository.jpa.RoleRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danghieu99.monolith.security.entity.Role;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @CacheEvict("roles")
    public Role update(Role role) {
        return roleRepository.save(role);
    }

    @CacheEvict("roles")
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Cacheable("roles")
    public Role getByERole(ERole role) {
        return roleRepository.findByRole(role).orElseThrow();
    }

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Cacheable("roles")
    public Role getById(@NotNull int id) {
        return roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));
    }

    public Page<Role> getAllPaged(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public boolean existsByRole(ERole role) {
        return roleRepository.findByRole(role).isPresent();
    }
}