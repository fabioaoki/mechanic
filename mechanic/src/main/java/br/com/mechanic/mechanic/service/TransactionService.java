package br.com.mechanic.mechanic.service;

import br.com.mechanic.mechanic.entity.provider.Transaction;
import br.com.mechanic.mechanic.exception.ErrorCode;
import br.com.mechanic.mechanic.exception.TransactionException;
import br.com.mechanic.mechanic.mapper.TransactionMapper;
import br.com.mechanic.mechanic.repository.provider.TransactionRepositoryImpl;
import br.com.mechanic.mechanic.request.TransactionRequest;
import br.com.mechanic.mechanic.response.TransactionResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@AllArgsConstructor
@Log4j2
@Service
public class TransactionService implements TransactionServiceBO {

    private final TransactionRepositoryImpl transactionRepository;

    @Override
    public TransactionResponse save(TransactionRequest requestDto) {
        log.info("Service: Save transaction by providerAccountId: {} clientAccountId: {}", requestDto.getProviderAccountId(), requestDto.getClientAccountId());
        return TransactionMapper.MAPPER.toDto(transactionRepository.save(TransactionMapper.MAPPER.toEntity(requestDto)));
    }

    @Override
    public Page<TransactionResponse> findAll(Pageable pageable) {
        log.info("Retrieving list of transactions");
        return transactionRepository.findAll(pageable).map(TransactionMapper.MAPPER::toDto);
    }

    @Override
    public TransactionResponse findById(Long id) {
        return TransactionMapper.MAPPER.toDto(getTransactionById(id));
    }

    @Override
    public Page<TransactionResponse> findAllByProviderAccountId(Long providerAccountId, Pageable pageable) {
        log.info("Retrieving list of transaction by providerAccount: {}", providerAccountId);
        return transactionRepository.findAllByProviderAccountId(pageable, providerAccountId).map(TransactionMapper.MAPPER::toDto);
    }

    @Override
    public Page<TransactionResponse> findAllByClientAccountId(Long clientAccountId, Pageable pageable) {
        log.info("Retrieving list of transaction by clientAccountId: {}", clientAccountId);
        return transactionRepository.findAllByClientAccountId(pageable, clientAccountId).map(TransactionMapper.MAPPER::toDto);
    }

    @Override
    public void reversal(BigDecimal amount, BigDecimal workmanshipAmount, Long id) {
        log.info("reversal transaction by providerAccount : {}", id);
        transactionRepository.reversal(id, amount, workmanshipAmount);
    }

    private Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionException(ErrorCode.TRANSACTION_ID_NOT_FOUND, "Transaction not found by id: " + id));
    }
}