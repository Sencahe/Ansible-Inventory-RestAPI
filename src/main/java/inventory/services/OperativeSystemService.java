package inventory.services;

import inventory.servicesinterfaces.IOperativeSystemService;
import inventory.dao.OperativeSystemDao;
import inventory.models.OperativeSystem;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OperativeSystemService implements IOperativeSystemService{
    
    @Autowired
    private OperativeSystemDao operativeSystemDao;
    
    @Override
    @Transactional
    public List<OperativeSystem> getAllOperativeSystems() {
        return (List<OperativeSystem>) operativeSystemDao.findAll();
    }

    @Override
    @Transactional
    public OperativeSystem findOperativeSystem(OperativeSystem operativeSystem) {
        return (OperativeSystem) operativeSystemDao.findById(operativeSystem.getOperativeSystemId()).orElse(null);
    }
    
    @Override
    @Transactional
    public OperativeSystem findOperativeSystemById(int operativeSystemId) {
        return (OperativeSystem) operativeSystemDao.findById(operativeSystemId).orElse(null);
    }

    @Override
    @Transactional
    public void saveOperativeSystem(OperativeSystem operativeSystem) {
        operativeSystemDao.save(operativeSystem);
    }

    @Override
    @Transactional
    public void deleteOperativeSystem(OperativeSystem operativeSystem) {
        operativeSystemDao.delete(operativeSystem);
    }
    
}
