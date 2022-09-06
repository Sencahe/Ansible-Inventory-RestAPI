/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory.dao;

import inventory.models.Host;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author panch
 */
public interface HostDao extends JpaRepository<Host, Long>{
    
}
