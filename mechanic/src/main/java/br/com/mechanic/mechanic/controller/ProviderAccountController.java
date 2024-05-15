package br.com.mechanic.mechanic.controller;

import br.com.mechanic.mechanic.request.ProviderAccountPutRequest;
import br.com.mechanic.mechanic.request.ProviderAccountRequest;
import br.com.mechanic.mechanic.response.ProviderAccountResponse;
import br.com.mechanic.mechanic.service.ProviderAccountServiceBO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/provider-accounts")
public class ProviderAccountController {

    private static final Logger logger = LogManager.getLogger(ProviderAccountController.class);

    @Autowired
    private ProviderAccountServiceBO providerAccountServiceBO;

    @GetMapping
    public List<ProviderAccountResponse> getAllProviderAccounts() {
        return providerAccountServiceBO.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProviderAccountResponse> getProviderAccountById(@PathVariable Long id) {
        Optional<ProviderAccountResponse> providerAccount = providerAccountServiceBO.findById(id);
        if (providerAccount.isPresent()) {
            return ResponseEntity.ok(providerAccount.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ProviderAccountResponse createProviderAccount(@RequestBody ProviderAccountRequest providerAccount) {
        return providerAccountServiceBO.save(providerAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProviderAccountResponse> updateProviderAccount(@PathVariable Long id, @RequestBody ProviderAccountPutRequest providerAccountPutRequest) {
        Optional<ProviderAccountResponse> providerAccount = providerAccountServiceBO.findById(id);
        if (providerAccount.isPresent()) {
            ProviderAccountRequest updatedProviderAccount = new ProviderAccountRequest();
            updatedProviderAccount.setWorkshop(providerAccountPutRequest.getWorkshop());
            updatedProviderAccount.setCnpj(providerAccountPutRequest.getCnpj());
            updatedProviderAccount.setCreateDate(providerAccountPutRequest.getCreateDate());
            updatedProviderAccount.setLastUpdate(providerAccountPutRequest.getLastUpdate());
            updatedProviderAccount.setType(providerAccountPutRequest.getType());
            providerAccountServiceBO.save(updatedProviderAccount);
            return ResponseEntity.ok(updatedProviderAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProviderAccount(@PathVariable Long id) {
        Optional<ProviderAccountResponse> providerAccount = providerAccountServiceBO.findById(id);
        if (providerAccount.isPresent()) {
            providerAccountServiceBO.delete(providerAccount.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
