package com.ideaexpobackend.idea_expo_backend.models;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "role_id", length = 100, nullable = false, unique = true)
    private String roleId;

    @Column(name = "role_description", length = 256, nullable = true)
    private String roleDescription;

    public Role() {
    }

    public Role(String roleId, String roleDescription) {
        this.roleId = roleId;
        this.roleDescription = roleDescription;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    @Override
    public String toString() {
        return "Role{" +
                ", roleId='" + roleId + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                '}';
    }
}
