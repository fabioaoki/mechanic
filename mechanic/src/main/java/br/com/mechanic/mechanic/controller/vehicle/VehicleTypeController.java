package br.com.mechanic.mechanic.controller.vehicle;

import br.com.mechanic.mechanic.exception.ErrorResponse;
import br.com.mechanic.mechanic.exception.ProviderAccountException;
import br.com.mechanic.mechanic.exception.VehicleTypeException;
import br.com.mechanic.mechanic.request.VehicleTypeRequestDto;
import br.com.mechanic.mechanic.response.VehicleTypeResponseDto;
import br.com.mechanic.mechanic.service.VehicleTypeServiceBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/vehicle-type")
public class VehicleTypeController {

    @Autowired
    private VehicleTypeServiceBO vehicleTypeServiceBO;

    @GetMapping
    public ResponseEntity<Page<VehicleTypeResponseDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(vehicleTypeServiceBO.findAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleTypeById(@PathVariable Long id) {
        try {
            log.info("Fetching vehicle type with id: " + id);
            return ResponseEntity.ok(vehicleTypeServiceBO.findById(id));
        } catch (VehicleTypeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createVehicleType(@RequestBody VehicleTypeRequestDto providerAccountRequest) throws VehicleTypeException {
        try {
            log.info("Creating a vehicle type");
            return ResponseEntity.ok(vehicleTypeServiceBO.save(providerAccountRequest));
        } catch (VehicleTypeException e) {
            log.error("VehicleTypeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicleType(@PathVariable Long id) throws ProviderAccountException {
        try {
            log.info("Deleting vehicle type with id: " + id);
            vehicleTypeServiceBO.isUsed(id, false);
            return ResponseEntity.ok().build();
        } catch (VehicleTypeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("active/{id}")
    public ResponseEntity<?> activeVehicleType(@PathVariable Long id) {
        try {
            log.info("Activating vehicle type with id: " + id);
            vehicleTypeServiceBO.isUsed(id, true);
            return ResponseEntity.ok().build();
        } catch (VehicleTypeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicleType(@PathVariable Long id, @RequestBody VehicleTypeRequestDto requestDto) {
        try {
            log.info("Updating vehicle type with id: " + id);
            return ResponseEntity.ok(vehicleTypeServiceBO.updateVehicleTypeName(id, requestDto));
        } catch (VehicleTypeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    private static ErrorResponse getErrorResponse(VehicleTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}