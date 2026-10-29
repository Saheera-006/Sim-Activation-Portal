package com.sim.portal.repository;

import com.sim.portal.entity.CustomerIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerIdentityRepository extends JpaRepository<CustomerIdentity, String> { }
