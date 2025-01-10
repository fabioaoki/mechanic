package br.com.mechanic.mechanic.service.request;

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
    private String login;
    private List<ProviderAddressRequest> addressRequest;
    private List<ProviderPhoneRequest> phoneRequest;
    private ProviderPersonRequest personRequest;
}
