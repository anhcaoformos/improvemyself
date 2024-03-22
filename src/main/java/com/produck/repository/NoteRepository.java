package com.produck.repository;

import com.produck.domain.Note;
import com.produck.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByUser(User user, Pageable pageable);

    Optional<Note> findOneByUserAndId(User user, Long id);

    void deleteByUserAndId(User user, Long id);
}
