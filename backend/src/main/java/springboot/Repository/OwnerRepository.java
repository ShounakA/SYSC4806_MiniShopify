package springboot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springboot.model.Owner;

import java.util.List;
import java.util.Optional;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

    List<Owner> findByFirstName(@Param("firstName") String firstName);
    List<Owner> findByLastName(@Param("lastName") String lastName);
    Owner findByEmail(@Param("email") String email);
    List<Owner> findByFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    Owner findByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    Optional<Owner> findById(Long id);
}
