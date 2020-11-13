package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.EnergySite;
import fr.ifpen.synergreen.service.EnergySiteService;
import fr.ifpen.synergreen.service.dto.EnergySiteDTO;
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
 * REST controller for managing EnergySite.
 */
@RestController
@RequestMapping("/api")
public class EnergySiteResource {

    private static final String ENTITY_NAME = "energySite";
    private final Logger log = LoggerFactory.getLogger(EnergySiteResource.class);
    private final EnergySiteService energySiteService;

    public EnergySiteResource(EnergySiteService energySiteService) {
        this.energySiteService = energySiteService;
    }

    /**
     * POST  /energy-sites : Create a new energySite.
     *
     * @param energySite the energySite to create
     * @return the ResponseEntity with status 201 (Created) and with body the new energySite, or with status 400 (Bad Request) if the energySite has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/energy-sites")
    @Timed
    public ResponseEntity<EnergySiteDTO> createEnergySite(@RequestBody EnergySiteDTO energySite) throws URISyntaxException {
        log.debug("REST request to save EnergySite : {}", energySite);
        if (energySite.getId() != null) {
            throw new BadRequestAlertException("A new energySite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnergySiteDTO result = energySiteService.save(energySite);
        return ResponseEntity.created(new URI("/api/energy-sites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /energy-sites : Updates an existing energySite.
     *
     * @param energySite the energySite to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated energySite,
     * or with status 400 (Bad Request) if the energySite is not valid,
     * or with status 500 (Internal Server Error) if the energySite couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/energy-sites")
    @Timed
    public ResponseEntity<EnergySiteDTO> updateEnergySite(@RequestBody EnergySiteDTO energySite) throws URISyntaxException {
        log.debug("REST request to update EnergySite : {}", energySite);
        if (energySite.getId() == null) {
            return createEnergySite(energySite);
        }
        EnergySiteDTO result = energySiteService.save(energySite);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, energySite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /energy-sites : get all the energySites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of energySites in body
     */
    @GetMapping("/energy-sites")
    @Timed
    public List<EnergySiteDTO> getAllEnergySites() {
        log.debug("REST request to get all EnergySites");
        return energySiteService.findAll();
    }


    /**
     * GET  /energy-sites : get all the energySites.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of energySites in body
     */
    @GetMapping("/energy-sites/pilotables")
    @Timed
    public List<EnergySiteDTO> getAllPilotablesEnergySites() {
        log.debug("REST request to get all EnergySites");
        return energySiteService.findAllPilotables();
    }

    /**
     * GET  /energy-sites/:id : get the "id" energySite.
     *
     * @param id the id of the energySite to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the energySite, or with status 404 (Not Found)
     */
    @GetMapping("/energy-sites/{id}")
    @Timed
    public ResponseEntity<EnergySiteDTO> getEnergySite(@PathVariable Long id) {
        log.debug("REST request to get EnergySite : {}", id);
        EnergySiteDTO energySite = energySiteService.findOne(id);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(energySite));
    }

    /**
     * DELETE  /energy-sites/:id : delete the "id" energySite.
     *
     * @param id the id of the energySite to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/energy-sites/{id}")
    @Timed
    public ResponseEntity<Void> deleteEnergySite(@PathVariable Long id) {
        log.debug("REST request to delete EnergySite : {}", id);
        energySiteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/energy-sites?query=:query : search for the energySite corresponding
     * to the query.
     *
     * @param query the query of the energySite search
     * @return the result of the search
     */
    @GetMapping("/_search/energy-sites")
    @Timed
    public List<EnergySite> searchEnergySites(@RequestParam String query) {
        log.debug("REST request to search EnergySites for query {}", query);
        return energySiteService.search(query);
    }

}
