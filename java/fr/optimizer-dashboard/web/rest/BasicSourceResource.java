package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.BasicSource;
import fr.ifpen.synergreen.service.BasicSourceService;
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
 * REST controller for managing BasicSource.
 */
@RestController
@RequestMapping("/api")
public class BasicSourceResource {

    private static final String ENTITY_NAME = "basicSource";
    private final Logger log = LoggerFactory.getLogger(BasicSourceResource.class);
    private final BasicSourceService basicSourceService;

    public BasicSourceResource(BasicSourceService basicSourceService) {
        this.basicSourceService = basicSourceService;
    }

    /**
     * POST  /basic-sources : Create a new basicSource.
     *
     * @param basicSource the basicSource to create
     * @return the ResponseEntity with status 201 (Created) and with body the new basicSource, or with status 400 (Bad Request) if the basicSource has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/basic-sources")
    @Timed
    public ResponseEntity<BasicSource> createBasicSource(@RequestBody BasicSource basicSource) throws URISyntaxException {
        log.debug("REST request to save BasicSource : {}", basicSource);
        if (basicSource.getId() != null) {
            throw new BadRequestAlertException("A new basicSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BasicSource result = basicSourceService.save(basicSource);
        return ResponseEntity.created(new URI("/api/basic-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /basic-sources : Updates an existing basicSource.
     *
     * @param basicSource the basicSource to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated basicSource,
     * or with status 400 (Bad Request) if the basicSource is not valid,
     * or with status 500 (Internal Server Error) if the basicSource couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/basic-sources")
    @Timed
    public ResponseEntity<BasicSource> updateBasicSource(@RequestBody BasicSource basicSource) throws URISyntaxException {
        log.debug("REST request to update BasicSource : {}", basicSource);
        if (basicSource.getId() == null) {
            return createBasicSource(basicSource);
        }
        BasicSource result = basicSourceService.save(basicSource);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, basicSource.getId().toString()))
            .body(result);
    }

    /**
     * GET  /basic-sources : get all the basicSources.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of basicSources in body
     */
    @GetMapping("/basic-sources")
    @Timed
    public List<BasicSource> getAllBasicSources() {
        log.debug("REST request to get all BasicSources");
        return basicSourceService.findAll();
    }

    /**
     * GET  /basic-sources/:id : get the "id" basicSource.
     *
     * @param id the id of the basicSource to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the basicSource, or with status 404 (Not Found)
     */
    @GetMapping("/basic-sources/{id}")
    @Timed
    public ResponseEntity<BasicSource> getBasicSource(@PathVariable Long id) {
        log.debug("REST request to get BasicSource : {}", id);
        BasicSource basicSource = basicSourceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(basicSource));
    }

    /**
     * DELETE  /basic-sources/:id : delete the "id" basicSource.
     *
     * @param id the id of the basicSource to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/basic-sources/{id}")
    @Timed
    public ResponseEntity<Void> deleteBasicSource(@PathVariable Long id) {
        log.debug("REST request to delete BasicSource : {}", id);
        basicSourceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/basic-sources?query=:query : search for the basicSource corresponding
     * to the query.
     *
     * @param query the query of the basicSource search
     * @return the result of the search
     */
    @GetMapping("/_search/basic-sources")
    @Timed
    public List<BasicSource> searchBasicSources(@RequestParam String query) {
        log.debug("REST request to search BasicSources for query {}", query);
        return basicSourceService.search(query);
    }

}
