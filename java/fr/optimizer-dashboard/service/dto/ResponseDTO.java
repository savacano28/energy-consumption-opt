package fr.ifpen.synergreen.service.dto;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ResponseDTO {
    private static final Logger log = LoggerFactory.getLogger(ResponseDTO.class);
    private List<String> headers;
    private List<List<Object>> values;

    public ResponseDTO() {
        headers = new ArrayList<>();
        values = new ArrayList<>();
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<Object>> getValues() {

        return values;
    }

    public void setValues(List<List<Object>> values) {
        this.values = values;
    }

    public void addHeaders(List<String> head) {
        try {
            headers.addAll(head);
        } catch (Exception e) {
            log.error(e + " Headers");
        }
    }

    public void addValues(List<List<Object>> val) {
        try {
            values.addAll(val);

        } catch (Exception e) {
            log.error(e + " Values");
        }
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
            "headers=" + headers +
            ", values=" + values +
            '}';
    }
}
