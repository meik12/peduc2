package com.gatech.peduc.web.rest;

import com.gatech.peduc.Peduc2App;

import com.gatech.peduc.domain.SuggestedLecture;
import com.gatech.peduc.repository.SuggestedLectureRepository;
import com.gatech.peduc.repository.search.SuggestedLectureSearchRepository;
import com.gatech.peduc.service.SuggestedLectureService;
import com.gatech.peduc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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

/**
 * Test class for the SuggestedLectureResource REST controller.
 *
 * @see SuggestedLectureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Peduc2App.class)
public class SuggestedLectureResourceIntTest {

    private static final byte[] DEFAULT_PROFILE_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PROFILE_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PROFILE_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PROFILE_PICTURE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVERAGE_SCORE = 1;
    private static final Integer UPDATED_AVERAGE_SCORE = 2;

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_VIDEO_CALL_LINK = "AAAAAAAAAA";
    private static final String UPDATED_VIDEO_CALL_LINK = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_PRESENTATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PRESENTATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_TIME_ZONE = "AAAAAAAAAA";
    private static final String UPDATED_TIME_ZONE = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUBLICATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUBLICATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private SuggestedLectureRepository suggestedLectureRepository;
    
    @Autowired
    private SuggestedLectureService suggestedLectureService;

    /**
     * This repository is mocked in the com.gatech.peduc.repository.search test package.
     *
     * @see com.gatech.peduc.repository.search.SuggestedLectureSearchRepositoryMockConfiguration
     */
    @Autowired
    private SuggestedLectureSearchRepository mockSuggestedLectureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSuggestedLectureMockMvc;

    private SuggestedLecture suggestedLecture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SuggestedLectureResource suggestedLectureResource = new SuggestedLectureResource(suggestedLectureService);
        this.restSuggestedLectureMockMvc = MockMvcBuilders.standaloneSetup(suggestedLectureResource)
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
    public static SuggestedLecture createEntity(EntityManager em) {
        SuggestedLecture suggestedLecture = new SuggestedLecture()
            .profilePicture(DEFAULT_PROFILE_PICTURE)
            .profilePictureContentType(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .averageScore(DEFAULT_AVERAGE_SCORE)
            .category(DEFAULT_CATEGORY)
            .title(DEFAULT_TITLE)
            .duration(DEFAULT_DURATION)
            .language(DEFAULT_LANGUAGE)
            .videoCallLink(DEFAULT_VIDEO_CALL_LINK)
            .presentationDate(DEFAULT_PRESENTATION_DATE)
            .timeZone(DEFAULT_TIME_ZONE)
            .publicationDate(DEFAULT_PUBLICATION_DATE);
        return suggestedLecture;
    }

    @Before
    public void initTest() {
        suggestedLecture = createEntity(em);
    }

    @Test
    @Transactional
    public void createSuggestedLecture() throws Exception {
        int databaseSizeBeforeCreate = suggestedLectureRepository.findAll().size();

        // Create the SuggestedLecture
        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isCreated());

        // Validate the SuggestedLecture in the database
        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeCreate + 1);
        SuggestedLecture testSuggestedLecture = suggestedLectureList.get(suggestedLectureList.size() - 1);
        assertThat(testSuggestedLecture.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
        assertThat(testSuggestedLecture.getProfilePictureContentType()).isEqualTo(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE);
        assertThat(testSuggestedLecture.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSuggestedLecture.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSuggestedLecture.getAverageScore()).isEqualTo(DEFAULT_AVERAGE_SCORE);
        assertThat(testSuggestedLecture.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testSuggestedLecture.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testSuggestedLecture.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testSuggestedLecture.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testSuggestedLecture.getVideoCallLink()).isEqualTo(DEFAULT_VIDEO_CALL_LINK);
        assertThat(testSuggestedLecture.getPresentationDate()).isEqualTo(DEFAULT_PRESENTATION_DATE);
        assertThat(testSuggestedLecture.getTimeZone()).isEqualTo(DEFAULT_TIME_ZONE);
        assertThat(testSuggestedLecture.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);

        // Validate the SuggestedLecture in Elasticsearch
        verify(mockSuggestedLectureSearchRepository, times(1)).save(testSuggestedLecture);
    }

    @Test
    @Transactional
    public void createSuggestedLectureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = suggestedLectureRepository.findAll().size();

        // Create the SuggestedLecture with an existing ID
        suggestedLecture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        // Validate the SuggestedLecture in the database
        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeCreate);

        // Validate the SuggestedLecture in Elasticsearch
        verify(mockSuggestedLectureSearchRepository, times(0)).save(suggestedLecture);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setFirstName(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setLastName(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setCategory(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setTitle(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setDuration(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setLanguage(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresentationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = suggestedLectureRepository.findAll().size();
        // set the field null
        suggestedLecture.setPresentationDate(null);

        // Create the SuggestedLecture, which fails.

        restSuggestedLectureMockMvc.perform(post("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuggestedLectures() throws Exception {
        // Initialize the database
        suggestedLectureRepository.saveAndFlush(suggestedLecture);

        // Get all the suggestedLectureList
        restSuggestedLectureMockMvc.perform(get("/api/suggested-lectures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestedLecture.getId().intValue())))
            .andExpect(jsonPath("$.[*].profilePictureContentType").value(hasItem(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE))))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].averageScore").value(hasItem(DEFAULT_AVERAGE_SCORE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].videoCallLink").value(hasItem(DEFAULT_VIDEO_CALL_LINK.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(sameInstant(DEFAULT_PRESENTATION_DATE))))
            .andExpect(jsonPath("$.[*].timeZone").value(hasItem(DEFAULT_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getSuggestedLecture() throws Exception {
        // Initialize the database
        suggestedLectureRepository.saveAndFlush(suggestedLecture);

        // Get the suggestedLecture
        restSuggestedLectureMockMvc.perform(get("/api/suggested-lectures/{id}", suggestedLecture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(suggestedLecture.getId().intValue()))
            .andExpect(jsonPath("$.profilePictureContentType").value(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePicture").value(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE)))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.averageScore").value(DEFAULT_AVERAGE_SCORE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.videoCallLink").value(DEFAULT_VIDEO_CALL_LINK.toString()))
            .andExpect(jsonPath("$.presentationDate").value(sameInstant(DEFAULT_PRESENTATION_DATE)))
            .andExpect(jsonPath("$.timeZone").value(DEFAULT_TIME_ZONE.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSuggestedLecture() throws Exception {
        // Get the suggestedLecture
        restSuggestedLectureMockMvc.perform(get("/api/suggested-lectures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuggestedLecture() throws Exception {
        // Initialize the database
        suggestedLectureService.save(suggestedLecture);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockSuggestedLectureSearchRepository);

        int databaseSizeBeforeUpdate = suggestedLectureRepository.findAll().size();

        // Update the suggestedLecture
        SuggestedLecture updatedSuggestedLecture = suggestedLectureRepository.findById(suggestedLecture.getId()).get();
        // Disconnect from session so that the updates on updatedSuggestedLecture are not directly saved in db
        em.detach(updatedSuggestedLecture);
        updatedSuggestedLecture
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .profilePictureContentType(UPDATED_PROFILE_PICTURE_CONTENT_TYPE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .averageScore(UPDATED_AVERAGE_SCORE)
            .category(UPDATED_CATEGORY)
            .title(UPDATED_TITLE)
            .duration(UPDATED_DURATION)
            .language(UPDATED_LANGUAGE)
            .videoCallLink(UPDATED_VIDEO_CALL_LINK)
            .presentationDate(UPDATED_PRESENTATION_DATE)
            .timeZone(UPDATED_TIME_ZONE)
            .publicationDate(UPDATED_PUBLICATION_DATE);

        restSuggestedLectureMockMvc.perform(put("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSuggestedLecture)))
            .andExpect(status().isOk());

        // Validate the SuggestedLecture in the database
        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeUpdate);
        SuggestedLecture testSuggestedLecture = suggestedLectureList.get(suggestedLectureList.size() - 1);
        assertThat(testSuggestedLecture.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
        assertThat(testSuggestedLecture.getProfilePictureContentType()).isEqualTo(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);
        assertThat(testSuggestedLecture.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSuggestedLecture.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSuggestedLecture.getAverageScore()).isEqualTo(UPDATED_AVERAGE_SCORE);
        assertThat(testSuggestedLecture.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testSuggestedLecture.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testSuggestedLecture.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testSuggestedLecture.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testSuggestedLecture.getVideoCallLink()).isEqualTo(UPDATED_VIDEO_CALL_LINK);
        assertThat(testSuggestedLecture.getPresentationDate()).isEqualTo(UPDATED_PRESENTATION_DATE);
        assertThat(testSuggestedLecture.getTimeZone()).isEqualTo(UPDATED_TIME_ZONE);
        assertThat(testSuggestedLecture.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);

        // Validate the SuggestedLecture in Elasticsearch
        verify(mockSuggestedLectureSearchRepository, times(1)).save(testSuggestedLecture);
    }

    @Test
    @Transactional
    public void updateNonExistingSuggestedLecture() throws Exception {
        int databaseSizeBeforeUpdate = suggestedLectureRepository.findAll().size();

        // Create the SuggestedLecture

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSuggestedLectureMockMvc.perform(put("/api/suggested-lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(suggestedLecture)))
            .andExpect(status().isBadRequest());

        // Validate the SuggestedLecture in the database
        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SuggestedLecture in Elasticsearch
        verify(mockSuggestedLectureSearchRepository, times(0)).save(suggestedLecture);
    }

    @Test
    @Transactional
    public void deleteSuggestedLecture() throws Exception {
        // Initialize the database
        suggestedLectureService.save(suggestedLecture);

        int databaseSizeBeforeDelete = suggestedLectureRepository.findAll().size();

        // Get the suggestedLecture
        restSuggestedLectureMockMvc.perform(delete("/api/suggested-lectures/{id}", suggestedLecture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SuggestedLecture> suggestedLectureList = suggestedLectureRepository.findAll();
        assertThat(suggestedLectureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SuggestedLecture in Elasticsearch
        verify(mockSuggestedLectureSearchRepository, times(1)).deleteById(suggestedLecture.getId());
    }

    @Test
    @Transactional
    public void searchSuggestedLecture() throws Exception {
        // Initialize the database
        suggestedLectureService.save(suggestedLecture);
        when(mockSuggestedLectureSearchRepository.search(queryStringQuery("id:" + suggestedLecture.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(suggestedLecture), PageRequest.of(0, 1), 1));
        // Search the suggestedLecture
        restSuggestedLectureMockMvc.perform(get("/api/_search/suggested-lectures?query=id:" + suggestedLecture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(suggestedLecture.getId().intValue())))
            .andExpect(jsonPath("$.[*].profilePictureContentType").value(hasItem(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE))))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].averageScore").value(hasItem(DEFAULT_AVERAGE_SCORE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].videoCallLink").value(hasItem(DEFAULT_VIDEO_CALL_LINK.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(sameInstant(DEFAULT_PRESENTATION_DATE))))
            .andExpect(jsonPath("$.[*].timeZone").value(hasItem(DEFAULT_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SuggestedLecture.class);
        SuggestedLecture suggestedLecture1 = new SuggestedLecture();
        suggestedLecture1.setId(1L);
        SuggestedLecture suggestedLecture2 = new SuggestedLecture();
        suggestedLecture2.setId(suggestedLecture1.getId());
        assertThat(suggestedLecture1).isEqualTo(suggestedLecture2);
        suggestedLecture2.setId(2L);
        assertThat(suggestedLecture1).isNotEqualTo(suggestedLecture2);
        suggestedLecture1.setId(null);
        assertThat(suggestedLecture1).isNotEqualTo(suggestedLecture2);
    }
}
