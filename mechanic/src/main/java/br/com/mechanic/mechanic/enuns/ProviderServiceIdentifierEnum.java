package br.com.mechanic.mechanic.enuns;

import lombok.Getter;

@Getter
public enum ProviderServiceIdentifierEnum {

    ALINHAMENTO_PNEUS("ALINHAMENTO_PNEUS"),
    BALANCEAMENTO_PNEUS("BALANCEAMENTO_PNEUS"),
    TROCA_OLEO("TROCA_OLEO"),
    REVISAO_COMPLETA("REVISAO_COMPLETA"),
    TROCA_FILTROS("TROCA_FILTROS"),
    ALINHAMENTO_DIRECAO("ALINHAMENTO_DIRECAO"),
    TROCA_FREIOS("TROCA_FREIOS"),
    REPARO_SUSPENSAO("REPARO_SUSPENSAO"),
    TROCA_BATERIA("TROCA_BATERIA"),
    LAVAGEM_COMPLETA("LAVAGEM_COMPLETA");

    private final String identifier;

    ProviderServiceIdentifierEnum(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return this.identifier;
    }

    public static ProviderServiceIdentifierEnum fromIdentifier(String identifier) {
        for (ProviderServiceIdentifierEnum s : ProviderServiceIdentifierEnum.values()) {
            if (s.getIdentifier().equals(identifier)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Unknown identifier: " + identifier);
    }
}
