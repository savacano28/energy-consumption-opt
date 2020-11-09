package fr.ifpen.synergreen.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class FluxPeriod implements Serializable {
    private ZonedDateTime start;
    private ZonedDateTime end;
    private Long step;

    public FluxPeriod() {
    }

    public FluxPeriod(ZonedDateTime start, ZonedDateTime end, Long step) {
        this.start = start;
        this.end = end;
        this.step = step;
    }


    public ZonedDateTime getStart() {
        return start;
    }

    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    public FluxPeriod start(ZonedDateTime start) {
        this.start = start;
        return this;
    }

    public ZonedDateTime getEnd() {
        return end;
    }

    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public FluxPeriod end(ZonedDateTime end) {
        this.end = end;
        return this;
    }

    public Long getStep() {
        return step;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public void setStep(Long step) {
        this.step = step;
    }

    public FluxPeriod step(Long step) {
        this.step = step;
        return this;
    }

    @Override
    public String toString() {
        return "FluxPeriod{" +
            "start=" + start +
            ", end=" + end +
            ", step=" + step +
            '}';
    }
}
