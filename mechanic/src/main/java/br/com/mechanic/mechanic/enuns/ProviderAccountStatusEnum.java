package br.com.mechanic.mechanic.enuns;

public enum ProviderAccountStatusEnum {

    INITIAL_BLOCK("INITIAL_BLOCK"),
    BLOCK("BLOCK"),
    CANCEL("CANCEL"),
    ACTIVE("ACTIVE");

    private final String status;

    ProviderAccountStatusEnum(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.status;
    }

    public static ProviderAccountStatusEnum fromString(String status) {
        for (ProviderAccountStatusEnum s : ProviderAccountStatusEnum.values()) {
            if (s.status.equalsIgnoreCase(status)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + status);
    }
}
