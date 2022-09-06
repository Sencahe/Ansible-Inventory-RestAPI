package inventory.controllers;

import inventory.beans.ResponseMessage;
import inventory.models.Group;
import inventory.models.Host;
import inventory.models.OperativeSystem;
import inventory.services.GroupService;
import inventory.services.HostService;
import inventory.services.OperativeSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupController {

    @Autowired
    private GroupService groupService;
    @Autowired
    private HostService hostService;
    @Autowired
    private OperativeSystemService operativeSystemService;

    @Operation(summary = "Get a Group by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group found",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "404", description = "Group not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @GetMapping("/api/group/{id}")
    public ResponseEntity getGroup(@PathVariable("id") long id) {
        try {
            Group group = groupService.findGroupById(id);
            if (group == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Group>(group, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a Group by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group deleted",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Group not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @DeleteMapping("/api/group/{id}")
    public ResponseEntity deleteGroup(@PathVariable("id") long id) {
        try {
            Group group = groupService.findGroupById(id);
            if (group == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                groupService.deleteGroup(group);
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.DELETED_OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a Group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group Created",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PostMapping("/api/group/")
    public ResponseEntity saveGroup(@Valid @RequestBody Group group) {
        try {
            if (!validGroupName(group.getName())) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Group name cannot be the same as an Operative System name"), HttpStatus.BAD_REQUEST);
            }
            groupService.saveGroup(group);
            return new ResponseEntity<Group>(group, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update a Group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group Updated",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "404", description = "Group not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PutMapping("/api/group")
    public ResponseEntity updateGroup(@Valid @RequestBody Group group) {
        try {
            if (groupService.findGroup(group) == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            }
            if (!validGroupName(group.getName())) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Group name cannot be the same as an Operative System name"), HttpStatus.BAD_REQUEST);
            }
            groupService.saveGroup(group);
            return new ResponseEntity<Group>(groupService.findGroup(group), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add a Host into a Group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host added",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "400", description = "Host already into Group",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Host or Group not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PostMapping("/api/group/{groupId}/addHost/{hostId}")
    public ResponseEntity addHostToGroup(@PathVariable("groupId") long groupId, @PathVariable("hostId") long hostId) {
        try {

            Group group = groupService.findGroupById(groupId);
            if (group == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("The Group could not be found by the provided id"), HttpStatus.NOT_FOUND);
            }

            Host hostToAdd = hostService.findHostById(hostId);
            if (hostToAdd == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("The Host could not be found by the provided id"), HttpStatus.NOT_FOUND);
            }

            Set<Host> hosts = group.getHosts();
            if (hosts.contains(hostToAdd)) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("The Host it's already in the group"), HttpStatus.BAD_REQUEST);
            }
            hosts.add(hostToAdd);
            group.setHosts(hosts);
            groupService.saveGroup(group);
            return new ResponseEntity<Group>(group, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Remove a host from a Group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host removed",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "404", description = "Host or Group not found or group is not found within group",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @DeleteMapping("/api/group/{groupId}/removeHost/{hostId}")
    public ResponseEntity RemoveHostInGroup(@PathVariable("groupId") long groupId, @PathVariable("hostId") long hostId) {
        try {

            Group group = groupService.findGroupById(groupId);
            if (group == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("The Host could not be found by the provided id"), HttpStatus.NOT_FOUND);
            }

            Host hostToRemove = hostService.findHostById(hostId);
            if (hostToRemove == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("The Group could not be found by the provided id"), HttpStatus.NOT_FOUND);
            }

            Set<Host> hosts = group.getHosts();
            if (!hosts.contains(hostToRemove)) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("The Host was not found within the indicated Group"), HttpStatus.NOT_FOUND);
            }
            hosts.remove(hostToRemove);
            group.setHosts(hosts);
            groupService.saveGroup(group);
            return new ResponseEntity<Group>(group, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Add a Child Group into a Parent Group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Group Added",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "400", description = "ChildGroup already into ParentGroup, or ChildGroup is parent of ParentGroup",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Child or Parent Group not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PostMapping("/api/group/{parentGroupId}/addChild/{childGroupId}")
    public ResponseEntity addGroupIntoGroup(@PathVariable("parentGroupId") long parentGroupId,
            @PathVariable("childGroupId") long childGroupId) {
        try {
            if (parentGroupId == childGroupId) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("A group cannot be added to itself."), HttpStatus.BAD_REQUEST);
            }
            Group parentGroup = groupService.findGroupById(parentGroupId);
            if (parentGroup == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Parent Group was not found by it's id: " + parentGroupId), HttpStatus.NOT_FOUND);
            }

            Group childGroup = groupService.findGroupById(childGroupId);
            if (childGroup == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Child Group was not found by it's id:: " + childGroupId), HttpStatus.NOT_FOUND);
            }
            Set<Group> children = parentGroup.getChildren();
            if (children.contains(childGroup)) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Child Group is already within Parent Group"), HttpStatus.BAD_REQUEST);
            }

            if (childGroup.getChildren().contains(parentGroup)) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Child Group is a parent of the Parent Group indicated in the request"), HttpStatus.BAD_REQUEST);
            }

            children.add(childGroup);
            parentGroup.setChildren(children);
            groupService.saveGroup(parentGroup);
            return new ResponseEntity<Group>(parentGroup, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Remove a Child Group from a Parent Group")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host removed",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Group.class))}),
        @ApiResponse(responseCode = "400", description = "ChildGroup already into ParentGroup, or ChildGroup is parent of ParentGroup",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Child or Parent Group not found, or Child not found within Parent",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @DeleteMapping("/api/group/{parentGroupId}/removeGroup/{childGroupId}")
    public ResponseEntity removeGroupInGroup(@PathVariable("parentGroupId") long parentGroupId,
            @PathVariable("childGroupId") long childGroupId) {
        try {
            if (parentGroupId == childGroupId) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Parent Group cannot be the same as Child Group"), HttpStatus.BAD_REQUEST);
            }
            Group parentGroup = groupService.findGroupById(parentGroupId);
            if (parentGroup == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Parent Group was not found by it's id: " + parentGroupId), HttpStatus.NOT_FOUND);
            }

            Group childGroup = groupService.findGroupById(childGroupId);
            if (childGroup == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Child Group was not found by it's id: " + childGroupId), HttpStatus.NOT_FOUND);
            }
            Set<Group> children = parentGroup.getChildren();
            if (!children.contains(childGroup)) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage("Child Group was not found within Parent Group."), HttpStatus.BAD_REQUEST);
            }
            children.remove(childGroup);
            parentGroup.setChildren(children);
            groupService.saveGroup(parentGroup);
            return new ResponseEntity<Group>(parentGroup, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
    // Private methods for this controller
    
    private boolean validGroupName(String groupName) {
        List<OperativeSystem> operativeSystems = operativeSystemService.getAllOperativeSystems();
        for (OperativeSystem operativeSystem : operativeSystems) {
            if (operativeSystem.getName().toUpperCase().equals(groupName.toUpperCase())) {
                return false;
            }
        }
        return true;
    }
}
