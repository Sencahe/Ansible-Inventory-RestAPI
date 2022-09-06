package inventory.servicesinterfaces;

import inventory.models.Group;
import java.util.List;
import java.util.Set;

public interface IGroupService {
    
    public List<Group> getAllGroups();
    
    public Group findGroup (Group group);
    
    public Group findGroupById(long groupId);
    
    public void saveGroup(Group group);
    
    public void deleteGroup(Group group);
}
