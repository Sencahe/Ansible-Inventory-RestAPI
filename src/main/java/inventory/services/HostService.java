/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.services;

import inventory.servicesinterfaces.IHostService;
import inventory.dao.HostDao;
import inventory.models.Host;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author panch
 */
@Service
public class HostService implements IHostService {

    @Autowired
    private HostDao hostDao;

    @Override
    @Transactional()
    public List<Host> getAllHosts() {
        return (List<Host>) hostDao.findAll();
    }

    @Override
    @Transactional()
    public void saveHost(Host host) {
        hostDao.save(host);
        hostDao.flush();

    }

    @Override
    @Transactional()
    public void deleteHost(Host host) {
        hostDao.delete(host);
    }

    @Override
    @Transactional()
    public Host findHost(Host host) {
        return (Host) hostDao.findById(host.getHostId()).orElse(null);
    }

    @Override
    @Transactional()
    public Host findHostById(long id) {
        return (Host) hostDao.findById(id).orElse(null);
    }

}
