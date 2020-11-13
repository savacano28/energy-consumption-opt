package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.FluxNode;
import fr.ifpen.synergreen.service.FluxNodeService;
import fr.ifpen.synergreen.service.dto.FluxNodeDTO;
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
 * REST controller for managing FluxNode.
 */
@RestController
@RequestMapping("/api")
public class FluxNodeResource {

    private static final String ENTITY_NAME = "fluxNode";
    private final Logger log = LoggerFactory.getLogger(FluxNodeResource.class);
    private final FluxNodeService fluxNodeService;

    public FluxNodeResource(FluxNodeService fluxNodeService) {
        this.fluxNodeService = fluxNodeService;
    }

    /**
     * POST  /flux-node : Create a new fluxNode.
     *
     * @param fluxNode the fluxNode to create
     * @return the ResponseEntity with status 201 (Created) and with body the new fluxNode, or with status 400 (Bad Request) if the fluxNode has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/flux-node")
    @Timed
    public ResponseEntity<FluxNode> createFluxNode(@RequestBody FluxNode fluxNode) throws URISyntaxException {
        log.debug("REST request to save FluxNode : {}", fluxNode);
        if (fluxNode.getId() != null) {
            throw new BadRequestAlertException("A new fluxNode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FluxNode result = fluxNodeService.save(fluxNode);
        return ResponseEntity.created(new URI("/api/flux-node/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /flux-node : Updates an existing fluxNode.
     *
     * @param fluxNode the fluxNode to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated fluxNode,
     * or with status 400 (Bad Request) if the fluxNode is not valid,
     * or with status 500 (Internal Server Error) if the fluxNode couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/flux-node")
    @Timed
    public ResponseEntity<FluxNode> updateFluxNode(@RequestBody FluxNode fluxNode) throws URISyntaxException {
        log.debug("REST request to update FluxNode : {}", fluxNode);
        if (fluxNode.getId() == null) {
            return createFluxNode(fluxNode);
        }
        FluxNode result = fluxNodeService.save(fluxNode);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, fluxNode.getId().toString()))
            .body(result);
    }

    /**
     * GET  /flux-node : get all the fluxNodes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxNodes in body
     */
    @GetMapping("/flux-nodes")
    @Timed
    public List<FluxNodeDTO> getAllFluxNodes() {
        log.debug("REST request to get all FluxNodes");
        return fluxNodeService.findAll();
    }

    /**
     * GET  /flux-node : get all the fluxNodes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxNodes in body
     */
    @GetMapping("/flux-nodes/flux-groups")
    @Timed
    public List<FluxNodeDTO> getAllFluxGroups() {
        log.debug("REST request to get all FluxGroups");
        return fluxNodeService.findFluxGroups();
    }

    /**
     * GET  /flux-node : get all the fluxNodes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxNodes in body
     */
    @GetMapping("/flux-nodes/energy-elements")
    @Timed
    public List<FluxNodeDTO> getAllEnergyElements() {
        log.debug("REST request to get all Energy Elements");
        return fluxNodeService.findEnergyElements();
    }

    /**
     * GET  /flux-node : get all the fluxNodes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of fluxNodes in body
     */
    @GetMapping("/flux-node/pilotables")
    @Timed
    public List<FluxNodeDTO> getAllPilotableFluxNodes() {
        log.debug("REST request to get all FluxNodes");
        return fluxNodeService.findAll();
    }

    /**
     * GET  /flux-node/:id : get the "id" fluxNode.
     *
     * @param id the id of the fluxNode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fluxNode, or with status 404 (Not Found)
     */
    @GetMapping("/flux-nodes/{id}")
    @Timed
    public ResponseEntity<FluxNodeDTO> getFluxNode(@PathVariable Long id) {
        log.debug("REST request to get FluxNode : {}", id);
        FluxNodeDTO fluxNode = fluxNodeService.findOneDTO(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fluxNode));
    }

    /**
     * GET  /flux-node/:id : get the "id" fluxNode.
     *
     * @param id the id of the fluxNode to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the fluxNode, or with status 404 (Not Found)
     */
    @GetMapping("/flux-nodes/pilotable/{id}")
    @Timed
    public ResponseEntity<FluxNodeDTO> getPilotableFluxNode(@PathVariable Long id) {
        log.debug("REST request to get FluxNode : {}", id);
        FluxNodeDTO fluxNode = fluxNodeService.findOneDTO(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(fluxNode));
    }

    /**
     * DELETE  /flux-node/:id : delete the "id" fluxNode.
     *
     * @param id the id of the fluxNode to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/flux-nodes/{id}")
    @Timed
    public ResponseEntity<Void> deleteFluxNode(@PathVariable Long id) {
        log.debug("REST request to delete FluxNode : {}", id);
        fluxNodeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }


}
