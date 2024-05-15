package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.RevisionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionHistoryRepository extends JpaRepository<RevisionHistory, Long> {}
