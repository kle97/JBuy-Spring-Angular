package io.spring.jbuy.common.constant;

import org.springframework.stereotype.Component;

@Component("Role")
public final class Role {
    public static final String ADMIN = RoleName.ADMIN.role();
    public static final String CUSTOMER = RoleName.CUSTOMER.role();

    public enum RoleName {
        ADMIN("ROLE_ADMIN"),
        CUSTOMER("ROLE_CUSTOMER");

        private final String name;
        private final String role;

        RoleName(String name) {
            this.name = name;
            this.role = name.replace("ROLE_", "");
        }

        public String getName() {
            return this.name;
        }

        public String role() {
            return role;
        }
    }
}
