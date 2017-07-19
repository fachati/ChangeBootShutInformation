package com.mobiwire.BootInformation;

/**
 * Created by Aly on 07/07/2017.
 */

public enum LanguageEnum {

    ARABIC("ar_EG"),
    FRENCH("fr_CA"),
    GERMAN("de_DE"),
    SPANISH("es_ES"),
    HINDI("hi_IN"),
    PORTUGUESE("pt_BR"),
    ITALIEN("it_IT"),
    ENGLISH("en_US")
    ;

    private String local;

    public String getLocal() {
        return local;
    }

    LanguageEnum(String local) {
        this.local = local;
    }

    public static LanguageEnum fromCode(String local) {
        for (LanguageEnum message : LanguageEnum.values()) {
            if (message.getLocal() == local) {
                return message;
            }
        }
        return ENGLISH;
    }
}
