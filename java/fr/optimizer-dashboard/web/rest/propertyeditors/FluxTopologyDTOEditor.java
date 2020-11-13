package fr.ifpen.synergreen.web.rest.propertyeditors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ifpen.synergreen.domain.FluxTopology;
import fr.ifpen.synergreen.service.dto.FluxTopologyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.beans.PropertyEditorSupport;

public class FluxTopologyDTOEditor extends PropertyEditorSupport {

        private final Logger log = LoggerFactory.getLogger(FluxTopologyDTOEditor.class);

        private ObjectMapper mapper;

        public FluxTopologyDTOEditor() {
            mapper = (new MappingJackson2HttpMessageConverter()).getObjectMapper();
        }

        /**
         * Format the FluxTopology as String
         *
         * @return FluxTopology formatted string
         */
        public String getAsText() {
            FluxTopologyDTO value = (FluxTopologyDTO) getValue();
            try {
                return value != null ? mapper.writeValueAsString(value) : "";
            } catch (JsonProcessingException e) {
                log.error("Error when serializing FluxTopology : {}", e.getMessage());
                return e.getMessage();
            }
        }

        /**
         * Parse the value from the given text, using Jackson.
         *
         * @param text the text to format
         * @throws IllegalArgumentException
         */
        public void setAsText(String text) throws IllegalArgumentException {
            try {
                setValue(mapper.readValue(text, FluxTopologyDTO.class));
            } catch (Exception e) {
                log.error("Error when deserializing FluxTopology : {}", e.getMessage());
                throw new IllegalArgumentException(e);
            }
        }
    }
