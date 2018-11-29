package com.gatech.peduc.web.rest;

import com.gatech.peduc.Peduc2App;

import com.gatech.peduc.domain.LectureActivity;
import com.gatech.peduc.repository.LectureActivityRepository;
import com.gatech.peduc.repository.search.LectureActivitySearchRepository;
import com.gatech.peduc.service.LectureActivityService;
import com.gatech.peduc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static com.gatech.peduc.web.rest.TestUtil.sameInstant;
import static com.gatech.peduc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gatech.peduc.domain.enumeration.LectureStatus;
/**
 * Test class for the LectureActivityResource REST controller.
 *
 * @see LectureActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Peduc2App.class)
public class LectureActivityResourceIntTest {

    private static final Long DEFAULT_PRESENTING_USER_ID = 1L;
    private static final Long UPDATED_PRESENTING_USER_ID = 2L;

    private static final Long DEFAULT_ATENDING_USER_ID = 1L;
    private static final Long UPDATED_ATENDING_USER_ID = 2L;

    private static final LectureStatus DEFAULT_LECTURE_STATUS = LectureStatus.ACTIVE;
    private static final LectureStatus UPDATED_LECTURE_STATUS = LectureStatus.SCORE;

    private static final ZonedDateTime DEFAULT_PRESENTATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PRESENTATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_POSTED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_POSTED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LectureActivityRepository lectureActivityRepository;

    @Mock
    private LectureActivityRepository lectureActivityRepositoryMock;
    

    @Mock
    private LectureActivityService lectureActivityServiceMock;

    @Autowired
    private LectureActivityService lectureActivityService;

    /**
     * This repository is mocked in the com.gatech.peduc.repository.search test package.
     *
     * @see com.gatech.peduc.repository.search.LectureActivitySearchRepositoryMockConfiguration
     */
    @Autowired
    private LectureActivitySearchRepository mockLectureActivitySearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLectureActivityMockMvc;

    private LectureActivity lectureActivity;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LectureActivityResource lectureActivityResource = new LectureActivityResource(lectureActivityService);
        this.restLectureActivityMockMvc = MockMvcBuilders.standaloneSetup(lectureActivityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LectureActivity createEntity(EntityManager em) {
        LectureActivity lectureActivity = new LectureActivity()
            .presentingUserId(DEFAULT_PRESENTING_USER_ID)
            .atendingUserId(DEFAULT_ATENDING_USER_ID)
            .lectureStatus(DEFAULT_LECTURE_STATUS)
            .presentationDate(DEFAULT_PRESENTATION_DATE)
            .postedDate(DEFAULT_POSTED_DATE);
        return lectureActivity;
    }

    @Before
    public void initTest() {
        lectureActivity = createEntity(em);
    }

    @Test
    @Transactional
    public void createLectureActivity() throws Exception {
        int databaseSizeBeforeCreate = lectureActivityRepository.findAll().size();

        // Create the LectureActivity
        restLectureActivityMockMvc.perform(post("/api/lecture-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureActivity)))
            .andExpect(status().isCreated());

        // Validate the LectureActivity in the database
        List<LectureActivity> lectureActivityList = lectureActivityRepository.findAll();
        assertThat(lectureActivityList).hasSize(databaseSizeBeforeCreate + 1);
        LectureActivity testLectureActivity = lectureActivityList.get(lectureActivityList.size() - 1);
        assertThat(testLectureActivity.getPresentingUserId()).isEqualTo(DEFAULT_PRESENTING_USER_ID);
        assertThat(testLectureActivity.getAtendingUserId()).isEqualTo(DEFAULT_ATENDING_USER_ID);
        assertThat(testLectureActivity.getLectureStatus()).isEqualTo(DEFAULT_LECTURE_STATUS);
        assertThat(testLectureActivity.getPresentationDate()).isEqualTo(DEFAULT_PRESENTATION_DATE);
        assertThat(testLectureActivity.getPostedDate()).isEqualTo(DEFAULT_POSTED_DATE);

        // Validate the LectureActivity in Elasticsearch
        verify(mockLectureActivitySearchRepository, times(1)).save(testLectureActivity);
    }

    @Test
    @Transactional
    public void createLectureActivityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lectureActivityRepository.findAll().size();

        // Create the LectureActivity with an existing ID
        lectureActivity.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLectureActivityMockMvc.perform(post("/api/lecture-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureActivity)))
            .andExpect(status().isBadRequest());

        // Validate the LectureActivity in the database
        List<LectureActivity> lectureActivityList = lectureActivityRepository.findAll();
        assertThat(lectureActivityList).hasSize(databaseSizeBeforeCreate);

        // Validate the LectureActivity in Elasticsearch
        verify(mockLectureActivitySearchRepository, times(0)).save(lectureActivity);
    }

    @Test
    @Transactional
    public void getAllLectureActivities() throws Exception {
        // Initialize the database
        lectureActivityRepository.saveAndFlush(lectureActivity);

        // Get all the lectureActivityList
        restLectureActivityMockMvc.perform(get("/api/lecture-activities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lectureActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].presentingUserId").value(hasItem(DEFAULT_PRESENTING_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].atendingUserId").value(hasItem(DEFAULT_ATENDING_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].lectureStatus").value(hasItem(DEFAULT_LECTURE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(sameInstant(DEFAULT_PRESENTATION_DATE))))
            .andExpect(jsonPath("$.[*].postedDate").value(hasItem(sameInstant(DEFAULT_POSTED_DATE))));
    }
    
    public void getAllLectureActivitiesWithEagerRelationshipsIsEnabled() throws Exception {
        LectureActivityResource lectureActivityResource = new LectureActivityResource(lectureActivityServiceMock);
        when(lectureActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restLectureActivityMockMvc = MockMvcBuilders.standaloneSetup(lectureActivityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLectureActivityMockMvc.perform(get("/api/lecture-activities?eagerload=true"))
        .andExpect(status().isOk());

        verify(lectureActivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    public void getAllLectureActivitiesWithEagerRelationshipsIsNotEnabled() throws Exception {
        LectureActivityResource lectureActivityResource = new LectureActivityResource(lectureActivityServiceMock);
            when(lectureActivityServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restLectureActivityMockMvc = MockMvcBuilders.standaloneSetup(lectureActivityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restLectureActivityMockMvc.perform(get("/api/lecture-activities?eagerload=true"))
        .andExpect(status().isOk());

            verify(lectureActivityServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getLectureActivity() throws Exception {
        // Initialize the database
        lectureActivityRepository.saveAndFlush(lectureActivity);

        // Get the lectureActivity
        restLectureActivityMockMvc.perform(get("/api/lecture-activities/{id}", lectureActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lectureActivity.getId().intValue()))
            .andExpect(jsonPath("$.presentingUserId").value(DEFAULT_PRESENTING_USER_ID.intValue()))
            .andExpect(jsonPath("$.atendingUserId").value(DEFAULT_ATENDING_USER_ID.intValue()))
            .andExpect(jsonPath("$.lectureStatus").value(DEFAULT_LECTURE_STATUS.toString()))
            .andExpect(jsonPath("$.presentationDate").value(sameInstant(DEFAULT_PRESENTATION_DATE)))
            .andExpect(jsonPath("$.postedDate").value(sameInstant(DEFAULT_POSTED_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingLectureActivity() throws Exception {
        // Get the lectureActivity
        restLectureActivityMockMvc.perform(get("/api/lecture-activities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLectureActivity() throws Exception {
        // Initialize the database
        lectureActivityService.save(lectureActivity);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockLectureActivitySearchRepository);

        int databaseSizeBeforeUpdate = lectureActivityRepository.findAll().size();

        // Update the lectureActivity
        LectureActivity updatedLectureActivity = lectureActivityRepository.findById(lectureActivity.getId()).get();
        // Disconnect from session so that the updates on updatedLectureActivity are not directly saved in db
        em.detach(updatedLectureActivity);
        updatedLectureActivity
            .presentingUserId(UPDATED_PRESENTING_USER_ID)
            .atendingUserId(UPDATED_ATENDING_USER_ID)
            .lectureStatus(UPDATED_LECTURE_STATUS)
            .presentationDate(UPDATED_PRESENTATION_DATE)
            .postedDate(UPDATED_POSTED_DATE);

        restLectureActivityMockMvc.perform(put("/api/lecture-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLectureActivity)))
            .andExpect(status().isOk());

        // Validate the LectureActivity in the database
        List<LectureActivity> lectureActivityList = lectureActivityRepository.findAll();
        assertThat(lectureActivityList).hasSize(databaseSizeBeforeUpdate);
        LectureActivity testLectureActivity = lectureActivityList.get(lectureActivityList.size() - 1);
        assertThat(testLectureActivity.getPresentingUserId()).isEqualTo(UPDATED_PRESENTING_USER_ID);
        assertThat(testLectureActivity.getAtendingUserId()).isEqualTo(UPDATED_ATENDING_USER_ID);
        assertThat(testLectureActivity.getLectureStatus()).isEqualTo(UPDATED_LECTURE_STATUS);
        assertThat(testLectureActivity.getPresentationDate()).isEqualTo(UPDATED_PRESENTATION_DATE);
        assertThat(testLectureActivity.getPostedDate()).isEqualTo(UPDATED_POSTED_DATE);

        // Validate the LectureActivity in Elasticsearch
        verify(mockLectureActivitySearchRepository, times(1)).save(testLectureActivity);
    }

    @Test
    @Transactional
    public void updateNonExistingLectureActivity() throws Exception {
        int databaseSizeBeforeUpdate = lectureActivityRepository.findAll().size();

        // Create the LectureActivity

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLectureActivityMockMvc.perform(put("/api/lecture-activities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lectureActivity)))
            .andExpect(status().isBadRequest());

        // Validate the LectureActivity in the database
        List<LectureActivity> lectureActivityList = lectureActivityRepository.findAll();
        assertThat(lectureActivityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LectureActivity in Elasticsearch
        verify(mockLectureActivitySearchRepository, times(0)).save(lectureActivity);
    }

    @Test
    @Transactional
    public void deleteLectureActivity() throws Exception {
        // Initialize the database
        lectureActivityService.save(lectureActivity);

        int databaseSizeBeforeDelete = lectureActivityRepository.findAll().size();

        // Get the lectureActivity
        restLectureActivityMockMvc.perform(delete("/api/lecture-activities/{id}", lectureActivity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<LectureActivity> lectureActivityList = lectureActivityRepository.findAll();
        assertThat(lectureActivityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LectureActivity in Elasticsearch
        verify(mockLectureActivitySearchRepository, times(1)).deleteById(lectureActivity.getId());
    }

    @Test
    @Transactional
    public void searchLectureActivity() throws Exception {
        // Initialize the database
        lectureActivityService.save(lectureActivity);
        when(mockLectureActivitySearchRepository.search(queryStringQuery("id:" + lectureActivity.getId())))
            .thenReturn(Collections.singletonList(lectureActivity));
        // Search the lectureActivity
        restLectureActivityMockMvc.perform(get("/api/_search/lecture-activities?query=id:" + lectureActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lectureActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].presentingUserId").value(hasItem(DEFAULT_PRESENTING_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].atendingUserId").value(hasItem(DEFAULT_ATENDING_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].lectureStatus").value(hasItem(DEFAULT_LECTURE_STATUS.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(sameInstant(DEFAULT_PRESENTATION_DATE))))
            .andExpect(jsonPath("$.[*].postedDate").value(hasItem(sameInstant(DEFAULT_POSTED_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LectureActivity.class);
        LectureActivity lectureActivity1 = new LectureActivity();
        lectureActivity1.setId(1L);
        LectureActivity lectureActivity2 = new LectureActivity();
        lectureActivity2.setId(lectureActivity1.getId());
        assertThat(lectureActivity1).isEqualTo(lectureActivity2);
        lectureActivity2.setId(2L);
        assertThat(lectureActivity1).isNotEqualTo(lectureActivity2);
        lectureActivity1.setId(null);
        assertThat(lectureActivity1).isNotEqualTo(lectureActivity2);
    }
}
