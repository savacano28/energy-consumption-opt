package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.BatteryModelSource;
import fr.ifpen.synergreen.service.BatteryModelSourceService;
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
 * REST controller for managing BatteryModelSource.
 */
@RestController
@RequestMapping("/api")
public class BatteryModelSourceResource {

    private static final String ENTITY_NAME = "batteryModelSource";
    private final Logger log = LoggerFactory.getLogger(BatteryModelSourceResource.class);
    private final BatteryModelSourceService batteryModelSourceService;

    public BatteryModelSourceResource(BatteryModelSourceService batteryModelSourceService) {
        this.batteryModelSourceService = batteryModelSourceService;
    }

    /**
     * POST  /battery-model-sources : Create a new batteryModelSource.
     *
     * @param batteryModelSource the batteryModelSource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new batteryModelSource, or with status 400 (Bad Request) if the batteryModelSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/battery-model-sources")
    @Timed
    public ResponseEntity<BatteryModelSource> createBatteryModelSource(@RequestBody BatteryModelSource batteryModelSource) throws URISyntaxException {
        log.debug("REST request to save BatteryModelSource : {}", batteryModelSource);
        if (batteryModelSource.getId() != null) {
            throw new BadRequestAlertException("A new batteryModelSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BatteryModelSource result = batteryModelSourceService.save(batteryModelSource);
        return ResponseEntity.created(new URI("/api/battery-model-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /battery-model-sources : Updates an existing batteryModelSource.
     *
     * @param batteryModelSource the batteryModelSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated batteryModelSource,
     * or with status 400 (Bad Request) if the batteryModelSource is not valid,
     * or with status 500 (Internal Server Error) if the batteryModelSource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/battery-model-sources")
    @Timed
    public ResponseEntity<BatteryModelSource> updateBatteryModelSource(@RequestBody BatteryModelSource batteryModelSource) throws URISyntaxException {
        log.debug("REST request to update BatteryModelSource : {}", batteryModelSource);
        if (batteryModelSource.getId() == null) {
            return createBatteryModelSource(batteryModelSource);
        }
        BatteryModelSource result = batteryModelSourceService.save(batteryModelSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, batteryModelSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /battery-model-sources : get all the batteryModelSources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of batteryModelSources in body
     */
    @GetMapping("/battery-model-sources")
    @Timed
    public List<BatteryModelSource> getAllBatteryModelSources() {
        log.debug("REST request to get all BatteryModelSources");
        return batteryModelSourceService.findAll();
    }

    /**
     * GET  /battery-model-sources/:id : get the "id" batteryModelSource.
     *
     * @param id the id of the batteryModelSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the batteryModelSource, or with status 404 (Not Found)
     */
    @GetMapping("/battery-model-sources/{id}")
    @Timed
    public ResponseEntity<BatteryModelSource> getBatteryModelSource(@PathVariable Long id) {
        log.debug("REST request to get BatteryModelSource : {}", id);
        BatteryModelSource batteryModelSource = batteryModelSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(batteryModelSource));
    }

    /**
     * DELETE  /battery-model-sources/:id : delete the "id" batteryModelSource.
     *
     * @param id the id of the batteryModelSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/battery-model-sources/{id}")
    @Timed
    public ResponseEntity<Void> deleteBatteryModelSource(@PathVariable Long id) {
        log.debug("REST request to delete BatteryModelSource : {}", id);
        batteryModelSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/battery-model-sources?query=:query : search for the batteryModelSource corresponding
     * to the query.
     *
     * @param query the query of the batteryModelSource search
     * @return the result of the search
     */
    @GetMapping("/_search/battery-model-sources")
    @Timed
    public List<BatteryModelSource> searchBatteryModelSources(@RequestParam String query) {
        log.debug("REST request to search BatteryModelSources for query {}", query);
        return batteryModelSourceService.search(query);
    }

}
