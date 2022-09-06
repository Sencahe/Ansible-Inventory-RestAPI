package inventory.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@DynamicInsert
@DynamicUpdate
@Table(
        name = "GROUPS",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "name")
        }
)
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @NotNull
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "groups"})
    @JoinTable(name = "groups_hosts",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "host_id"))
    private Set<Host> hosts;

    // Self relation
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groups_parents_childrens",
            joinColumns = @JoinColumn(name = "children_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children","parents"})
    @Column(updatable=false)
    private Set<Group> parents;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "groups_parents_childrens",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "children_id"))
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "parents","children"})
        @Column(updatable=false)
    private Set<Group> children;

}
