package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.vehicle.Revision;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.RevisionException;
import br.com.mechanic.mechanic.mapper.RevisionMapper;
import br.com.mechanic.mechanic.repository.provider.RevisionRepositoryImpl;
import br.com.mechanic.mechanic.request.RevisionRequest;
import br.com.mechanic.mechanic.response.RevisionResponse;
import com.twilio.Twilio;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.time.LocalDate;

@AllArgsConstructor
@Log4j2
@Service
public class RevisionService implements RevisionServiceBO {

    private final RevisionRepositoryImpl revisionRepository;

    @Override
    public RevisionResponse save(RevisionRequest revisionRequest) {
        log.info("Service: Saving a new revision");
        if (revisionRequest.getMileage().compareTo(revisionRequest.getMileageForInspection()) > 0) {
            throw new RevisionException(ErrorCode.INVALID_FIELD, "Future mileage cannot be less than current mileage.");
        }
        Revision entity = RevisionMapper.MAPPER.toEntity(revisionRequest);
        validEndDate(revisionRequest.getEndDate());
        if (entity.getRevisionId() == 0) {
            entity.setRevisionId(null);
        }
        return RevisionMapper.MAPPER.toDto(revisionRepository.save(entity));
    }


    @Override
    public Page<RevisionResponse> findAll(Pageable pageable) {
        log.info("Retrieving list of revisions");
        return revisionRepository.findAll(pageable).map(RevisionMapper.MAPPER::toDto);
    }

    @Override
    public RevisionResponse findById(Long id) {
        return RevisionMapper.MAPPER.toDto(getRevision(id));
    }

    @Override
    public RevisionResponse findByCompletedServiceId(Long id) {
        return RevisionMapper.MAPPER.toDto(getRevisionByCompletedServiceId(id));
    }

    @Override
    public Page<RevisionResponse> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of revisions by providerAccount");
        return revisionRepository.findAllByProviderAccountId(pageable, providerAccountId).map(RevisionMapper.MAPPER::toDto);
    }

    @Override
    public Page<RevisionResponse> findAllByClientAccountId(Long clientAccountId, Pageable pageable) {
        log.info("Retrieving list of revisions by clientAccountId");
        return revisionRepository.findAllByClientAccountId(pageable, clientAccountId).map(RevisionMapper.MAPPER::toDto);
    }

    @Override
    public void updateRevision(Long id, LocalDate revisionReturn, boolean isFinish, long quantityRevised) {
        revisionRepository.updateReturn(id, revisionReturn, isFinish, quantityRevised);
    }

    @Override
    public void senRevisionNotification() {
        Twilio.init("ACCOUNT_SID", "AUTH_TOKEN");

        revisionRepository.findPendingRevision();

        String fromWhatsAppNumber = "whatsapp:+YOUR_TWILIO_WHATSAPP_NUMBER"; // Inclua whatsapp: antes do número
        String toWhatsAppNumber = "whatsapp:+CLIENT_NUMBER"; // Substitua CLIENT_NUMBER pelo número do cliente

        Message message = Message.creator(
                new PhoneNumber(toWhatsAppNumber),
                new PhoneNumber(fromWhatsAppNumber),
                "Olá, este é um teste de envio de mensagem via WhatsApp!"
        ).create();

        System.out.println("Message SID: " + message.getSid());
    }

    private Revision getRevision(Long id) {
        return revisionRepository.findById(id).orElseThrow(() -> new RevisionException(ErrorCode.ERROR_REVISION_NOT_FOUND, "Revision not found by id: " + id));
    }

    private Revision getRevisionByCompletedServiceId(Long id) {
        return revisionRepository.findByCompletedServiceId(id).orElseThrow(() -> new RevisionException(ErrorCode.ERROR_REVISION_NOT_FOUND, "Revision not found by id: " + id));
    }

    private static void validEndDate(LocalDate endDate) {
        LocalDate today = LocalDate.now();

        if (endDate.isBefore(today)) {
            throw new RevisionException(ErrorCode.INVALID_FIELD, "The 'endDate' cannot be in the past.");
        }
    }
}
