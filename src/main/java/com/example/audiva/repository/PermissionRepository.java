package com.example.audiva.repository;

import com.example.audiva.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Permission save(Permission permission);
}
