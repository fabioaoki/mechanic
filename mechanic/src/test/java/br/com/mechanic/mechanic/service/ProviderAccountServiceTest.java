//package br.com.mechanic.mechanic.service;
//
//import br.com.mechanic.mechanic.entity.ProviderAccount;
//import br.com.mechanic.mechanic.entity.ProviderAccountType;
//import br.com.mechanic.mechanic.entity.ProviderPerson;
//import br.com.mechanic.mechanic.enuns.ProviderAccountStatusEnum;
//import br.com.mechanic.mechanic.exception.ProviderAccountException;
//import br.com.mechanic.mechanic.exception.ProviderAccountTypeException;
//import br.com.mechanic.mechanic.exception.ProviderAddressException;
//import br.com.mechanic.mechanic.exception.ProviderPhoneException;
//import br.com.mechanic.mechanic.repository.*;
//import br.com.mechanic.mechanic.request.*;
//import br.com.mechanic.mechanic.response.ProviderAccountResponseDto;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//public class ProviderAccountServiceTest {
//
//    @Mock
//    private ProviderAccountRepositoryImpl providerAccountRepository;
//
//    @Mock
//    private ProviderAddressRepositoryImpl addressRepository;
//
//    @Mock
//    private ProviderPersonRepositoryImpl providerPersonRepository;
//
//    @Mock
//    private ProviderPhoneRepositoryImpl phoneRepository;
//
//    @Mock
//    private ProviderAccountTypeRepositoryImpl typeRepository;
//
//    @Mock
//    private ProviderAccountHistoryRepositoryImpl providerAccountHistoryRepository;
//
//    @InjectMocks
//    private ProviderAccountService providerAccountService;
//
//    private ProviderAccount providerAccount;
//
//    private ProviderAccount providerAccountUpdated;
//    private ProviderAccountRequestDto providerAccountRequestDto;
//    private ProviderAccountUpdateRequestDto providerAccountUpdateRequestDto;
//
//    @BeforeEach
//    void setUp() {
//        providerAccount = new ProviderAccount();
//        providerAccount.setId(1L);
//        providerAccount.setWorkshop("Test Workshop");
//        providerAccount.setCnpj("12345678000195");
//        providerAccount.setType(1L);
//        providerAccount.setStatus(ProviderAccountStatusEnum.INITIAL_BLOCK);
//
//        providerAccountUpdated = new ProviderAccount();
//        providerAccountUpdated.setId(1L);
//        providerAccountUpdated.setWorkshop("Updated Workshop");
//        providerAccountUpdated.setCnpj("12345678000195");
//        providerAccountUpdated.setType(1L);
//        providerAccountUpdated.setStatus(ProviderAccountStatusEnum.INITIAL_BLOCK);
//
//        ProviderAddressRequest addressRequest = new ProviderAddressRequest("Barueri", "Rua Adriano", "0644666");
//        List<ProviderAddressRequest> addressRequestList = new ArrayList<>();
//        addressRequestList.add(addressRequest);
//
//        ProviderPhoneRequest phoneRequest = new ProviderPhoneRequest(11L, "959949494");
//        List<ProviderPhoneRequest> phoneList = new ArrayList<>();
//        phoneList.add(phoneRequest);
//
//
//        providerAccountRequestDto = new ProviderAccountRequestDto();
//        providerAccountRequestDto.setWorkshop("Test Workshop");
//        providerAccountRequestDto.setCnpj("12.345.678/0001-95");
//        providerAccountRequestDto.setType(1L);
//        providerAccountRequestDto.setAddressRequest(addressRequestList);
//        providerAccountRequestDto.setPhoneRequest(phoneList);
//        providerAccountRequestDto.setPersonRequest(new ProviderPersonRequest("Test Person", LocalDate.of(1990, 1, 1)));
//
//        providerAccountUpdateRequestDto = new ProviderAccountUpdateRequestDto();
//        providerAccountUpdateRequestDto.setWorkshop("Updated Workshop"); // Certifique-se de que está definido corretamente
//        providerAccountUpdateRequestDto.setCnpj("12.345.678/0001-95");
//        providerAccountUpdateRequestDto.setType(2L);
//
//        // Adicione a configuração do mock para typeRepository
//        when(typeRepository.getProviderType(1L)).thenReturn(Optional.of(new ProviderAccountType()));
//        when(typeRepository.getProviderType(2L)).thenReturn(Optional.of(new ProviderAccountType()));
//    }
//
//    @Test
//    void testFindById() {
//        when(providerAccountRepository.findById(1L)).thenReturn(Optional.of(providerAccount));
//
//        ProviderAccountResponseDto response = providerAccountService.findById(1L);
//
//        assertNotNull(response);
//        assertEquals("Test Workshop", response.getWorkshop());
//    }
//
//    @Test
//    void testFindAll() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<ProviderAccount> page = new PageImpl<>(Collections.singletonList(providerAccount));
//        when(providerAccountRepository.findAll(pageable)).thenReturn(page);
//
//        Page<ProviderAccountResponseDto> responsePage = providerAccountService.findAll(pageable);
//
//        assertNotNull(responsePage);
//        assertEquals(1, responsePage.getTotalElements());
//        assertEquals("Test Workshop", responsePage.getContent().get(0).getWorkshop());
//    }
//
//    @Test
//    void testSave() throws ProviderAccountException, ProviderAddressException, ProviderPhoneException, ProviderAccountTypeException {
//        // Configuração do mock para typeRepository
//        when(typeRepository.getProviderType(1L)).thenReturn(Optional.of(new ProviderAccountType()));
//
//        // Configuração do mock para providerAccountRepository
//        when(providerAccountRepository.save(any(ProviderAccount.class))).thenReturn(providerAccount);
//
//        // Configuração do mock para providerPersonRepository
//        ProviderPerson providerPerson = new ProviderPerson();
//        providerPerson.setId(1L);  // Defina um ID não nulo
//        providerPerson.setName("Test Person");
//        providerPerson.setBirthDate(LocalDate.of(1990, 1, 1));
//        providerPerson.setProviderAccountId(providerAccount.getId());
//        when(providerPersonRepository.save(any(ProviderPerson.class))).thenReturn(providerPerson);
//
//        // Execute o método de teste
//        ProviderAccountResponseDto response = providerAccountService.save(providerAccountRequestDto);
//
//        assertNotNull(response);
//        assertEquals("Test Workshop", response.getWorkshop());
//    }
//
//    @Test
//    void testChangeStatus() throws ProviderAccountException {
//        when(providerAccountRepository.findById(1L)).thenReturn(Optional.of(providerAccount));
//
//        providerAccountService.changeStatus(1L, ProviderAccountStatusEnum.ACTIVE);
//
//        verify(providerAccountRepository, times(1)).save(any(ProviderAccount.class));
//    }
//
//    @Test
//    void testUpdateProviderAccount() throws ProviderAccountException {
//        when(providerAccountRepository.findById(1L)).thenReturn(Optional.of(providerAccount));
//        when(providerAccountRepository.save(any(ProviderAccount.class))).thenReturn(providerAccountUpdated);
//
//        // Adicione a configuração do mock para typeRepository com o ID 2
//        when(typeRepository.getProviderType(2L)).thenReturn(Optional.of(new ProviderAccountType()));
//
//        ProviderAccountResponseDto response = providerAccountService.updateProviderAccount(1L, providerAccountUpdateRequestDto);
//
//        assertNotNull(response);
//        assertEquals("Updated Workshop", response.getWorkshop());
//    }
//
//
//    @Test
//    void testValidationsThroughPublicMethods() {
//        // Configurações comuns
//        when(typeRepository.getProviderType(1L)).thenReturn(Optional.of(new ProviderAccountType()));
//        when(typeRepository.getProviderType(2L)).thenReturn(Optional.of(new ProviderAccountType()));
//        when(providerAccountRepository.save(any(ProviderAccount.class))).thenReturn(providerAccount);
//
//        // Mock para providerPersonRepository
//        ProviderPerson providerPerson = new ProviderPerson();
//        providerPerson.setId(1L);  // Defina um ID não nulo
//        providerPerson.setName("Test Person");
//        providerPerson.setBirthDate(LocalDate.of(1990, 1, 1));
//        providerPerson.setProviderAccountId(providerAccount.getId());
//        when(providerPersonRepository.save(any(ProviderPerson.class))).thenReturn(providerPerson);
//
//        // Testa validField através do método save
//        assertDoesNotThrow(() -> providerAccountService.save(providerAccountRequestDto));
//
//        // Mock para providerAccountRepository.findById
//        when(providerAccountRepository.findById(1L)).thenReturn(Optional.of(providerAccount));
//
//        // Testa validUpdateField através do método updateProviderAccount
//        assertDoesNotThrow(() -> providerAccountService.updateProviderAccount(1L, providerAccountUpdateRequestDto));
//    }
//
//}