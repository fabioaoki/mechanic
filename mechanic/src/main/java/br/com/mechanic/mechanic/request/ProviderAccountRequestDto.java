package br.com.mechanic.mechanic.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProviderAccountRequestDto {

    private String workshop;
    private String cnpj;
    private Long type;
    private String password;
    private List<ProviderAddressRequest> addressRequest;
    private List<ProviderPhoneRequest> phoneRequest;
    private ProviderPersonRequest personRequest;
}
