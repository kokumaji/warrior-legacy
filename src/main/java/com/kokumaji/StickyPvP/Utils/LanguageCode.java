package com.kokumaji.StickyPvP.Utils;

public enum LanguageCode {
    en_US("English (US)"),
    de_DE("Deutsch (Deutschland)"),
    es_ES("Spanish (España)"),
    it_IT(""),
    ru_RU(""),
    fr_FR("");


    private String readable;
    LanguageCode(String pReadable) {
        this.readable = pReadable;
    }

    public String getReadable() {
        return readable;
    }

    public static LanguageCode fromString(String pString) {
        for(LanguageCode languageCode : LanguageCode.values()) {
            if(languageCode.getReadable().toLowerCase().equals(pString.toLowerCase())) {
                return languageCode;
            }
        }

        return null;
    }
}
