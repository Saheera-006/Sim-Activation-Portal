
package com.sim.portal.service;

import com.sim.portal.dto.SimValidationRequest;
import com.sim.portal.dto.CreateSimRequest;
import com.sim.portal.entity.SimDetails;
import com.sim.portal.entity.SimOffers;
import com.sim.portal.exception.Messages;
import com.sim.portal.exception.SimPortalException;
import com.sim.portal.repository.SimDetailsRepository;
import com.sim.portal.repository.SimOffersRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SimService {

    private final SimDetailsRepository simDetailsRepository;
    private final SimOffersRepository simOffersRepository;

    public SimService(
            SimDetailsRepository simDetailsRepository,
            SimOffersRepository simOffersRepository) {
        this.simDetailsRepository = simDetailsRepository;
        this.simOffersRepository = simOffersRepository;
    }

    public List<SimDetails> getAllSims() {
        return simDetailsRepository.findAll();
    }

    @Transactional
    public SimDetails createSim(CreateSimRequest request) {

        // Prevent duplicate SIM + service number combinations
        boolean exists =
                simDetailsRepository.existsBySimNumberAndServiceNumber(
                        request.simNumber(),
                        request.serviceNumber()
                );

        if (exists) {
            throw new SimPortalException(
                    "SIM number and service number combination already exists",
                    HttpStatus.CONFLICT
            );
        }

        SimDetails sim = new SimDetails();

        sim.setSimNumber(request.simNumber());
        sim.setServiceNumber(request.serviceNumber());

        String status = request.simStatus();

        sim.setSimStatus(
                status == null || status.isBlank()
                        ? "inactive"
                        : status.toLowerCase()
        );

        SimDetails savedSim = simDetailsRepository.save(sim);

        // Get existing offers and identify distinct offer templates
        List<SimOffers> existingOffers = simOffersRepository.findAll();

        Map<String, SimOffers> uniqueOffers = new LinkedHashMap<>();

        for (SimOffers offer : existingOffers) {

            String key = offer.getOfferName() + "|"
                    + offer.getCallQty() + "|"
                    + offer.getCost() + "|"
                    + offer.getDataQty() + "|"
                    + offer.getDuration();

            uniqueOffers.putIfAbsent(key, offer);
        }

        // Copy each distinct offer to the newly created SIM
        List<SimOffers> offersForNewSim = new ArrayList<>();

        for (SimOffers template : uniqueOffers.values()) {

            SimOffers copy = new SimOffers();

            copy.setCallQty(template.getCallQty());
            copy.setCost(template.getCost());
            copy.setDataQty(template.getDataQty());
            copy.setDuration(template.getDuration());
            copy.setOfferName(template.getOfferName());
            copy.setSimId(savedSim.getSimId());

            offersForNewSim.add(copy);
        }

        if (!offersForNewSim.isEmpty()) {
            simOffersRepository.saveAll(offersForNewSim);
        }

        return savedSim;
    }

    public List<SimOffers> validateSim(
            SimValidationRequest request) {

        SimDetails sim = simDetailsRepository
                .findBySimNumberAndServiceNumber(
                        request.simNumber(),
                        request.serviceNumber()
                )
                .orElseThrow(() ->
                        new SimPortalException(
                                Messages.INVALID_SIM_DETAILS,
                                HttpStatus.BAD_REQUEST
                        )
                );

        if ("active".equalsIgnoreCase(sim.getSimStatus())) {
            throw new SimPortalException(
                    Messages.SIM_ALREADY_ACTIVE,
                    HttpStatus.CONFLICT
            );
        }

        return simOffersRepository.findBySimId(sim.getSimId());
    }
}
