package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.PVModelSource;
import fr.ifpen.synergreen.service.PVModelSourceService;
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
 * REST controller for managing PVModelSource.
 */
@RestController
@RequestMapping("/api")
public class PVModelSourceResource {

    private static final String ENTITY_NAME = "pVModelSource";
    private final Logger log = LoggerFactory.getLogger(PVModelSourceResource.class);
    private final PVModelSourceService pVModelSourceService;

    public PVModelSourceResource(PVModelSourceService pVModelSourceService) {
        this.pVModelSourceService = pVModelSourceService;
    }

    /**
     * POST  /pv-model-sources : Create a new pVModelSource.
     *
     * @param pVModelSource the pVModelSource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new pVModelSource, or with status 400 (Bad Request) if the pVModelSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pv-model-sources")
    @Timed
    public ResponseEntity<PVModelSource> createPVModelSource(@RequestBody PVModelSource pVModelSource) throws URISyntaxException {
        log.debug("REST request to save PVModelSource : {}", pVModelSource);
        if (pVModelSource.getId() != null) {
            throw new BadRequestAlertException("A new pVModelSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PVModelSource result = pVModelSourceService.save(pVModelSource);
        return ResponseEntity.created(new URI("/api/pv-model-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pv-model-sources : Updates an existing pVModelSource.
     *
     * @param pVModelSource the pVModelSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated pVModelSource,
     * or with status 400 (Bad Request) if the pVModelSource is not valid,
     * or with status 500 (Internal Server Error) if the pVModelSource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pv-model-sources")
    @Timed
    public ResponseEntity<PVModelSource> updatePVModelSource(@RequestBody PVModelSource pVModelSource) throws URISyntaxException {
        log.debug("REST request to update PVModelSource : {}", pVModelSource);
        if (pVModelSource.getId() == null) {
            return createPVModelSource(pVModelSource);
        }
        PVModelSource result = pVModelSourceService.save(pVModelSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, pVModelSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pv-model-sources : get all the pVModelSources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pVModelSources in body
     */
    @GetMapping("/pv-model-sources")
    @Timed
    public List<PVModelSource> getAllPVModelSources() {
        log.debug("REST request to get all PVModelSources");
        return pVModelSourceService.findAll();
    }

    /**
     * GET  /pv-model-sources/:id : get the "id" pVModelSource.
     *
     * @param id the id of the pVModelSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the pVModelSource, or with status 404 (Not Found)
     */
    @GetMapping("/pv-model-sources/{id}")
    @Timed
    public ResponseEntity<PVModelSource> getPVModelSource(@PathVariable Long id) {
        log.debug("REST request to get PVModelSource : {}", id);
        PVModelSource pVModelSource = pVModelSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(pVModelSource));
    }

    /**
     * DELETE  /pv-model-sources/:id : delete the "id" pVModelSource.
     *
     * @param id the id of the pVModelSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pv-model-sources/{id}")
    @Timed
    public ResponseEntity<Void> deletePVModelSource(@PathVariable Long id) {
        log.debug("REST request to delete PVModelSource : {}", id);
        pVModelSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/pv-model-sources?query=:query : search for the pVModelSource corresponding
     * to the query.
     *
     * @param query the query of the pVModelSource search
     * @return the result of the search
     */
    @GetMapping("/_search/pv-model-sources")
    @Timed
    public List<PVModelSource> searchPVModelSources(@RequestParam String query) {
        log.debug("REST request to search PVModelSources for query {}", query);
        return pVModelSourceService.search(query);
    }

}
