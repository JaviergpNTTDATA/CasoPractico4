package com.novabank.account.service;

import com.novabank.account.dto.MovementDTO;
import com.novabank.account.exception.AccountNotFoundException;
import com.novabank.account.exception.MovementNotFoundException;
import com.novabank.account.mapper.MovementMapper;
import com.novabank.account.model.Movement;
import com.novabank.account.repository.AccountRepository;
import com.novabank.account.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class InquiryService {

        private final MovementRepository movementRepository;
        private final AccountRepository accountRepository;

        public InquiryService(MovementRepository movementRepository,AccountRepository accountRepository) {
                this.movementRepository = movementRepository;
                this.accountRepository = accountRepository;
        }

        public List<MovementDTO> getByAccount(String iban) {

                accountRepository.findByIban(iban)
                                .orElseThrow(() -> new AccountNotFoundException(
                                                "Account not found with IBAN: " + iban));

                List<Movement> movements = movementRepository.findByAccount_ibanOrderByCreatedAtDesc(iban);

                if (movements.isEmpty()) {
                        throw new MovementNotFoundException(
                                        "No movements found for account: " + iban);
                }

                return movements.stream()
                                .map(MovementMapper::toDto)
                                .toList();
        }

        public List<MovementDTO> getByAccountAndDates(String iban,
                        LocalDate start,
                        LocalDate end) {

                accountRepository.findByIban(iban)
                                .orElseThrow(() -> new AccountNotFoundException(
                                                "Account not found with IBAN: " + iban));

                if (start.isAfter(end)) {
                        throw new IllegalArgumentException("Start date cannot be after end date");
                }

                LocalDateTime startDateTime = start.atStartOfDay();
                LocalDateTime endDateTime = end.atTime(23, 59, 59);

                List<Movement> movements = movementRepository
                                .findByAccount_ibanAndCreatedAtBetweenOrderByCreatedAtDesc(
                                                iban, startDateTime, endDateTime);

                if (movements.isEmpty()) {
                        throw new MovementNotFoundException(
                                        "No movements found for account: "
                                                        + iban + " between " + start + " and " + end);
                }

                return movements.stream()
                                .map(MovementMapper::toDto)
                                .toList();
        }
}
