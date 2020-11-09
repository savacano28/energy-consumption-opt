package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.CSVSource;
import fr.ifpen.synergreen.service.CSVSourceService;
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
 * REST controller for managing CSVSource.
 */
@RestController
@RequestMapping("/api")
public class CSVSourceResource {

    private static final String ENTITY_NAME = "cSVSource";
    private final Logger log = LoggerFactory.getLogger(CSVSourceResource.class);
    private final CSVSourceService cSVSourceService;

    public CSVSourceResource(CSVSourceService cSVSourceService) {
        this.cSVSourceService = cSVSourceService;
    }

    /**
     * POST  /csv-sources : Create a new cSVSource.
     *
     * @param cSVSource the cSVSource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cSVSource, or with status 400 (Bad Request) if the cSVSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/csv-sources")
    @Timed
    public ResponseEntity<CSVSource> createCSVSource(@RequestBody CSVSource cSVSource) throws URISyntaxException {
        log.debug("REST request to save CSVSource : {}", cSVSource);
        if (cSVSource.getId() != null) {
            throw new BadRequestAlertException("A new cSVSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CSVSource result = cSVSourceService.save(cSVSource);
        return ResponseEntity.created(new URI("/api/csv-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /csv-sources : Updates an existing cSVSource.
     *
     * @param cSVSource the cSVSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cSVSource,
     * or with status 400 (Bad Request) if the cSVSource is not valid,
     * or with status 500 (Internal Server Error) if the cSVSource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/csv-sources")
    @Timed
    public ResponseEntity<CSVSource> updateCSVSource(@RequestBody CSVSource cSVSource) throws URISyntaxException {
        log.debug("REST request to update CSVSource : {}", cSVSource);
        if (cSVSource.getId() == null) {
            return createCSVSource(cSVSource);
        }
        CSVSource result = cSVSourceService.save(cSVSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cSVSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /csv-sources : get all the cSVSources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cSVSources in body
     */
    @GetMapping("/csv-sources")
    @Timed
    public List<CSVSource> getAllCSVSources() {
        log.debug("REST request to get all CSVSources");
        return cSVSourceService.findAll();
    }

    /**
     * GET  /csv-sources/:id : get the "id" cSVSource.
     *
     * @param id the id of the cSVSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cSVSource, or with status 404 (Not Found)
     */
    @GetMapping("/csv-sources/{id}")
    @Timed
    public ResponseEntity<CSVSource> getCSVSource(@PathVariable Long id) {
        log.debug("REST request to get CSVSource : {}", id);
        CSVSource cSVSource = cSVSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cSVSource));
    }

    /**
     * DELETE  /csv-sources/:id : delete the "id" cSVSource.
     *
     * @param id the id of the cSVSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/csv-sources/{id}")
    @Timed
    public ResponseEntity<Void> deleteCSVSource(@PathVariable Long id) {
        log.debug("REST request to delete CSVSource : {}", id);
        cSVSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/csv-sources?query=:query : search for the cSVSource corresponding
     * to the query.
     *
     * @param query the query of the cSVSource search
     * @return the result of the search
     */
    @GetMapping("/_search/csv-sources")
    @Timed
    public List<CSVSource> searchCSVSources(@RequestParam String query) {
        log.debug("REST request to search CSVSources for query {}", query);
        return cSVSourceService.search(query);
    }

}
