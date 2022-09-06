
package inventory.dao;

import inventory.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface GroupDao extends CrudRepository<Group, Long> {
    
}
