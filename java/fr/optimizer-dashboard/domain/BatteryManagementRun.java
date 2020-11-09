package fr.ifpen.synergreen.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Run instance is defined by its status, start and end dates, and clientId/TopologyId/BatteryId combination
 */
@Entity
@Table(name = "battery_management_run")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "batterymanagementrun")
public class BatteryManagementRun implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "battery_id")
    private Long batteryId;

    @Column(name = "start")
    private ZonedDateTime start;

    @Column(name = "end")
    private ZonedDateTime end;

    @Column(name = "status")
    private String status;

    @Column(name = "error")
    private String error;

    @Column(name = "process_id")
    private Long processId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "battery_manager_id")
    private BatteryManager batteryManager;

    @OneToMany(mappedBy = "batteryManagementRun", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<BatteryManagementInstruction> instructions = new ArrayList<>();


    public BatteryManagementRun() {
    }

    public BatteryManagementRun(Long clientId, Long batteryId, String status) {
        this.clientId = clientId;
        this.batteryId = batteryId;
        this.status = status;
    }


// jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(Long batteryId) {
        this.batteryId = batteryId;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<BatteryManagementInstruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<BatteryManagementInstruction> instructions) {
        this.instructions = instructions;
    }

    public BatteryManager getBatteryManager() {
        return batteryManager;
    }

    public void setBatteryManager(BatteryManager batteryManager) {
        this.batteryManager = batteryManager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatteryManagementRun that = (BatteryManagementRun) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(batteryId, that.batteryId) &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(status, that.status) &&
            Objects.equals(error, that.error) &&
            Objects.equals(processId, that.processId) &&
            Objects.equals(instructions, that.instructions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, batteryId, start, end, status, error, processId, instructions);
    }

    @Override
    public String toString() {
        return "BatteryManagementRun{" +
            "id=" + id +
            ", clientId=" + clientId +
            ", batteryId=" + batteryId +
            ", start=" + start +
            ", end=" + end +
            ", status='" + status + '\'' +
            ", errorMsg='" + error + '\'' +
            ", processId=" + processId +
            ", instructions=" + instructions +
            '}';
    }
}
