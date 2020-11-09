package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.Invoice;
import fr.ifpen.synergreen.service.dto.InvoiceDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class InvoiceMapper {


    @Inject
    public InvoiceMapper() {

    }

    public InvoiceDTO toDto(Invoice invoice) {
        if (invoice == null) return null;
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setMonth(invoice.getMonth());
        dto.setYear(invoice.getYear());
        dto.setAmount(invoice.getAmount());
        dto.setAccAmount(invoice.getAccAmount());
        dto.setLastUpdateInvoice(invoice.getLastUpdateInvoice());

        return dto;
    }

    public Invoice toEntity(InvoiceDTO dto) {
        Invoice entity = new Invoice();
        entity.setId(dto.getId());
        entity.setYear(dto.getYear());
        entity.setFluxTopology(dto.getFluxTopology());
        entity.setMonth(dto.getMonth());
        entity.setAccAmount(dto.getAccAmount());
        entity.setLastUpdateInvoice(dto.getLastUpdateInvoice());

        return entity;
    }

}
