package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.EnergyProvider;
import fr.ifpen.synergreen.service.EnergyProviderService;
import fr.ifpen.synergreen.service.dto.EnergyProviderDTO;
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
 * REST controller for managing EnergyProvider.
 */
@RestController
@RequestMapping("/api")
public class EnergyProviderResource {

    private static final String ENTITY_NAME = "energyProvider";
    private final Logger log = LoggerFactory.getLogger(EnergyProviderResource.class);
    private final EnergyProviderService energyProviderService;

    public EnergyProviderResource(EnergyProviderService energyProviderService) {
        this.energyProviderService = energyProviderService;
    }

    /**
     * POST  /energy-providers : Create a new energyProvider.
     *
     * @param energyProvider the energyProvider to create
     * @return the ResponseEntity with status 201 (Created) and with body the new energyProvider, or with status 400 (Bad Request) if the energyProvider has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/energy-providers")
    @Timed
    public ResponseEntity<EnergyProvider> createEnergyProvider(@RequestBody EnergyProvider energyProvider) throws URISyntaxException {
        log.debug("REST request to save EnergyProvider : {}", energyProvider);
        if (energyProvider.getId() != null) {
            throw new BadRequestAlertException("A new energyProvider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EnergyProvider result = energyProviderService.save(energyProvider);
        return ResponseEntity.created(new URI("/api/energy-providers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /energy-providers : Updates an existing energyProvider.
     *
     * @param energyProvider the energyProvider to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated energyProvider,
     * or with status 400 (Bad Request) if the energyProvider is not valid,
     * or with status 500 (Internal Server Error) if the energyProvider couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/energy-providers")
    @Timed
    public ResponseEntity<EnergyProvider> updateEnergyProvider(@RequestBody EnergyProvider energyProvider) throws URISyntaxException {
        log.debug("REST request to update EnergyProvider : {}", energyProvider);
        if (energyProvider.getId() == null) {
            return createEnergyProvider(energyProvider);
        }
        EnergyProvider result = energyProviderService.save(energyProvider);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, energyProvider.getId().toString()))
            .body(result);
    }

    /**
     * GET  /energy-providers : get all the energyProviders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of energyProviders in body
     */
    @GetMapping("/energy-providers")
    @Timed
    public List<EnergyProviderDTO> getAllEnergyProviders() {
        log.debug("REST request to get all EnergyProviders");
        return energyProviderService.findAll();
    }

    /**
     * GET  /energy-providers/:id : get the "id" energyProvider.
     *
     * @param id the id of the energyProvider to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the energyProvider, or with status 404 (Not Found)
     */
    @GetMapping("/energy-providers/{id}")
    @Timed
    public ResponseEntity<EnergyProviderDTO> getEnergyProvider(@PathVariable Long id) {
        log.debug("REST request to get EnergyProvider : {}", id);
        EnergyProviderDTO energyProvider = energyProviderService.findOneDTO(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(energyProvider));
    }

    /**
     * DELETE  /energy-providers/:id : delete the "id" energyProvider.
     *
     * @param id the id of the energyProvider to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/energy-providers/{id}")
    @Timed
    public ResponseEntity<Void> deleteEnergyProvider(@PathVariable Long id) {
        log.debug("REST request to delete EnergyProvider : {}", id);
        energyProviderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/energy-providers?query=:query : search for the energyProvider corresponding
     * to the query.
     *
     * @param query the query of the energyProvider search
     * @return the result of the search
     */
    @GetMapping("/_search/energy-providers")
    @Timed
    public List<EnergyProvider> searchEnergyProviders(@RequestParam String query) {
        log.debug("REST request to search EnergyProviders for query {}", query);
        return energyProviderService.search(query);
    }

}
