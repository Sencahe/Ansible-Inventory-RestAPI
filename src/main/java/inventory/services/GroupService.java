/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.services;
import inventory.servicesinterfaces.IGroupService;
import inventory.dao.GroupDao;
import inventory.models.Group;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GroupService implements IGroupService{

    @Autowired
    private GroupDao groupDao;
    
    @Override
    public List<Group> getAllGroups() {
        return (List<Group>) groupDao.findAll();
    }

    @Override
    public Group findGroup(Group group) {
        return groupDao.findById(group.getGroupId()).orElse(null);
    }

    @Override
    public Group findGroupById(long groupId) {
        return groupDao.findById(groupId).orElse(null);
    }

    @Override
    public void saveGroup(Group group) {
        groupDao.save(group);
    }

    @Override
    public void deleteGroup(Group group) {
        groupDao.delete(group);
    }
    
}
