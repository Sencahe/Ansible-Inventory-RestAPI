package inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
        name = "HOSTS",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "host"),
            @UniqueConstraint(columnNames = "name")
        }
)
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hostId;

    @NotNull
    private String name;

    @NotNull
    private String host;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    @JoinColumn(name = "operative_system_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hosts"})
    private OperativeSystem operativeSystem;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groups_hosts",
            joinColumns = @JoinColumn(name = "host_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "hosts","children","parents"})
    private Set<Group> groups;

}
