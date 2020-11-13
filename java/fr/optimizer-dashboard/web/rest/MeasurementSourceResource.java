package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.MeasurementSource;
import fr.ifpen.synergreen.service.MeasurementSourceService;
import fr.ifpen.synergreen.service.dto.MeasurementSourceDTO;
import fr.ifpen.synergreen.web.rest.errors.BadRequestAlertException;
import fr.ifpen.synergreen.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing MeasurementSource.
 */
@RestController
@RequestMapping("/api")
public class MeasurementSourceResource {

    private static final String ENTITY_NAME = "measurementSource";
    private final Logger log = LoggerFactory.getLogger(MeasurementSourceResource.class);
    private final MeasurementSourceService measurementSourceService;

    public MeasurementSourceResource(MeasurementSourceService measurementSourceService) {
        this.measurementSourceService = measurementSourceService;
    }

    /**
     * POST  /measurement-source : Create a new measurementSource.
     *
     * @param measurementSource
     * @return the ResponseEntity with status 201 (Created) and with body the new measurementSource, or with status 400 (Bad Request) if the measurementSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measurement-source")
    @Timed
    public ResponseEntity<MeasurementSource> createMeasurementSource(@RequestParam MeasurementSource measurementSource) throws URISyntaxException {
        log.debug("REST request to save MeasurementSource : {}", measurementSource);
        if (measurementSource.getId() != null) {
            throw new BadRequestAlertException("A new measurementSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        measurementSourceService.save(measurementSource);
        return ResponseEntity.created(new URI("/api/measurement-source/" + measurementSource.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, measurementSource.getId().toString()))
            .body(measurementSource);
    }

    /**
     * PUT  /measurement-source : Updates an existing measurementSource.
     *
     * @param measurementSource the measurementSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measurementSource,
     * or with status 400 (Bad Request) if the measurementSource is not valid,
     * or with status 500 (Internal Server Error) if the measurementSource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measurement-source")
    @Timed
    public ResponseEntity<MeasurementSource> updateMeasurementSource(@RequestBody MeasurementSource measurementSource) throws URISyntaxException {
        log.debug("REST request to update MeasurementSource : {}", measurementSource);
        /*if (measurementSource.getId() == null) {
            return createMeasurementSource(measurementSource);
        }*/
        MeasurementSource result = measurementSourceService.save(measurementSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, measurementSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measurement-source : get all the measurementSources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measurementSources in body
     */
    @GetMapping("/measurement-source")
    @Timed
    public List<MeasurementSourceDTO> getAllMeasurementSources() {
        log.debug("REST request to get all MeasurementSources");
        return measurementSourceService.findAll();
    }


    /**
     * GET  /measurement-source/:id : get the "id" measurementSource.
     *
     * @param id the id of the measurementSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measurementSource, or with status 404 (Not Found)
     */
    @GetMapping("/measurement-source/{id}")
    @Timed
    public ResponseEntity<MeasurementSourceDTO> getMeasurementSource(@PathVariable Long id) {
        log.debug("REST request to get MeasurementSource : {}", id);
        MeasurementSourceDTO measurementSource = measurementSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(measurementSource));
    }

    /**
     * DELETE  /measurement-source/:id : delete the "id" measurementSource.
     *
     * @param id the id of the measurementSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measurement-source/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasurementSource(@PathVariable Long id) {
        log.debug("REST request to delete MeasurementSource : {}", id);
        measurementSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


}
