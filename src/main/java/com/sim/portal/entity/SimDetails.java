
package com.sim.portal.entity;

import jakarta.persistence.*;

@Entity
@Table(
    name = "SimDetails",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_sim_service",
            columnNames = {"simNumber", "serviceNumber"}
        )
    }
)
public class SimDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer simId;

    @Column(nullable = false, length = 20)
    private String simNumber;

    @Column(nullable = false, length = 20)
    private String serviceNumber;

    @Column(nullable = false)
    private String simStatus;

    public SimDetails() {
    }

    public Integer getSimId() {
        return simId;
    }

    public void setSimId(Integer simId) {
        this.simId = simId;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getServiceNumber() {
        return serviceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        this.serviceNumber = serviceNumber;
    }

    public String getSimStatus() {
        return simStatus;
    }

    public void setSimStatus(String simStatus) {
        this.simStatus = simStatus;
    }
}
