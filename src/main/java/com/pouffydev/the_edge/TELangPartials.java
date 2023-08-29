package com.pouffydev.the_edge;

import com.google.gson.JsonElement;
import com.simibubi.create.foundation.data.LangPartial;
import com.simibubi.create.foundation.utility.Lang;

import java.util.function.Supplier;

public enum TELangPartials implements LangPartial {
    INTERFACE("The Edge's UI & Messages"),
    TOOLTIPS("The Edge's Item Descriptions")
    ;

    private final String displayName;
    private final Supplier<JsonElement> provider;

    private TELangPartials(String displayName) {
        this.displayName = displayName;
        String fileName = Lang.asId(name());
        this.provider = () -> LangPartial.fromResource(TheEdge.ID, fileName);
    }

    private TELangPartials(String displayName, Supplier<JsonElement> provider) {
        this.displayName = displayName;
        this.provider = provider;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public JsonElement provide() {
        return provider.get();
    }
}
