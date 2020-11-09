package fr.ifpen.synergreen.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.ifpen.synergreen.domain.SourceDescriptor;
import fr.ifpen.synergreen.service.SourceDescriptorService;
import fr.ifpen.synergreen.service.dto.SourceDescriptorDTO;
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
 * REST controller for managing SourceDescriptor.
 */
@RestController
@RequestMapping("/api")
public class SourceDescriptorResource {

    private static final String ENTITY_NAME = "sourceDescriptor";
    private final Logger log = LoggerFactory.getLogger(SourceDescriptorResource.class);
    private final SourceDescriptorService sourceDescriptorService;

    public SourceDescriptorResource(SourceDescriptorService sourceDescriptorService) {
        this.sourceDescriptorService = sourceDescriptorService;
    }

    /**
     * POST  /source-descriptor : Create a new sourceDescriptor.
     *
     * @param sourceDescriptor
     * @return the ResponseEntity with status 201 (Created) and with body the new sourceDescriptor, or with status 400 (Bad Request) if the sourceDescriptor has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/source-descriptor")
    @Timed
    public ResponseEntity<SourceDescriptor> createSourceDescriptor(@RequestParam SourceDescriptor sourceDescriptor) throws URISyntaxException {
        log.debug("REST request to save SourceDescriptor : {}", sourceDescriptor);
        if (sourceDescriptor.getId() != null) {
            throw new BadRequestAlertException("A new sourceDescriptor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        sourceDescriptorService.save(sourceDescriptor);

        return ResponseEntity.created(new URI("/api/source-descriptor/" + sourceDescriptor.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, sourceDescriptor.getId().toString()))
            .body(sourceDescriptor);
    }

    /**
     * PUT  /source-descriptor : Updates an existing sourceDescriptor.
     *
     * @param sourceDescriptor the sourceDescriptor to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated sourceDescriptor,
     * or with status 400 (Bad Request) if the sourceDescriptor is not valid,
     * or with status 500 (Internal Server Error) if the sourceDescriptor couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/source-descriptor")
    @Timed
    public ResponseEntity<SourceDescriptor> updateSourceDescriptor(@RequestBody SourceDescriptor sourceDescriptor) throws URISyntaxException {
        log.debug("REST request to update SourceDescriptor : {}", sourceDescriptor);
        /*if (sourceDescriptor.getId() == null) {
            return createSourceDescriptor(sourceDescriptor);
        }*/
        SourceDescriptor result = sourceDescriptorService.save(sourceDescriptor);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, sourceDescriptor.getId().toString()))
            .body(result);
    }

    /**
     * GET  /source-descriptor : get all the sourceDescriptors.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sourceDescriptors in body
     */
    @GetMapping("/source-descriptor")
    @Timed
    public List<SourceDescriptorDTO> getAllSourceDescriptors() {
        log.debug("REST request to get all SourceDescriptors");
        return sourceDescriptorService.findAll();
    }

    /**
     * GET  /source-descriptor/:id : get the "id" sourceDescriptor.
     *
     * @param id the id of the sourceDescriptor to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the sourceDescriptor, or with status 404 (Not Found)
     */
    @GetMapping("/source-descriptor/{id}")
    @Timed
    public ResponseEntity<SourceDescriptorDTO> getSourceDescriptor(@PathVariable Long id) {
        log.debug("REST request to get SourceDescriptor : {}", id);
        SourceDescriptorDTO sourceDescriptor = sourceDescriptorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(sourceDescriptor));
    }

    /**
     * DELETE  /source-descriptor/:id : delete the "id" sourceDescriptor.
     *
     * @param id the id of the sourceDescriptor to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/source-descriptor/{id}")
    @Timed
    public ResponseEntity<Void> deleteSourceDescriptor(@PathVariable Long id) {
        log.debug("REST request to delete SourceDescriptor : {}", id);
        sourceDescriptorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
