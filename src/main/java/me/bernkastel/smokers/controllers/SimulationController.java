package me.bernkastel.smokers.controllers;

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

    @Autowired
    public SimulationController(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostMapping("/start")
    public ResponseEntity<String> startSimulation() {
        simulation = beanFactory.getBean(SmokersSimulation.class);
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

    @PostMapping("/smoke-time")
    public ResponseEntity<String> setSmokeTime(@RequestParam int milliseconds) {
        simulation.setSmokeTime(milliseconds);
        return ResponseEntity.ok("Smoke time updated");
    }
}