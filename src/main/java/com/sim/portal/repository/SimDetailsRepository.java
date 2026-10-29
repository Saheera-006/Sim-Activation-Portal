
package com.sim.portal.repository;

import com.sim.portal.entity.SimDetails;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimDetailsRepository
        extends JpaRepository<SimDetails, Integer> {

    Optional<SimDetails> findBySimNumberAndServiceNumber(
            String simNumber,
            String serviceNumber
    );

    boolean existsBySimNumberAndServiceNumber(
            String simNumber,
            String serviceNumber
    );
}
