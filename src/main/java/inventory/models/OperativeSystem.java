package inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@Getter
@Setter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(
        name = "OPERATIVE_SYSTEMS",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "name")
        }
)
public class OperativeSystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private int operativeSystemId;

    @NotNull
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "operativeSystem", cascade={CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler","operativeSystem"})
    private Set<Host> hosts;

}
