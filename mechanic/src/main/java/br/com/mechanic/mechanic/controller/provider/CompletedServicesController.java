package br.com.mechanic.mechanic.controller.provider;

import br.com.mechanic.mechanic.exception.*;
import br.com.mechanic.mechanic.request.CompletedServiceRequest;
import br.com.mechanic.mechanic.request.ReversalCompletedServiceRequest;
import br.com.mechanic.mechanic.service.CompletedServiceManagerBO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@Log4j2
@RequestMapping("/completed-service")
public class CompletedServicesController {

    @Autowired
    private CompletedServiceManagerBO completedServiceManagerBO;

    @GetMapping
    public ResponseEntity<Page<?>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(completedServiceManagerBO.findAll(PageRequest.of(page, size)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createCompletedService(
            @RequestBody CompletedServiceRequest completedServiceRequest) throws CompletedServiceException, ProviderAccountException {
        try {
            log.info("Creating a completed service");
            return ResponseEntity.ok(completedServiceManagerBO.save(completedServiceRequest));
        } catch (CompletedServiceException e) {
            log.error("CompletedServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            log.error("ProviderAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (EmployeeAccountException e) {
            log.error("EmployeeAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderServiceException e) {
            log.error("ProviderServiceException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ModelException e) {
            log.error("ModelException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (PlateException e) {
            log.error("PlateException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ColorException e) {
            log.error("ColorException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (EquipmentException e) {
            log.error("EquipmentException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (TransactionException e) {
            log.error("TransactionException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ClientAccountException e) {
            log.error("ClientAccountException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (RevisionException e) {
            log.error("RevisionException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (VehicleTypeException e) {
            log.error("VehicleTypeException: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    @GetMapping("/provider/{providerAccountId}/service-count-by-employee")
    public ResponseEntity<?> getProviderServiceByEmployee(@PathVariable Long providerAccountId,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Fetching service count for providerAccountId: {}, from: {}, to: {} by employee", providerAccountId, startDate, endDate);
            return ResponseEntity.ok(completedServiceManagerBO.getCompletedServiceCountByEmployee(providerAccountId, startDate, endDate));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @PostMapping("/provider/{providerAccountId}/reversal")
    public void reversalCompletedService(@PathVariable Long providerAccountId, @RequestBody ReversalCompletedServiceRequest reversalRequest) {
        try {
            log.info("Reversal completed service by providerAccountId: {}", providerAccountId);
            completedServiceManagerBO.reversalCompletedService(providerAccountId, reversalRequest);
        } catch (CompletedServiceException e) {
            log.error("Error during reversal service: ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Completed service not found", e);
        } catch (ProviderAccountException e) {
            log.error("Provider account issue: ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Provider account issue", e);
        } catch (EquipmentException e) {
            log.error("Equipment issue: ", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment not found", e);
        }
    }

    @GetMapping("/provider/{providerAccountId}/service-count-by-provider-service")
    public ResponseEntity<?> getProviderServiceByProviderAccount(@PathVariable Long providerAccountId,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Fetching service count for providerAccountId: {}, from: {}, to: {}", providerAccountId, startDate, endDate);
            return ResponseEntity.ok(completedServiceManagerBO.getCompletedServiceCountByProviderService(providerAccountId, startDate, endDate));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }
    @GetMapping("/provider/{providerAccountId}/service-count-by-provider-service-group-by-date")
    public ResponseEntity<?> getProviderServiceByProviderAccountGroupByDate(@PathVariable Long providerAccountId,
                                                                 @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                 @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Fetching service count for providerAccountId: {}, from: {}, to: {}, group by date", providerAccountId, startDate, endDate);
            return ResponseEntity.ok(completedServiceManagerBO.countFirstCompletedServiceByProviderServiceGroupByDate(providerAccountId, startDate, endDate));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/provider/{providerAccountId}/service-count-by-provider-service-completed-group-by-date")
    public ResponseEntity<?> getProviderCompletedServiceByProviderAccountGroupByDate(@PathVariable Long providerAccountId,
                                                                            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            log.info("Fetching service count for providerAccountId: {}, from: {}, to: {}, group by date", providerAccountId, startDate, endDate);
            return ResponseEntity.ok(completedServiceManagerBO.countCompletedServicesByVehicleTypeIdAndOptionalDate(providerAccountId, startDate, endDate));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentInById(@PathVariable Long id) {
        try {
            log.info("Fetching equipmentIn with id: {}", id);
            return ResponseEntity.ok(completedServiceManagerBO.findById(id));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorResponse(e));
        }
    }

    @GetMapping("/provider-account/{provider-account-id}")
    public ResponseEntity<?> findAllByProviderAccountId(
            @PathVariable("provider-account-id") Long providerAccountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            return ResponseEntity.ok(completedServiceManagerBO.findAllByProviderAccountId(providerAccountId, PageRequest.of(page, size), startDate, endDate));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }
    @GetMapping("/provider-account/{provider-account-id}/reversal")
    public ResponseEntity<?> findAllByProviderAccountIdReversal(
            @PathVariable("provider-account-id") Long providerAccountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            return ResponseEntity.ok(completedServiceManagerBO.findAllReversalByProviderAccountId(providerAccountId, PageRequest.of(page, size), startDate, endDate));
        } catch (CompletedServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        } catch (ProviderAccountException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getErrorResponse(e));
        }
    }

    // Services by Month
    @GetMapping("/provider/{providerAccountId}/services-by-month")
    public ResponseEntity<?> getServicesByMonth(@PathVariable Long providerAccountId,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var services = completedServiceManagerBO.getServicesByMonth(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(services);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching services by month data");
        }
    }

    // Revenue by Service Type
    @GetMapping("/provider/{providerAccountId}/revenue-by-service-type")
    public ResponseEntity<?> getRevenueByServiceType(@PathVariable Long providerAccountId,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var revenue = completedServiceManagerBO.getRevenueByServiceType(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(revenue);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching revenue by service type");
        }
    }

    // Equipment Utilization Report
    @GetMapping("/provider/{providerAccountId}/equipment-utilization")
    public ResponseEntity<?> getEquipmentUtilizationReport(@PathVariable Long providerAccountId,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var utilization = completedServiceManagerBO.getEquipmentUtilizationReport(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(utilization);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching equipment utilization data");
        }
    }

    // Employee Efficiency Report
    @GetMapping("/provider/{providerAccountId}/employee-efficiency")
    public ResponseEntity<?> getEmployeeEfficiencyReport(@PathVariable Long providerAccountId,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var efficiency = completedServiceManagerBO.getEmployeeEfficiencyReport(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(efficiency);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching employee efficiency data");
        }
    }

    // Labor Costs by Service Type
    @GetMapping("/provider/{providerAccountId}/labor-costs-by-service-type")
    public ResponseEntity<?> getLaborCostsByServiceType(@PathVariable Long providerAccountId,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var laborCosts = completedServiceManagerBO.getLaborCostsByServiceType(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(laborCosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching labor costs by service type");
        }
    }

    // Equipment Costs by Service Type
    @GetMapping("/provider/{providerAccountId}/equipment-costs-by-service-type")
    public ResponseEntity<?> getEquipmentCostsByServiceType(@PathVariable Long providerAccountId,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var equipmentCosts = completedServiceManagerBO.getEquipmentCostsByServiceType(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(equipmentCosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching equipment costs by service type");
        }
    }

    // Cost Revenue Comparison
    @GetMapping("/provider/{providerAccountId}/cost-revenue-comparison")
    public ResponseEntity<?> getCostRevenueComparison(@PathVariable Long providerAccountId,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var comparison = completedServiceManagerBO.getCostRevenueComparison(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(comparison);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching cost revenue comparison");
        }
    }

    // Inventory Efficiency Report
    @GetMapping("/provider/{providerAccountId}/inventory-efficiency")
    public ResponseEntity<?> getInventoryEfficiencyReport(@PathVariable Long providerAccountId,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var efficiency = completedServiceManagerBO.getInventoryEfficiencyReport(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(efficiency);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching inventory efficiency data");
        }
    }

    // Maintenance and Revisions Report
    @GetMapping("/provider/{providerAccountId}/maintenance-revisions-report")
    public ResponseEntity<?> getMaintenanceAndRevisionsReport(@PathVariable Long providerAccountId,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            var report = completedServiceManagerBO.getMaintenanceAndRevisionsReport(providerAccountId, startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching maintenance and revisions report");
        }
    }

    private static ErrorResponse getErrorResponse(CompletedServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(EmployeeAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ProviderServiceException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(VehicleTypeException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ColorException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(PlateException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ModelException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(EquipmentException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(TransactionException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(ClientAccountException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }

    private static ErrorResponse getErrorResponse(RevisionException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(e.getErrorCode());
        errorResponse.setMessage(e.getMessage());
        return errorResponse;
    }
}