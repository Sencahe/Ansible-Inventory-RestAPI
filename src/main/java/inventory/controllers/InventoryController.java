package inventory.controllers;

import inventory.models.Group;
import inventory.models.Host;
import inventory.models.OperativeSystem;
import inventory.services.GroupService;
import inventory.services.HostService;
import inventory.services.OperativeSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Set;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
    
    @Autowired
    private HostService hostService;
    @Autowired
    private GroupService groupService;
    @Autowired 
    private OperativeSystemService operativeSystemService;

    @Operation(summary = "Get the inventory")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inventory JSON for Ansible",
                content = @Content),

        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @GetMapping("/api/inventory")
    public ResponseEntity getInventory(){
        
        JSONObject inventory = new JSONObject();
        
        // ADDING GROUPS TO INVENTORY
        List<Group> groups = groupService.getAllGroups();
        for(Group group: groups){
            // for each group, we save all the hosts and child within it into a json array
            JSONArray hostsArray = new JSONArray();
            Set<Host> groupHosts = group.getHosts();      
            for(Host host: groupHosts){
                hostsArray.add(host.getName());
            }
          
            JSONArray childrensArray = new JSONArray();
            Set<Group> children = group.getChildren();
            for(Group child: children){
                childrensArray.add(child.getName());
            }
           
            // we add the JSONarrays into the json object (group)
            JSONObject groupObject = new JSONObject();
            groupObject.appendField("hosts", hostsArray);
            groupObject.appendField("children", childrensArray);
           
            inventory.appendField(group.getName(), groupObject);       
        }
        
        // ADDING OPERATIVE SYSTEMS AS DEFAULTS GROUPS INTO INVENTORY
        List<OperativeSystem> operativeSystems = operativeSystemService.getAllOperativeSystems();
        for (OperativeSystem operativeSystem : operativeSystems){
            
            JSONArray hostsArray = new JSONArray();
            Set<Host> operativeSystemHosts = operativeSystem.getHosts();
            
            for(Host operativeSystemHost : operativeSystemHosts){
                hostsArray.add(operativeSystemHost.getName());
            }
            
            // hosts for operative system group  
            JSONObject operativeSystemGroupObj = new JSONObject();                
            operativeSystemGroupObj.appendField("hosts", hostsArray);
            
            // vars for operative system group adding "ansible_group_priority" as default with high number for a later variable merge
            JSONObject operativeSystemVarsObj = new JSONObject();
            operativeSystemVarsObj.appendField("ansible_group_priority", 999);  
            operativeSystemGroupObj.appendField("vars", operativeSystemVarsObj);
            
            inventory.appendField(operativeSystem.getName().toUpperCase(), operativeSystemGroupObj);

        }
        
        // ADDING HOSTS TO INVENTORY WITHIN _meta OBJECT and hostvars OBJECT
        List<Host> hosts = hostService.getAllHosts();    
        JSONObject hostvars = new JSONObject();
        for(Host host : hosts){
            JSONObject hostObject = new JSONObject();
            // setting ansible_host var as default variable
            hostObject.appendField("ansible_host", host.getHost());
            
            // adding hostObject into hostvars object
            hostvars.appendField(host.getName(), hostObject);
        }
        
        JSONObject meta = new JSONObject();
        meta.appendField("hostvars", hostvars);
        inventory.appendField("_meta", meta);
     
        return new ResponseEntity<JSONObject>(inventory, HttpStatus.OK);
    }
    

}
