package inventory.controllers;

import inventory.beans.ResponseMessage;
import inventory.models.OperativeSystem;
import inventory.services.OperativeSystemService;
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
public class OperativeSystemController {

    @Autowired
    private OperativeSystemService operativeSystemService;


    @Operation(summary = "Get an Operative System by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operative System found",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OperativeSystem.class))}),
        @ApiResponse(responseCode = "404", description = "Operative System not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @GetMapping("/api/operativesystem/{id}")
    public ResponseEntity getOperativeSystem(@PathVariable("id") int id) {
        try {
            OperativeSystem operativeSystem = operativeSystemService.findOperativeSystemById(id);
            if (operativeSystem == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<OperativeSystem>(operativeSystem, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete an Operative System by its id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operative System deleted",
                content = @Content),
        @ApiResponse(responseCode = "404", description = "Operative System not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @DeleteMapping("/api/operativesystem/{id}")
    public ResponseEntity deleteOperativeSystem(@PathVariable("id") int id) {
        try {
            OperativeSystem operativeSystem = operativeSystemService.findOperativeSystemById(id);
            if (operativeSystem == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {
                operativeSystemService.deleteOperativeSystem(operativeSystem);
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.DELETED_OK), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Create an Operative System")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operative System Created",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OperativeSystem.class))}),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PostMapping("/api/operativesystem/")
    public ResponseEntity saveOperativeSystem(@Valid @RequestBody OperativeSystem operativeSystem) {
        try {
            operativeSystemService.saveOperativeSystem(operativeSystem);
            return new ResponseEntity<OperativeSystem>(operativeSystem, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Update a Operative System")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Operative System Updated",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = OperativeSystem.class))}),
        @ApiResponse(responseCode = "404", description = "Operative System not found",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content)})
    @ResponseBody
    @PutMapping("/api/operativesystem")
    public ResponseEntity updateOperativeSystem(@Valid @RequestBody OperativeSystem operativeSystem) {
        try {
            if (operativeSystemService.findOperativeSystem(operativeSystem) == null) {
                return new ResponseEntity<ResponseMessage>(new ResponseMessage(ResponseMessage.NOT_FOUND_BY_ID), HttpStatus.NOT_FOUND);
            } else {                
                operativeSystemService.saveOperativeSystem(operativeSystem);
                return new ResponseEntity<OperativeSystem>(operativeSystem, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<ResponseMessage>(new ResponseMessage(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
