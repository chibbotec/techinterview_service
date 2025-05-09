package com.ll.techinterview.domain.note.repository;

import com.ll.techinterview.domain.note.document.Note;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

  List<Note> findAllBySpaceIdAndPublicAccess(Long spaceId, boolean isPublic);

  List<Note> findAllBySpaceIdAndAuthor_Id(Long spaceId, Long id);

  List<Note> findBySpaceId(Long spaceId);
}
