package org.service;

import org.model.Role;

public class SecurityContext {
    private Role role;

    public SecurityContext(Role role){
        this.role = role;
    }

    public SecurityContext(){
        this.role = Role.USER;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
