package com.api.estudo.enums;

import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

public enum UserType implements GrantedAuthority {
    ADMINISTRADOR, PILOTO, ESCUDERIA;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public static boolean isValid(String str) {
        if(str == null || str.isBlank())
            return false;
        return Objects.equals(UserType.ADMINISTRADOR.name(), str)
                || Objects.equals(UserType.ESCUDERIA.name(), str)
                || Objects.equals(UserType.PILOTO.name(), str);
    }
}
