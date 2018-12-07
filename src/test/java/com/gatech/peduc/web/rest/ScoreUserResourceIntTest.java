package com.gatech.peduc.web.rest;

import com.gatech.peduc.Peduc2App;

import com.gatech.peduc.domain.ScoreUser;
import com.gatech.peduc.repository.ScoreUserRepository;
import com.gatech.peduc.repository.search.ScoreUserSearchRepository;
import com.gatech.peduc.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;


import static com.gatech.peduc.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ScoreUserResource REST controller.
 *
 * @see ScoreUserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Peduc2App.class)
public class ScoreUserResourceIntTest {

    private static final Integer DEFAULT_EXCELLENT = 0;
    private static final Integer UPDATED_EXCELLENT = 1;

    private static final Integer DEFAULT_VERY_GOOD = 1;
    private static final Integer UPDATED_VERY_GOOD = 2;

    private static final Integer DEFAULT_FAIR = 1;
    private static final Integer UPDATED_FAIR = 2;

    private static final Integer DEFAULT_BAD = 1;
    private static final Integer UPDATED_BAD = 2;

    private static final Integer DEFAULT_AVERAGE = 1;
    private static final Integer UPDATED_AVERAGE = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private ScoreUserRepository scoreUserRepository;

    /**
     * This repository is mocked in the com.gatech.peduc.repository.search test package.
     *
     * @see com.gatech.peduc.repository.search.ScoreUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private ScoreUserSearchRepository mockScoreUserSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScoreUserMockMvc;

    private ScoreUser scoreUser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScoreUserResource scoreUserResource = new ScoreUserResource(scoreUserRepository, mockScoreUserSearchRepository);
        this.restScoreUserMockMvc = MockMvcBuilders.standaloneSetup(scoreUserResource)
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
    public static ScoreUser createEntity(EntityManager em) {
        ScoreUser scoreUser = new ScoreUser()
            .excellent(DEFAULT_EXCELLENT)
            .veryGood(DEFAULT_VERY_GOOD)
            .fair(DEFAULT_FAIR)
            .bad(DEFAULT_BAD)
            .average(DEFAULT_AVERAGE)
            .description(DEFAULT_DESCRIPTION);
        return scoreUser;
    }

    @Before
    public void initTest() {
        scoreUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createScoreUser() throws Exception {
        int databaseSizeBeforeCreate = scoreUserRepository.findAll().size();

        // Create the ScoreUser
        restScoreUserMockMvc.perform(post("/api/score-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreUser)))
            .andExpect(status().isCreated());

        // Validate the ScoreUser in the database
        List<ScoreUser> scoreUserList = scoreUserRepository.findAll();
        assertThat(scoreUserList).hasSize(databaseSizeBeforeCreate + 1);
        ScoreUser testScoreUser = scoreUserList.get(scoreUserList.size() - 1);
        assertThat(testScoreUser.getExcellent()).isEqualTo(DEFAULT_EXCELLENT);
        assertThat(testScoreUser.getVeryGood()).isEqualTo(DEFAULT_VERY_GOOD);
        assertThat(testScoreUser.getFair()).isEqualTo(DEFAULT_FAIR);
        assertThat(testScoreUser.getBad()).isEqualTo(DEFAULT_BAD);
        assertThat(testScoreUser.getAverage()).isEqualTo(DEFAULT_AVERAGE);
        assertThat(testScoreUser.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the ScoreUser in Elasticsearch
        verify(mockScoreUserSearchRepository, times(1)).save(testScoreUser);
    }

    @Test
    @Transactional
    public void createScoreUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scoreUserRepository.findAll().size();

        // Create the ScoreUser with an existing ID
        scoreUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreUserMockMvc.perform(post("/api/score-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreUser)))
            .andExpect(status().isBadRequest());

        // Validate the ScoreUser in the database
        List<ScoreUser> scoreUserList = scoreUserRepository.findAll();
        assertThat(scoreUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the ScoreUser in Elasticsearch
        verify(mockScoreUserSearchRepository, times(0)).save(scoreUser);
    }

    @Test
    @Transactional
    public void getAllScoreUsers() throws Exception {
        // Initialize the database
        scoreUserRepository.saveAndFlush(scoreUser);

        // Get all the scoreUserList
        restScoreUserMockMvc.perform(get("/api/score-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scoreUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].excellent").value(hasItem(DEFAULT_EXCELLENT)))
            .andExpect(jsonPath("$.[*].veryGood").value(hasItem(DEFAULT_VERY_GOOD)))
            .andExpect(jsonPath("$.[*].fair").value(hasItem(DEFAULT_FAIR)))
            .andExpect(jsonPath("$.[*].bad").value(hasItem(DEFAULT_BAD)))
            .andExpect(jsonPath("$.[*].average").value(hasItem(DEFAULT_AVERAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getScoreUser() throws Exception {
        // Initialize the database
        scoreUserRepository.saveAndFlush(scoreUser);

        // Get the scoreUser
        restScoreUserMockMvc.perform(get("/api/score-users/{id}", scoreUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scoreUser.getId().intValue()))
            .andExpect(jsonPath("$.excellent").value(DEFAULT_EXCELLENT))
            .andExpect(jsonPath("$.veryGood").value(DEFAULT_VERY_GOOD))
            .andExpect(jsonPath("$.fair").value(DEFAULT_FAIR))
            .andExpect(jsonPath("$.bad").value(DEFAULT_BAD))
            .andExpect(jsonPath("$.average").value(DEFAULT_AVERAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingScoreUser() throws Exception {
        // Get the scoreUser
        restScoreUserMockMvc.perform(get("/api/score-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScoreUser() throws Exception {
        // Initialize the database
        scoreUserRepository.saveAndFlush(scoreUser);

        int databaseSizeBeforeUpdate = scoreUserRepository.findAll().size();

        // Update the scoreUser
        ScoreUser updatedScoreUser = scoreUserRepository.findById(scoreUser.getId()).get();
        // Disconnect from session so that the updates on updatedScoreUser are not directly saved in db
        em.detach(updatedScoreUser);
        updatedScoreUser
            .excellent(UPDATED_EXCELLENT)
            .veryGood(UPDATED_VERY_GOOD)
            .fair(UPDATED_FAIR)
            .bad(UPDATED_BAD)
            .average(UPDATED_AVERAGE)
            .description(UPDATED_DESCRIPTION);

        restScoreUserMockMvc.perform(put("/api/score-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScoreUser)))
            .andExpect(status().isOk());

        // Validate the ScoreUser in the database
        List<ScoreUser> scoreUserList = scoreUserRepository.findAll();
        assertThat(scoreUserList).hasSize(databaseSizeBeforeUpdate);
        ScoreUser testScoreUser = scoreUserList.get(scoreUserList.size() - 1);
        assertThat(testScoreUser.getExcellent()).isEqualTo(UPDATED_EXCELLENT);
        assertThat(testScoreUser.getVeryGood()).isEqualTo(UPDATED_VERY_GOOD);
        assertThat(testScoreUser.getFair()).isEqualTo(UPDATED_FAIR);
        assertThat(testScoreUser.getBad()).isEqualTo(UPDATED_BAD);
        assertThat(testScoreUser.getAverage()).isEqualTo(UPDATED_AVERAGE);
        assertThat(testScoreUser.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the ScoreUser in Elasticsearch
        verify(mockScoreUserSearchRepository, times(1)).save(testScoreUser);
    }

    @Test
    @Transactional
    public void updateNonExistingScoreUser() throws Exception {
        int databaseSizeBeforeUpdate = scoreUserRepository.findAll().size();

        // Create the ScoreUser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreUserMockMvc.perform(put("/api/score-users")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scoreUser)))
            .andExpect(status().isBadRequest());

        // Validate the ScoreUser in the database
        List<ScoreUser> scoreUserList = scoreUserRepository.findAll();
        assertThat(scoreUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ScoreUser in Elasticsearch
        verify(mockScoreUserSearchRepository, times(0)).save(scoreUser);
    }

    @Test
    @Transactional
    public void deleteScoreUser() throws Exception {
        // Initialize the database
        scoreUserRepository.saveAndFlush(scoreUser);

        int databaseSizeBeforeDelete = scoreUserRepository.findAll().size();

        // Get the scoreUser
        restScoreUserMockMvc.perform(delete("/api/score-users/{id}", scoreUser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ScoreUser> scoreUserList = scoreUserRepository.findAll();
        assertThat(scoreUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ScoreUser in Elasticsearch
        verify(mockScoreUserSearchRepository, times(1)).deleteById(scoreUser.getId());
    }

    @Test
    @Transactional
    public void searchScoreUser() throws Exception {
        // Initialize the database
        scoreUserRepository.saveAndFlush(scoreUser);
        when(mockScoreUserSearchRepository.search(queryStringQuery("id:" + scoreUser.getId())))
            .thenReturn(Collections.singletonList(scoreUser));
        // Search the scoreUser
        restScoreUserMockMvc.perform(get("/api/_search/score-users?query=id:" + scoreUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scoreUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].excellent").value(hasItem(DEFAULT_EXCELLENT)))
            .andExpect(jsonPath("$.[*].veryGood").value(hasItem(DEFAULT_VERY_GOOD)))
            .andExpect(jsonPath("$.[*].fair").value(hasItem(DEFAULT_FAIR)))
            .andExpect(jsonPath("$.[*].bad").value(hasItem(DEFAULT_BAD)))
            .andExpect(jsonPath("$.[*].average").value(hasItem(DEFAULT_AVERAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScoreUser.class);
        ScoreUser scoreUser1 = new ScoreUser();
        scoreUser1.setId(1L);
        ScoreUser scoreUser2 = new ScoreUser();
        scoreUser2.setId(scoreUser1.getId());
        assertThat(scoreUser1).isEqualTo(scoreUser2);
        scoreUser2.setId(2L);
        assertThat(scoreUser1).isNotEqualTo(scoreUser2);
        scoreUser1.setId(null);
        assertThat(scoreUser1).isNotEqualTo(scoreUser2);
    }
}
