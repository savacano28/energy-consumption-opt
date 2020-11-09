package fr.ifpen.synergreen.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A Run instance is defined by its status, start and end dates, and clientId/TopologyId/BatteryId combination
 */
public class BatteryManagementResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long runId;
    private String status;
    private String error;
    private ZonedDateTime start;
    private ZonedDateTime end;
    private List<BatteryManagementInstruction> result = new ArrayList<>();

    public BatteryManagementResult() {
    }

    public BatteryManagementResult(Long runId, String status, String error) {
        this.runId = runId;
        this.status = status;
        this.error = error;
    }

    public BatteryManagementResult(Long runId, String status, String error,
                                   List<BatteryManagementInstruction> result) {
        this.runId = runId;
        this.status = status;
        this.error = error;
        this.result = result;
    }

    public BatteryManagementResult(Long runId, String status, String error, ZonedDateTime start, ZonedDateTime end) {
        this.runId = runId;
        this.status = status;
        this.error = error;
        this.start = start;
        this.end = end;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
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

    public List<BatteryManagementInstruction> getResult() {
        return result;
    }

    public void setResult(List<BatteryManagementInstruction> result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BatteryManagementResult that = (BatteryManagementResult) o;
        return Objects.equals(runId, that.runId) &&
            Objects.equals(status, that.status) &&
            Objects.equals(error, that.error) &&
            Objects.equals(start, that.start) &&
            Objects.equals(end, that.end) &&
            Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(runId, status, error, start, end, result);
    }

    @Override
    public String toString() {
        return "BatteryManagementResult{" +
            "runId=" + runId +
            ", status='" + status + '\'' +
            ", errorMsg='" + error + '\'' +
            ", start=" + start +
            ", end=" + end +
            ", result=" + result +
            '}';
    }
}
