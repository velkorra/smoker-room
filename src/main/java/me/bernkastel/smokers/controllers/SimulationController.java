package me.bernkastel.smokers.controllers;

import me.bernkastel.smokers.dto.StateDto;
import me.bernkastel.smokers.smoking.SimulationConstants;
import me.bernkastel.smokers.smoking.SmokersSimulation;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    private SmokersSimulation simulation;
    private final BeanFactory beanFactory;
    private int smokeTime = SimulationConstants.DEFAULT_SMOKE_TIME;
    private int checkTime = SimulationConstants.DEFAULT_CHECK_INTERVAL;

    @Autowired
    public SimulationController(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostMapping("/smoke-time-default")
    public ResponseEntity<String> setSmokeTime2(@RequestParam int milliseconds) {
        this.smokeTime = milliseconds;
        return ResponseEntity.ok("Check time updated");
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation() {
        simulation = beanFactory.getBean(SmokersSimulation.class);
        simulation.setSmokingTime(smokeTime);
        simulation.setDefaultCheckTime(checkTime);
        simulation.start();
        return ResponseEntity.ok("Simulation started");
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSimulation() {
        if (simulation != null) {
            simulation.stop();
            simulation = null;
        }
        return ResponseEntity.ok("Simulation stopped");
    }

    @PostMapping("/smoke-time-current")
    public ResponseEntity<String> setSmokeTime(@RequestParam int milliseconds) {
        simulation.setSmokeTime(milliseconds);
        return ResponseEntity.ok("Smoke time updated");
    }

    @PostMapping("/check-time-current")
    public ResponseEntity<String> setCheckTime(@RequestParam int milliseconds) {
        simulation.setCheckTimeCurrent(milliseconds);
        return ResponseEntity.ok("Check time updated");
    }

    @GetMapping("/check-state")
    public StateDto checkState() {
        var dto = new StateDto();
        dto.setCheckTime(checkTime);
        dto.setSmokingTime(smokeTime);
        if (simulation == null) {
            dto.setRunning(false);
        } else {
            dto.setRunning(simulation.isRunning());
        }
        return dto;
    }

    @PostMapping("/check-time-default")
    public ResponseEntity<String> setCheckTimeDefault(@RequestParam int milliseconds) {
        this.checkTime = milliseconds;
        return ResponseEntity.ok("Check time updated");
    }
}