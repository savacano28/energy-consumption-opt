package fr.ifpen.synergreen.domain;

import fr.ifpen.synergreen.service.dto.StateFluxNodeDTO;

import java.util.List;

/**
 * A FluxNode
 */

public interface FluxNodeInterface {

    /* methodes*/

    void calculateState(List<StateFluxNodeDTO> stateFluxNodeList);

}

