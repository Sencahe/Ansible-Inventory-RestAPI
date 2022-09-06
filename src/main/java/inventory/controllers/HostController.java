package inventory.controllers;

import inventory.beans.ResponseMessage;
import inventory.models.Host;
import inventory.services.HostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
public class HostController {

    @Autowired
    private HostService hostService;

    @Operation(summary = "Get a host by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host found",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Host.class))}),
        @ApiResponse(responseCode = "404", description = "Host not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @GetMapping("/api/host/{id}")
    public ResponseEntity getHost(@PathVariable("id") long id) {
        try {
            Host host = hostService.findHostById(id);
            if (host == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<Host>(host, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete a host by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host deleted",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Host not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @DeleteMapping("/api/host/{id}")
    public ResponseEntity deleteHost(@PathVariable("id") long id) {
        try {
            Host host = hostService.findHostById(id);
            if (host == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                hostService.deleteHost(host);
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.DELETED_OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create a host")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host Created",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Host.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PostMapping("/api/host/")
    public ResponseEntity saveHost(@Valid @RequestBody Host host) {
        try {
            hostService.saveHost(host);
            return new ResponseEntity<Host>(host, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update a host")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Host Updated",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Host.class))}),
        @ApiResponse(responseCode = "404", description = "Host not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PutMapping("/api/host")
    public ResponseEntity updateHost(@Valid @RequestBody Host host) {
        try {
            if (hostService.findHost(host) == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                hostService.saveHost(host);
                return new ResponseEntity<Host>(hostService.findHost(host), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
