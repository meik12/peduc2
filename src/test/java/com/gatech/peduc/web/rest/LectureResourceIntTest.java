package com.gatech.peduc.web.rest;

import com.gatech.peduc.Peduc2App;

import com.gatech.peduc.domain.Lecture;
import com.gatech.peduc.repository.LectureRepository;
import com.gatech.peduc.repository.search.LectureSearchRepository;
import com.gatech.peduc.service.LectureService;
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
 * Test class for the LectureResource REST controller.
 *
 * @see LectureResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Peduc2App.class)
public class LectureResourceIntTest {

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final String DEFAULT_TOPIC = "AAAAAAAAAA";
    private static final String UPDATED_TOPIC = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_KEY_WORD = "AAAAAAAAAA";
    private static final String UPDATED_KEY_WORD = "BBBBBBBBBB";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

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
    private LectureRepository lectureRepository;
    
    @Autowired
    private LectureService lectureService;

    /**
     * This repository is mocked in the com.gatech.peduc.repository.search test package.
     *
     * @see com.gatech.peduc.repository.search.LectureSearchRepositoryMockConfiguration
     */
    @Autowired
    private LectureSearchRepository mockLectureSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLectureMockMvc;

    private Lecture lecture;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LectureResource lectureResource = new LectureResource(lectureService);
        this.restLectureMockMvc = MockMvcBuilders.standaloneSetup(lectureResource)
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
    public static Lecture createEntity(EntityManager em) {
        Lecture lecture = new Lecture()
            .category(DEFAULT_CATEGORY)
            .topic(DEFAULT_TOPIC)
            .title(DEFAULT_TITLE)
            .keyWord(DEFAULT_KEY_WORD)
            .duration(DEFAULT_DURATION)
            .status(DEFAULT_STATUS)
            .language(DEFAULT_LANGUAGE)
            .videoCallLink(DEFAULT_VIDEO_CALL_LINK)
            .presentationDate(DEFAULT_PRESENTATION_DATE)
            .timeZone(DEFAULT_TIME_ZONE)
            .publicationDate(DEFAULT_PUBLICATION_DATE);
        return lecture;
    }

    @Before
    public void initTest() {
        lecture = createEntity(em);
    }

    @Test
    @Transactional
    public void createLecture() throws Exception {
        int databaseSizeBeforeCreate = lectureRepository.findAll().size();

        // Create the Lecture
        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isCreated());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeCreate + 1);
        Lecture testLecture = lectureList.get(lectureList.size() - 1);
        assertThat(testLecture.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testLecture.getTopic()).isEqualTo(DEFAULT_TOPIC);
        assertThat(testLecture.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testLecture.getKeyWord()).isEqualTo(DEFAULT_KEY_WORD);
        assertThat(testLecture.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testLecture.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testLecture.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testLecture.getVideoCallLink()).isEqualTo(DEFAULT_VIDEO_CALL_LINK);
        assertThat(testLecture.getPresentationDate()).isEqualTo(DEFAULT_PRESENTATION_DATE);
        assertThat(testLecture.getTimeZone()).isEqualTo(DEFAULT_TIME_ZONE);
        assertThat(testLecture.getPublicationDate()).isEqualTo(DEFAULT_PUBLICATION_DATE);

        // Validate the Lecture in Elasticsearch
        verify(mockLectureSearchRepository, times(1)).save(testLecture);
    }

    @Test
    @Transactional
    public void createLectureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lectureRepository.findAll().size();

        // Create the Lecture with an existing ID
        lecture.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeCreate);

        // Validate the Lecture in Elasticsearch
        verify(mockLectureSearchRepository, times(0)).save(lecture);
    }

    @Test
    @Transactional
    public void checkCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setCategory(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTopicIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setTopic(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setTitle(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkKeyWordIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setKeyWord(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setDuration(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setStatus(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLanguageIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setLanguage(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPresentationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = lectureRepository.findAll().size();
        // set the field null
        lecture.setPresentationDate(null);

        // Create the Lecture, which fails.

        restLectureMockMvc.perform(post("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLectures() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get all the lectureList
        restLectureMockMvc.perform(get("/api/lectures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lecture.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].keyWord").value(hasItem(DEFAULT_KEY_WORD.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].videoCallLink").value(hasItem(DEFAULT_VIDEO_CALL_LINK.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(sameInstant(DEFAULT_PRESENTATION_DATE))))
            .andExpect(jsonPath("$.[*].timeZone").value(hasItem(DEFAULT_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getLecture() throws Exception {
        // Initialize the database
        lectureRepository.saveAndFlush(lecture);

        // Get the lecture
        restLectureMockMvc.perform(get("/api/lectures/{id}", lecture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lecture.getId().intValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
            .andExpect(jsonPath("$.topic").value(DEFAULT_TOPIC.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.keyWord").value(DEFAULT_KEY_WORD.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE.toString()))
            .andExpect(jsonPath("$.videoCallLink").value(DEFAULT_VIDEO_CALL_LINK.toString()))
            .andExpect(jsonPath("$.presentationDate").value(sameInstant(DEFAULT_PRESENTATION_DATE)))
            .andExpect(jsonPath("$.timeZone").value(DEFAULT_TIME_ZONE.toString()))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLecture() throws Exception {
        // Get the lecture
        restLectureMockMvc.perform(get("/api/lectures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLecture() throws Exception {
        // Initialize the database
        lectureService.save(lecture);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockLectureSearchRepository);

        int databaseSizeBeforeUpdate = lectureRepository.findAll().size();

        // Update the lecture
        Lecture updatedLecture = lectureRepository.findById(lecture.getId()).get();
        // Disconnect from session so that the updates on updatedLecture are not directly saved in db
        em.detach(updatedLecture);
        updatedLecture
            .category(UPDATED_CATEGORY)
            .topic(UPDATED_TOPIC)
            .title(UPDATED_TITLE)
            .keyWord(UPDATED_KEY_WORD)
            .duration(UPDATED_DURATION)
            .status(UPDATED_STATUS)
            .language(UPDATED_LANGUAGE)
            .videoCallLink(UPDATED_VIDEO_CALL_LINK)
            .presentationDate(UPDATED_PRESENTATION_DATE)
            .timeZone(UPDATED_TIME_ZONE)
            .publicationDate(UPDATED_PUBLICATION_DATE);

        restLectureMockMvc.perform(put("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLecture)))
            .andExpect(status().isOk());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeUpdate);
        Lecture testLecture = lectureList.get(lectureList.size() - 1);
        assertThat(testLecture.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testLecture.getTopic()).isEqualTo(UPDATED_TOPIC);
        assertThat(testLecture.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testLecture.getKeyWord()).isEqualTo(UPDATED_KEY_WORD);
        assertThat(testLecture.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testLecture.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testLecture.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testLecture.getVideoCallLink()).isEqualTo(UPDATED_VIDEO_CALL_LINK);
        assertThat(testLecture.getPresentationDate()).isEqualTo(UPDATED_PRESENTATION_DATE);
        assertThat(testLecture.getTimeZone()).isEqualTo(UPDATED_TIME_ZONE);
        assertThat(testLecture.getPublicationDate()).isEqualTo(UPDATED_PUBLICATION_DATE);

        // Validate the Lecture in Elasticsearch
        verify(mockLectureSearchRepository, times(1)).save(testLecture);
    }

    @Test
    @Transactional
    public void updateNonExistingLecture() throws Exception {
        int databaseSizeBeforeUpdate = lectureRepository.findAll().size();

        // Create the Lecture

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLectureMockMvc.perform(put("/api/lectures")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(lecture)))
            .andExpect(status().isBadRequest());

        // Validate the Lecture in the database
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Lecture in Elasticsearch
        verify(mockLectureSearchRepository, times(0)).save(lecture);
    }

    @Test
    @Transactional
    public void deleteLecture() throws Exception {
        // Initialize the database
        lectureService.save(lecture);

        int databaseSizeBeforeDelete = lectureRepository.findAll().size();

        // Get the lecture
        restLectureMockMvc.perform(delete("/api/lectures/{id}", lecture.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Lecture> lectureList = lectureRepository.findAll();
        assertThat(lectureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Lecture in Elasticsearch
        verify(mockLectureSearchRepository, times(1)).deleteById(lecture.getId());
    }

    @Test
    @Transactional
    public void searchLecture() throws Exception {
        // Initialize the database
        lectureService.save(lecture);
        when(mockLectureSearchRepository.search(queryStringQuery("id:" + lecture.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(lecture), PageRequest.of(0, 1), 1));
        // Search the lecture
        restLectureMockMvc.perform(get("/api/_search/lectures?query=id:" + lecture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lecture.getId().intValue())))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].topic").value(hasItem(DEFAULT_TOPIC.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].keyWord").value(hasItem(DEFAULT_KEY_WORD.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE.toString())))
            .andExpect(jsonPath("$.[*].videoCallLink").value(hasItem(DEFAULT_VIDEO_CALL_LINK.toString())))
            .andExpect(jsonPath("$.[*].presentationDate").value(hasItem(sameInstant(DEFAULT_PRESENTATION_DATE))))
            .andExpect(jsonPath("$.[*].timeZone").value(hasItem(DEFAULT_TIME_ZONE.toString())))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lecture.class);
        Lecture lecture1 = new Lecture();
        lecture1.setId(1L);
        Lecture lecture2 = new Lecture();
        lecture2.setId(lecture1.getId());
        assertThat(lecture1).isEqualTo(lecture2);
        lecture2.setId(2L);
        assertThat(lecture1).isNotEqualTo(lecture2);
        lecture1.setId(null);
        assertThat(lecture1).isNotEqualTo(lecture2);
    }
}
