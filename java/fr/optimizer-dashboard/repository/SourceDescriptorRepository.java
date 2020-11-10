package fr.ifpen.synergreen.repository;

import fr.ifpen.synergreen.domain.SourceDescriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data JPA repository for the SourceDescriptor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SourceDescriptorRepository extends JpaRepository<SourceDescriptor, Long> {

}
