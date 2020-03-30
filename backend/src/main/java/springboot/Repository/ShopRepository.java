package springboot.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springboot.model.Shop;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>{

    List<Shop> findByName(@Param("name") String name);
    List<Shop> findByDescription(@Param("description")String description);
    List<Shop> findByNameAndDescription(@Param("name") String name, @Param("description")String description);
    List<Shop> findByTagsIn(@Param("tags") List<String> tags);
    List<Shop> findByNameAndTagsIn(@Param("name") String name, @Param("tags") List<String> tags);
    List<Shop> findByDescriptionAndTagsIn(@Param("description") String description, @Param("tags") List<String> tags);
    List<Shop> findByNameAndDescriptionAndTagsIn(@Param("name") String name, @Param("description")String description, @Param("tags") List<String> tags);

//    Shop findById (long id);
    Optional<Shop> findById(Long id);
}
