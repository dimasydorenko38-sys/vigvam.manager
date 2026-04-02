package com.sydorenko.vigvam.manager.enums.users;

import lombok.Getter;

@Getter
public enum SourceClient {
    INSTAGRAM("Instagram"),
    FACEBOOK("Facebook"),
    GOOGLE("Google"),
    OLX("OLX"),
    RECOMMENDATIONS("Рекомендації знайомих"),;

    private final String displayName;

    SourceClient(String displayName) {
        this.displayName = displayName;
    }
}
