package com.kt.giga.home.b2b.domain;

import com.google.common.base.MoreObjects;

/**
 * com.kt.giga.home.b2b.domain
 * <p>
 * Created by cecil on 2017. 1. 16..
 */
public enum Role {
    SUPER_MASTER((short) 10, null),
    MASTER((short) 20, (short) 30),
    SUPERVISOR((short) 30, (short) 40),
    STAFF((short) 40, null);

    private short authorityCd;
    private Short childRoleCd;

    Role(short authorityCd, Short childRoleCd) {
        this.authorityCd = authorityCd;
        this.childRoleCd = childRoleCd;
    }

    public Role getChildRole() {
        if (this.childRoleCd == null) {
            return null;
        } else {
            return Role.getRole(this.childRoleCd);
        }
    }

    public static Role getRole(short authorityCd) {
        for (Role role : values()) {
            if (role.authorityCd == authorityCd)
                return role;
        }

        throw new IllegalArgumentException("No matching role for authority code [" + authorityCd + "] found.");
    }

    public short getAuthorityCd() {
        return this.authorityCd;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("name", this.name())
                          .add("authorityCd", authorityCd)
                          .toString();
    }
}
