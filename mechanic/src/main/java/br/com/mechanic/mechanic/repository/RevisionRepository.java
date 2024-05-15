package br.com.mechanic.mechanic.repository;

import br.com.mechanic.mechanic.entity.Revision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {}
