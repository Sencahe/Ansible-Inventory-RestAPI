package inventory.servicesinterfaces;

import inventory.models.Host;
import java.util.List;
import java.util.Set;


public interface IHostService {
       
    public List<Host> getAllHosts();
    
    public void saveHost(Host host);
    
    public void deleteHost(Host host);
    
    public Host findHost(Host host);
    
    public Host findHostById(long id);
    
}
