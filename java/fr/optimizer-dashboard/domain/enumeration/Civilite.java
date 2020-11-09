package fr.ifpen.synergreen.domain.enumeration;

public enum Civilite implements LabelEnum {
    M("M."), MME("Mme"), PHD("PhD"), ME("Me"), PR("Pr");

    private String libelle;

    Civilite(String libelle) {
        this.libelle = libelle;
    }

    public static Civilite findByLabel(String libelle) {
        for (Civilite civilite : values()) {
            if (civilite.libelle.equals(libelle)) {
                return civilite;
            }
        }
        return null;
    }

    public static Civilite findByCode(String code) {
        try {
            return Civilite.valueOf(code);
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public String getLibelle() {
        return libelle;
    }

    @Override
    public boolean isActive() {
        return true;
    }
}
