package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.FluxTopology;
import fr.ifpen.synergreen.service.FluxTopologyService;
import fr.ifpen.synergreen.service.dto.FluxTopologyDTO;
import fr.ifpen.synergreen.web.rest.errors.BadRequestAlertException;
import fr.ifpen.synergreen.web.rest.propertyeditors.FluxTopologyDTOEditor;
import fr.ifpen.synergreen.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FluxTopology.
 */
@RestController
@RequestMapping("/api")
public class FluxTopologyResource {

    private static final String ENTITY_NAME = "fluxTopology";
    private final Logger log = LoggerFactory.getLogger(FluxTopologyResource.class);
    private final FluxTopologyService fluxTopologyService;

    public FluxTopologyResource(FluxTopologyService fluxTopologyService) {
        this.fluxTopologyService = fluxTopologyService;
    }

    // property editor pour parser le fluxTopologyDTO qui vient d'un form multipart-data
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(FluxTopologyDTO.class, new FluxTopologyDTOEditor());
    }

    /**
     * POST  /flux-topologies : Create a new fluxTopology.
     *
     * @param fluxTopology the fluxTopology to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fluxTopology, or with status 400 (Bad Request) if the fluxTopology has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/flux-topologies")
    @Timed
    public ResponseEntity<FluxTopologyDTO> createFluxTopology(@RequestBody FluxTopologyDTO fluxTopology) throws URISyntaxException {
        log.debug("REST request to save FluxTopology : {}", fluxTopology);
        if (fluxTopology.getId() != null) {
            throw new BadRequestAlertException("A new fluxTopology cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FluxTopologyDTO result = fluxTopologyService.save(fluxTopology);
        return ResponseEntity.created(new URI("/api/flux-topologies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flux-topologies : Updates an existing fluxTopology.
     *
     * @param fluxTopology the fluxTopology to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fluxTopology,
     * or with status 400 (Bad Request) if the fluxTopology is not valid,
     * or with status 500 (Internal Server Error) if the fluxTopology couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/flux-topologies")
    @Timed
    public ResponseEntity<FluxTopologyDTO> updateFluxTopology(@RequestBody FluxTopologyDTO fluxTopology) throws URISyntaxException {
        log.debug("REST request to update FluxTopology : {}", fluxTopology);
        if (fluxTopology.getId() == null) {
            return createFluxTopology(fluxTopology);
        }
        FluxTopologyDTO result = fluxTopologyService.save(fluxTopology);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fluxTopology.getId().toString()))
            .body(result);
    }

    /**
     * POST  /type-technologie : Create ou update a new fluxTopology (attention avec multipart form data il faut tjs être en POST).
     *
     * @param fluxTopologyDTO the fluxTopology to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeTechnologie, or with status 400 (Bad Request) if the typeTechnologie has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping(value = "/flux-topology-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<FluxTopologyDTO> createOrUpdateFluxTopology(@RequestParam(value = "fluxTopology") FluxTopologyDTO fluxTopologyDTO,
                                           @RequestParam(value = "image", required = false) MultipartFile image) throws URISyntaxException {
        log.debug("REST request to save FluxTopology : {}, with Image", fluxTopologyDTO);
        FluxTopologyDTO result = fluxTopologyService.save(fluxTopologyDTO, image);

        return ResponseEntity.created(new URI("/api/flux-topologies/flux-topology-image/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(fluxTopologyDTO);
    }

    /**
     * GET  /flux-topologies : get all the fluxTopologies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxTopologies in body
     */
    @GetMapping("/flux-topologies")
    @Timed
    public List<FluxTopology> getAllFluxTopologies() {
        log.debug("REST request to get all FluxTopologies");
        return fluxTopologyService.findAll();
    }

    /**
     * GET  /flux-topologies : get all the fluxTopologies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxTopologies in body
     */
    @GetMapping("/flux-topologies/real")
    @Timed
    public List<FluxTopology> getAllRealFluxTopologies() {
        log.debug("REST request to get all FluxRealTopologies");
        return fluxTopologyService.findAllReal();
    }

    /**
     * GET  /flux-topologies : get all the fluxTopologies.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxTopologies in body
     */
    @GetMapping("/flux-topologies/pilotables")
    @Timed
    public List<FluxTopologyDTO> getAllPilotablesFluxTopologies() {
        log.debug("REST request to get all FluxRealTopologies");
        return fluxTopologyService.findAllPilotables();
    }

    /**
     * GET  /flux-topologies/:id : get the "id" fluxTopology.
     *
     * @param id the id of the fluxTopology to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fluxTopology, or with status 404 (Not Found)
     */
    @GetMapping("/flux-topologies/pilotable")
    @Timed
    public ResponseEntity<FluxTopologyDTO> getPilotableFluxTopology(@RequestParam Long id) {
        log.debug("REST request to get FluxTopology : {}", id);
        FluxTopologyDTO fluxTopology = fluxTopologyService.findOneDTO(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fluxTopology));
    }

    /**
     * GET  /flux-topologies/:id : get the "id" fluxTopology.
     *
     * @param id the id of the fluxTopology to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fluxTopology, or with status 404 (Not Found)
     */
    @GetMapping("/flux-topologies/{id}")
    @Timed
    public ResponseEntity<FluxTopologyDTO> getFluxTopologyDTO(@PathVariable Long id) {
        log.debug("REST request to get FluxTopology : {}", id);
        FluxTopologyDTO fluxTopology = fluxTopologyService.findOneDTO(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fluxTopology));
    }

    /**
     * GET  /flux-topologies/:id : get the "id" fluxTopology.
     *
     * @param id the id of the fluxTopology to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fluxTopology, or with status 404 (Not Found)
     */
    @GetMapping("/flux-topologies/basic/{id}")
    @Timed
    public ResponseEntity<FluxTopology> getBasicFluxTopology(@PathVariable Long id) {
        log.debug("REST request to get FluxTopology : {}", id);
        FluxTopology fluxTopology = fluxTopologyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fluxTopology));
    }

    /**
     * DELETE  /flux-topologies/:id : delete the "id" fluxTopology.
     *
     * @param id the id of the fluxTopology to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/flux-topologies/{id}")
    @Timed
    public ResponseEntity<Void> deleteFluxTopology(@PathVariable Long id) {
        log.debug("REST request to delete FluxTopology : {}", id);
        fluxTopologyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/flux-topologies?query=:query : search for the fluxTopology corresponding
     * to the query.
     *
     * @param query the query of the fluxTopology search
     * @return the result of the search
     */
    @GetMapping("/_search/flux-topologies")
    @Timed
    public List<FluxTopology> searchFluxTopologies(@RequestParam String query) {
        log.debug("REST request to search FluxTopologies for query {}", query);
        return fluxTopologyService.search(query);
    }

}
