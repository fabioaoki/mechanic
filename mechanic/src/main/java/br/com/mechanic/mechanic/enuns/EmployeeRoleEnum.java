package br.com.mechanic.mechanic.enuns;

public enum EmployeeRoleEnum {

    MECHANIC("MECHANIC"), // Mecânico
    SERVICE_ADVISOR("SERVICE_ADVISOR"), // Consultor de Serviços
    RECEPTIONIST("RECEPTIONIST"), // Recepcionista
    MANAGER("MANAGER"), // Gerente
    PARTS_SPECIALIST("PARTS_SPECIALIST"), // Especialista em Peças
    CLEANER("CLEANER"), // Limpeza
    ELECTRICIAN("ELECTRICIAN"); // Eletricista

    private final String role;

    EmployeeRoleEnum(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return this.role;
    }

    public static EmployeeRoleEnum fromString(String role) {
        for (EmployeeRoleEnum s : EmployeeRoleEnum.values()) {
            if (s.role.equalsIgnoreCase(role)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}

