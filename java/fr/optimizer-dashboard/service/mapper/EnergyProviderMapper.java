package fr.ifpen.synergreen.service.mapper;

import fr.ifpen.synergreen.domain.EnergyProvider;
import fr.ifpen.synergreen.service.dto.EnergyProviderDTO;
import org.elasticsearch.common.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class EnergyProviderMapper {

    @Inject
    public EnergyProviderMapper() {
    }

    public EnergyProviderDTO toDto(EnergyProvider energyProvider) {
        if (energyProvider == null) {
            return null;
        }
        EnergyProviderDTO dto = new EnergyProviderDTO();
        dto.setId(energyProvider.getId());
        dto.setName(energyProvider.getName());
        dto.setHighHours(energyProvider.getHighHours());
        dto.setLowHours(energyProvider.getLowHours());
        dto.setMonthsHighSeasons(energyProvider.getMonthsHighSeasons());
        dto.setMonthsOffPeaks(energyProvider.getMonthsOffPeaks());
        dto.setPeakHours(energyProvider.getPeakHours());
        dto.setPurchaseAutoConsoRate(energyProvider.getPurchaseAutoConsoRate());
        dto.setPurchaseRate(energyProvider.getPurchaseRate());
        dto.setSellHighHoursHighSeason(energyProvider.getSellHighHoursHighSeason());
        dto.setSellHighHoursLowSeason(energyProvider.getSellHighHoursLowSeason());
        dto.setSellLowHoursHighSeason(energyProvider.getSellLowHoursHighSeason());
        dto.setSellLowHoursLowSeason(energyProvider.getSellLowHoursLowSeason());
        dto.setSellPeakHoursHighSeason(energyProvider.getSellPeakHoursHighSeason());
        dto.setSellHighHoursLowSeason(energyProvider.getSellHighHoursLowSeason());
        dto.setSellHighHoursHighSeason(energyProvider.getSellHighHoursHighSeason());
        dto.setSellPeakHoursLowSeason(energyProvider.getSellPeakHoursLowSeason());

        return dto;
    }

    public EnergyProvider toEntity(EnergyProviderDTO energyProviderDTO) {
        EnergyProvider energyProvider = new EnergyProvider();
        energyProvider.setId(energyProviderDTO.getId());
        energyProvider.setName(energyProviderDTO.getName());
        energyProvider.setHighHours(energyProviderDTO.getHighHours());
        energyProvider.setLowHours(energyProviderDTO.getLowHours());
        energyProvider.setMonthsHighSeasons(energyProviderDTO.getMonthsHighSeasons());
        energyProvider.setMonthsOffPeaks(energyProviderDTO.getMonthsOffPeaks());
        energyProvider.setPeakHours(energyProviderDTO.getPeakHours());
        energyProvider.setPurchaseAutoConsoRate(energyProviderDTO.getPurchaseAutoConsoRate());
        energyProvider.setPurchaseRate(energyProviderDTO.getPurchaseRate());
        energyProvider.setSellHighHoursHighSeason(energyProviderDTO.getSellHighHoursHighSeason());
        energyProvider.setSellHighHoursLowSeason(energyProviderDTO.getSellHighHoursLowSeason());
        energyProvider.setSellLowHoursHighSeason(energyProviderDTO.getSellLowHoursHighSeason());
        energyProvider.setSellLowHoursLowSeason(energyProviderDTO.getSellLowHoursLowSeason());
        energyProvider.setSellPeakHoursHighSeason(energyProviderDTO.getSellPeakHoursHighSeason());
        energyProvider.setSellHighHoursLowSeason(energyProviderDTO.getSellHighHoursLowSeason());
        energyProvider.setSellHighHoursHighSeason(energyProviderDTO.getSellHighHoursHighSeason());
        energyProvider.setSellPeakHoursLowSeason(energyProviderDTO.getSellPeakHoursLowSeason());

        return energyProvider;


    }
}
