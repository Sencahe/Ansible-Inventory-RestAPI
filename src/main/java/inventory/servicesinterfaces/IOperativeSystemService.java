package inventory.servicesinterfaces;

import inventory.models.OperativeSystem;
import java.util.List;
import java.util.Set;

public interface IOperativeSystemService {
    
    public List<OperativeSystem> getAllOperativeSystems();
    
    public OperativeSystem findOperativeSystem(OperativeSystem operativeSystem);
    
    public OperativeSystem findOperativeSystemById(int operativeSystemId);
    
    public void saveOperativeSystem(OperativeSystem operativeSystem);
    
    public void deleteOperativeSystem(OperativeSystem operativeSystem);
    
    
}
