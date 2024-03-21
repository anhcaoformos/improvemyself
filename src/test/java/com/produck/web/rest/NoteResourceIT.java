package com.produck.web.rest;

import static com.produck.domain.NoteAsserts.*;
import static com.produck.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.produck.IntegrationTest;
import com.produck.domain.Note;
import com.produck.domain.enumeration.AlertType;
import com.produck.domain.enumeration.NoteType;
import com.produck.domain.enumeration.RepeatType;
import com.produck.repository.NoteRepository;
import com.produck.service.dto.NoteDTO;
import com.produck.service.mapper.NoteMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NoteResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_NOTE_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOTE_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_NOTE_DATE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_NOTE_DATE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final NoteType DEFAULT_NOTE_TYPE = NoteType.LIST;
    private static final NoteType UPDATED_NOTE_TYPE = NoteType.EVENT;

    private static final RepeatType DEFAULT_REPEAT_TYPE = RepeatType.EVERY_DAY;
    private static final RepeatType UPDATED_REPEAT_TYPE = RepeatType.EVERY_WEEK;

    private static final AlertType DEFAULT_ALERT_TYPE = AlertType.AT_TIME;
    private static final AlertType UPDATED_ALERT_TYPE = AlertType.BEFORE_FIVE_MINUTES;

    private static final String ENTITY_API_URL = "/api/notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteMockMvc;

    private Note note;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity(EntityManager em) {
        Note note = new Note()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .noteDateFrom(DEFAULT_NOTE_DATE_FROM)
            .noteDateTo(DEFAULT_NOTE_DATE_TO)
            .noteType(DEFAULT_NOTE_TYPE)
            .repeatType(DEFAULT_REPEAT_TYPE)
            .alertType(DEFAULT_ALERT_TYPE);
        return note;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        Note note = new Note()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .noteDateFrom(UPDATED_NOTE_DATE_FROM)
            .noteDateTo(UPDATED_NOTE_DATE_TO)
            .noteType(UPDATED_NOTE_TYPE)
            .repeatType(UPDATED_REPEAT_TYPE)
            .alertType(UPDATED_ALERT_TYPE);
        return note;
    }

    @BeforeEach
    public void initTest() {
        note = createEntity(em);
    }

    @Test
    @Transactional
    void createNote() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);
        var returnedNoteDTO = om.readValue(
            restNoteMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(noteDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NoteDTO.class
        );

        // Validate the Note in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNote = noteMapper.toEntity(returnedNoteDTO);
        assertNoteUpdatableFieldsEquals(returnedNote, getPersistedNote(returnedNote));
    }

    @Test
    @Transactional
    void createNoteWithExistingId() throws Exception {
        // Create the Note with an existing ID
        note.setId(1L);
        NoteDTO noteDTO = noteMapper.toDto(note);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList
        restNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].noteDateFrom").value(hasItem(DEFAULT_NOTE_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].noteDateTo").value(hasItem(DEFAULT_NOTE_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].noteType").value(hasItem(DEFAULT_NOTE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].repeatType").value(hasItem(DEFAULT_REPEAT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.noteDateFrom").value(DEFAULT_NOTE_DATE_FROM.toString()))
            .andExpect(jsonPath("$.noteDateTo").value(DEFAULT_NOTE_DATE_TO.toString()))
            .andExpect(jsonPath("$.noteType").value(DEFAULT_NOTE_TYPE.toString()))
            .andExpect(jsonPath("$.repeatType").value(DEFAULT_REPEAT_TYPE.toString()))
            .andExpect(jsonPath("$.alertType").value(DEFAULT_ALERT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNote are not directly saved in db
        em.detach(updatedNote);
        updatedNote
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .noteDateFrom(UPDATED_NOTE_DATE_FROM)
            .noteDateTo(UPDATED_NOTE_DATE_TO)
            .noteType(UPDATED_NOTE_TYPE)
            .repeatType(UPDATED_REPEAT_TYPE)
            .alertType(UPDATED_ALERT_TYPE);
        NoteDTO noteDTO = noteMapper.toDto(updatedNote);

        restNoteMockMvc
            .perform(put(ENTITY_API_URL_ID, noteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(noteDTO)))
            .andExpect(status().isOk());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNoteToMatchAllProperties(updatedNote);
    }

    @Test
    @Transactional
    void putNonExistingNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        note.setId(longCount.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(put(ENTITY_API_URL_ID, noteDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        note.setId(longCount.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(noteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        note.setId(longCount.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(noteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        partialUpdatedNote.noteDateFrom(UPDATED_NOTE_DATE_FROM).noteDateTo(UPDATED_NOTE_DATE_TO).alertType(UPDATED_ALERT_TYPE);

        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNote))
            )
            .andExpect(status().isOk());

        // Validate the Note in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNoteUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedNote, note), getPersistedNote(note));
    }

    @Test
    @Transactional
    void fullUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        partialUpdatedNote
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .noteDateFrom(UPDATED_NOTE_DATE_FROM)
            .noteDateTo(UPDATED_NOTE_DATE_TO)
            .noteType(UPDATED_NOTE_TYPE)
            .repeatType(UPDATED_REPEAT_TYPE)
            .alertType(UPDATED_ALERT_TYPE);

        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNote))
            )
            .andExpect(status().isOk());

        // Validate the Note in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNoteUpdatableFieldsEquals(partialUpdatedNote, getPersistedNote(partialUpdatedNote));
    }

    @Test
    @Transactional
    void patchNonExistingNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        note.setId(longCount.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, noteDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(noteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        note.setId(longCount.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(noteDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNote() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        note.setId(longCount.incrementAndGet());

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(noteDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Note in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the note
        restNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, note.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return noteRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Note getPersistedNote(Note note) {
        return noteRepository.findById(note.getId()).orElseThrow();
    }

    protected void assertPersistedNoteToMatchAllProperties(Note expectedNote) {
        assertNoteAllPropertiesEquals(expectedNote, getPersistedNote(expectedNote));
    }

    protected void assertPersistedNoteToMatchUpdatableProperties(Note expectedNote) {
        assertNoteAllUpdatablePropertiesEquals(expectedNote, getPersistedNote(expectedNote));
    }
}
