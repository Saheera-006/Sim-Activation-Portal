package com.sim.portal.repository;

import com.sim.portal.entity.SimOffers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimOffersRepository extends JpaRepository<SimOffers, Integer> {
    List<SimOffers> findBySimId(Integer simId);
}
