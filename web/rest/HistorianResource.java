package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.HistorianSource;
import fr.ifpen.synergreen.service.HistorianService;
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
 * REST controller for managing Historian.
 */
@RestController
@RequestMapping("/api")
public class HistorianResource {

    private static final String ENTITY_NAME = "historian";
    private final Logger log = LoggerFactory.getLogger(HistorianResource.class);
    private final HistorianService historianService;

    public HistorianResource(HistorianService historianService) {
        this.historianService = historianService;
    }

    /**
     * POST  /historians : Create a new historian.
     *
     * @param historianSource the historian to create
     * @return the ResponseEntity with status 201 (Created) and with body the new historian, or with status 400 (Bad Request) if the historian has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/historians")
    @Timed
    public ResponseEntity<HistorianSource> createHistorian(@RequestBody HistorianSource historianSource) throws URISyntaxException {
        log.debug("REST request to save Historian : {}", historianSource);
        if (historianSource.getId() != null) {
            throw new BadRequestAlertException("A new historian cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HistorianSource result = historianService.save(historianSource);
        return ResponseEntity.created(new URI("/api/historians/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historians : Updates an existing historian.
     *
     * @param historianSource the historian to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated historian,
     * or with status 400 (Bad Request) if the historian is not valid,
     * or with status 500 (Internal Server Error) if the historian couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/historians")
    @Timed
    public ResponseEntity<HistorianSource> updateHistorian(@RequestBody HistorianSource historianSource) throws URISyntaxException {
        log.debug("REST request to update Historian : {}", historianSource);
        if (historianSource.getId() == null) {
            return createHistorian(historianSource);
        }
        HistorianSource result = historianService.save(historianSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, historianSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historians : get all the historians.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of historians in body
     */
    @GetMapping("/historians")
    @Timed
    public List<HistorianSource> getAllHistorians() {
        log.debug("REST request to get all Historians");
        return historianService.findAll();
    }


    /**
     * GET  /historians/:id : get the "id" historian.
     *
     * @param sensor the sensor' id of the historian to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historian, or with status 404 (Not Found)
     */
    @GetMapping("/historian/{id}")
    @Timed
    public List<List<Object>> getHistorian(@RequestParam String sensor,
                                           @RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam String interval) {
        log.debug("REST request to get Historian : {}", sensor);
        return HistorianService.getPowersFromHistorian(sensor, start, end, interval);
    }

    /**
     * GET  /historians/:id : get the "id" historian.
     *
     * @param id the id of the historian to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the historian, or with status 404 (Not Found)
     */
    @GetMapping("/historians/{id}")
    @Timed
    public ResponseEntity<HistorianSource> getHistorian(@PathVariable Long id) {
        log.debug("REST request to get Historian : {}", id);
        HistorianSource historianSource = historianService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(historianSource));
    }

    /**
     * DELETE  /historians/:id : delete the "id" historian.
     *
     * @param id the id of the historian to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/historians/{id}")
    @Timed
    public ResponseEntity<Void> deleteHistorian(@PathVariable Long id) {
        log.debug("REST request to delete Historian : {}", id);
        historianService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

/*    *//**
     * SEARCH  /_search/historians?query=:query : search for the historian corresponding
     * to the query.
     *
     * @param query the query of the historian search
     * @return the result of the search
     *//*
    @GetMapping("/_search/historians")
    @Timed
    public List<Historian> searchHistorians(@RequestParam String query) {
        log.debug("REST request to search Historians for query {}", query);
        return historianService.search(query);
    }*/

}
