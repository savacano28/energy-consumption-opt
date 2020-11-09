package fr.ifpen.synergreen.service.dto;

import java.util.ArrayList;
import java.util.List;

public class BatteryManagementResultDTO {

    private Long runId;
    private String status;
    private String errorMsg;
    private String startComputation;
    private String endComputation;
    private List<BatteryManagementInstructionDTO> result = new ArrayList<>();

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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getStartComputation() {
        return startComputation;
    }

    public void setStartComputation(String startComputation) {
        this.startComputation = startComputation;
    }

    public String getEndComputation() {
        return endComputation;
    }

    public void setEndComputation(String endComputation) {
        this.endComputation = endComputation;
    }

    public List<BatteryManagementInstructionDTO> getResult() {
        return result;
    }

    public void setResult(List<BatteryManagementInstructionDTO> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BatteryManagementResultDTO{" +
            "runId=" + runId +
            ", status='" + status + '\'' +
            ", errorMsg='" + errorMsg + '\'' +
            ", startComputation='" + startComputation + '\'' +
            ", endComputation='" + endComputation + '\'' +
            ", result=" + result +
            '}';
    }
}
