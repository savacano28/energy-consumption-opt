package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.BatteryManagementRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the BatteryManagementRun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BatteryManagementRunRepository extends JpaRepository<BatteryManagementRun, Long> {

    /**/
   /* SELECT * FROM BATTERY_MANAGEMENT_RUN WHERE START IN (SELECT max(START) FROM  BATTERY_MANAGEMENT_RUN)*/
   /*@Query("select distinct bmr from BatteryManagementRun bmr where bmr.start in (select max(start) from BatteryManagementRun)")
    BatteryManagementRun getLastRun();*/

}
