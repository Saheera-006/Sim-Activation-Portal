package com.sim.portal.controller;

import com.sim.portal.dto.SimValidationRequest;
import com.sim.portal.dto.CreateSimRequest;
import com.sim.portal.entity.SimOffers;
import com.sim.portal.entity.SimDetails;
import com.sim.portal.service.SimService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sims")
public class SimController {

    private final SimService simService;

    public SimController(SimService simService) {
        this.simService = simService;
    }

    @PostMapping
    public ResponseEntity<SimDetails> createSim(@Valid @RequestBody CreateSimRequest request) {
        return ResponseEntity.status(201).body(simService.createSim(request));
    }

    @GetMapping
    public ResponseEntity<List<SimDetails>> getAllSims() {
        return ResponseEntity.ok(simService.getAllSims());
    }

    @PostMapping("/validate")
    public ResponseEntity<List<SimOffers>> validateSim(@Valid @RequestBody SimValidationRequest request) {
        return ResponseEntity.ok(simService.validateSim(request));
    }
}
