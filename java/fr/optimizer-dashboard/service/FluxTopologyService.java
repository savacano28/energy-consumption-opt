package fr.ifpen.synergreen.service;

import com.google.common.base.Strings;
import fr.ifpen.synergreen.domain.FluxNode;
import fr.ifpen.synergreen.domain.FluxTopology;
import fr.ifpen.synergreen.domain.enumeration.FluxNodeType;
import fr.ifpen.synergreen.repository.FluxTopologyRepository;
import fr.ifpen.synergreen.repository.search.FluxTopologySearchRepository;
import fr.ifpen.synergreen.service.dto.FluxTopologyDTO;
import fr.ifpen.synergreen.service.mapper.FluxTopologyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing FluxTopology.
 */
@Service
@Transactional
public class FluxTopologyService {

    private final Logger log = LoggerFactory.getLogger(FluxTopologyService.class);

    private final FluxTopologyRepository fluxTopologyRepository;
    private final FluxTopologySearchRepository fluxTopologySearchRepository;
    private FluxTopologyMapper fluxTopologyMapper;
    private final FileService fileService;

    public FluxTopologyService(FluxTopologyRepository fluxTopologyRepository,
                               FluxTopologySearchRepository fluxTopologySearchRepository,
                               FluxTopologyMapper fluxTopologyMapper,
                               FileService fileService) {

        this.fluxTopologyRepository = fluxTopologyRepository;
        this.fluxTopologySearchRepository = fluxTopologySearchRepository;
        this.fluxTopologyMapper = fluxTopologyMapper;
        this.fileService = fileService;
    }

    /**
     * Save a fluxTopology.
     *
     * @param fluxTopology the entity to save
     * @return the persisted entity
     */
    public FluxTopologyDTO save(FluxTopologyDTO fluxTopology) {
        log.debug("Request to save FluxTopology : {}", fluxTopology);
        FluxTopologyDTO result = fluxTopologyMapper.toDto(fluxTopologyRepository.save(fluxTopologyMapper.toEntity(fluxTopology)));
        return result;
    }

    /**
     * Save a typeTechnologie.
     *
     * @param fluxTopologyDTO the entity to save
     * @return the persisted entity
     */
    public FluxTopologyDTO save(FluxTopologyDTO fluxTopologyDTO, MultipartFile fichier) {
        log.debug("Request to save FluxTopology : {}", fluxTopologyDTO);

        String fichierASupprimer = null;
        // si le fichier a été mis à jour
        if (fichier != null) {
            // on envoie un nouveau fichier, on va supprimer le précédent
            if (!Strings.isNullOrEmpty(fluxTopologyDTO.getImg())) {
                fichierASupprimer = fluxTopologyDTO.getImg();
            }
            File file = fileService.uploadFichier(fichier);
            fluxTopologyDTO.setImg(file.getName());
        }

        fluxTopologyRepository.save(fluxTopologyMapper.toEntityWithOutInvoices(fluxTopologyDTO));
        // on supprime l'ancien fichier (on fait ça après avoir sauvegardé le nouveau en cas d'erreur)
        if (fichierASupprimer != null) {
            fileService.deleteFichier(fichierASupprimer);
        }
        return fluxTopologyDTO;
    }

    /**
     * Update a fluxTopology.
     *
     * @param fluxTopology the entity to update
     * @return the persisted entity
     */
    public FluxTopology update(FluxTopology fluxTopology) {
        log.debug("Request to save FluxTopology : {}", fluxTopology);
        FluxTopology result = fluxTopologyRepository.save(fluxTopology);
        return result;
    }


    /**
     * Get all the fluxTopologies.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FluxTopology> findAll() {
        log.debug("Request to get all FluxTopologies");
        return fluxTopologyRepository.findAll();
    }

    /**
     * Get all the fluxTopologies.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FluxTopology> findAllReal() {
        log.debug("Request to get all FluxTopologies");
        return fluxTopologyRepository.findAll().stream().filter(f -> !f.isOptimization()).collect(Collectors.toList());
    }

    /**
     * Get all the fluxTopologies with pilotables elements.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FluxTopologyDTO> findAllPilotables() {
        log.debug("Request to get all FluxTopologies");
        return fluxTopologyRepository.findAll()
            .stream()
            .filter(f ->
                f.getChildren()
                    .stream()
                    .map(FluxNode::getAllChildren)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList())
                    .stream()
                    .anyMatch(
                        e -> e.getType().equals(FluxNodeType.BATTERY)))
            .map(fluxTopologyMapper::toDto).collect(Collectors.toList());
    }


    /**
     * Get one fluxTopology by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FluxTopology findOne(Long id) {
        return fluxTopologyRepository.findOne(id);

    }


    /**
     * Get one fluxTopologyDTO by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public FluxTopologyDTO findOneDTO(Long id) {
        FluxTopology fluxTopology = fluxTopologyRepository.findOne(id);
        FluxTopologyDTO fluxTopologyDTO = fluxTopologyMapper.toDto(fluxTopology);
        return fluxTopologyDTO;


    }


    /**
     * Delete the fluxTopology by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete FluxTopology : {}", id);
        fluxTopologyRepository.delete(id);
        fluxTopologySearchRepository.delete(id);
    }

    /**
     * Search for the fluxTopology corresponding to the query.
     *
     * @param query the query of the search
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<FluxTopology> search(String query) {
        log.debug("Request to search FluxTopologies for query {}", query);
        return StreamSupport
            .stream(fluxTopologySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
