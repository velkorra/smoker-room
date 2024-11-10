package me.bernkastel.smokers.dto;

import me.bernkastel.smokers.smoking.SmokersSimulation;

import java.util.List;

public class SimulationDto {
    private List<SmokerDto> smokers;
    private ServantDto servant;
    private TableDto table;

    public SimulationDto(SmokersSimulation simulation) {
        this.smokers = simulation.getSmokers().stream().map(SmokerDto::new).toList();
        this.servant = new ServantDto(simulation.getServant());
        this.table = new TableDto(simulation.getTable());
    }

    public List<SmokerDto> getSmokers() {
        return smokers;
    }

    public void setSmokers(List<SmokerDto> smokers) {
        this.smokers = smokers;
    }

    public ServantDto getServant() {
        return servant;
    }

    public void setServant(ServantDto servant) {
        this.servant = servant;
    }

    public TableDto getTable() {
        return table;
    }

    public void setTable(TableDto table) {
        this.table = table;
    }
}
