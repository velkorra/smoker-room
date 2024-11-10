package me.bernkastel.smokers.dto;

import me.bernkastel.smokers.smoking.Servant;

public class ServantDto {
    private String name;

    public ServantDto(Servant servant) {
        this.name = servant.getServantName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
