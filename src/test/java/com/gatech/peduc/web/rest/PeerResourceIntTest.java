package com.gatech.peduc.web.rest;

import com.gatech.peduc.Peduc2App;

import com.gatech.peduc.domain.Peer;
import com.gatech.peduc.repository.PeerRepository;
import com.gatech.peduc.repository.search.PeerSearchRepository;
import com.gatech.peduc.service.PeerService;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the PeerResource REST controller.
 *
 * @see PeerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Peduc2App.class)
public class PeerResourceIntTest {

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

    private static final String DEFAULT_INTEREST_AREAS = "AAAAAAAAAA";
    private static final String UPDATED_INTEREST_AREAS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private PeerRepository peerRepository;
    
    @Autowired
    private PeerService peerService;

    /**
     * This repository is mocked in the com.gatech.peduc.repository.search test package.
     *
     * @see com.gatech.peduc.repository.search.PeerSearchRepositoryMockConfiguration
     */
    @Autowired
    private PeerSearchRepository mockPeerSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPeerMockMvc;

    private Peer peer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PeerResource peerResource = new PeerResource(peerService);
        this.restPeerMockMvc = MockMvcBuilders.standaloneSetup(peerResource)
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
    public static Peer createEntity(EntityManager em) {
        Peer peer = new Peer()
            .profilePicture(DEFAULT_PROFILE_PICTURE)
            .profilePictureContentType(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .averageScore(DEFAULT_AVERAGE_SCORE)
            .interestAreas(DEFAULT_INTEREST_AREAS)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD);
        return peer;
    }

    @Before
    public void initTest() {
        peer = createEntity(em);
    }

    @Test
    @Transactional
    public void createPeer() throws Exception {
        int databaseSizeBeforeCreate = peerRepository.findAll().size();

        // Create the Peer
        restPeerMockMvc.perform(post("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isCreated());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeCreate + 1);
        Peer testPeer = peerList.get(peerList.size() - 1);
        assertThat(testPeer.getProfilePicture()).isEqualTo(DEFAULT_PROFILE_PICTURE);
        assertThat(testPeer.getProfilePictureContentType()).isEqualTo(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE);
        assertThat(testPeer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPeer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPeer.getAverageScore()).isEqualTo(DEFAULT_AVERAGE_SCORE);
        assertThat(testPeer.getInterestAreas()).isEqualTo(DEFAULT_INTEREST_AREAS);
        assertThat(testPeer.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testPeer.getPassword()).isEqualTo(DEFAULT_PASSWORD);

        // Validate the Peer in Elasticsearch
        verify(mockPeerSearchRepository, times(1)).save(testPeer);
    }

    @Test
    @Transactional
    public void createPeerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = peerRepository.findAll().size();

        // Create the Peer with an existing ID
        peer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeerMockMvc.perform(post("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeCreate);

        // Validate the Peer in Elasticsearch
        verify(mockPeerSearchRepository, times(0)).save(peer);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = peerRepository.findAll().size();
        // set the field null
        peer.setFirstName(null);

        // Create the Peer, which fails.

        restPeerMockMvc.perform(post("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = peerRepository.findAll().size();
        // set the field null
        peer.setLastName(null);

        // Create the Peer, which fails.

        restPeerMockMvc.perform(post("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = peerRepository.findAll().size();
        // set the field null
        peer.setEmail(null);

        // Create the Peer, which fails.

        restPeerMockMvc.perform(post("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = peerRepository.findAll().size();
        // set the field null
        peer.setPassword(null);

        // Create the Peer, which fails.

        restPeerMockMvc.perform(post("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPeers() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get all the peerList
        restPeerMockMvc.perform(get("/api/peers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peer.getId().intValue())))
            .andExpect(jsonPath("$.[*].profilePictureContentType").value(hasItem(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE))))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].averageScore").value(hasItem(DEFAULT_AVERAGE_SCORE)))
            .andExpect(jsonPath("$.[*].interestAreas").value(hasItem(DEFAULT_INTEREST_AREAS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }
    
    @Test
    @Transactional
    public void getPeer() throws Exception {
        // Initialize the database
        peerRepository.saveAndFlush(peer);

        // Get the peer
        restPeerMockMvc.perform(get("/api/peers/{id}", peer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(peer.getId().intValue()))
            .andExpect(jsonPath("$.profilePictureContentType").value(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.profilePicture").value(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE)))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.averageScore").value(DEFAULT_AVERAGE_SCORE))
            .andExpect(jsonPath("$.interestAreas").value(DEFAULT_INTEREST_AREAS.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPeer() throws Exception {
        // Get the peer
        restPeerMockMvc.perform(get("/api/peers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePeer() throws Exception {
        // Initialize the database
        peerService.save(peer);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockPeerSearchRepository);

        int databaseSizeBeforeUpdate = peerRepository.findAll().size();

        // Update the peer
        Peer updatedPeer = peerRepository.findById(peer.getId()).get();
        // Disconnect from session so that the updates on updatedPeer are not directly saved in db
        em.detach(updatedPeer);
        updatedPeer
            .profilePicture(UPDATED_PROFILE_PICTURE)
            .profilePictureContentType(UPDATED_PROFILE_PICTURE_CONTENT_TYPE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .averageScore(UPDATED_AVERAGE_SCORE)
            .interestAreas(UPDATED_INTEREST_AREAS)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);

        restPeerMockMvc.perform(put("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPeer)))
            .andExpect(status().isOk());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);
        Peer testPeer = peerList.get(peerList.size() - 1);
        assertThat(testPeer.getProfilePicture()).isEqualTo(UPDATED_PROFILE_PICTURE);
        assertThat(testPeer.getProfilePictureContentType()).isEqualTo(UPDATED_PROFILE_PICTURE_CONTENT_TYPE);
        assertThat(testPeer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPeer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPeer.getAverageScore()).isEqualTo(UPDATED_AVERAGE_SCORE);
        assertThat(testPeer.getInterestAreas()).isEqualTo(UPDATED_INTEREST_AREAS);
        assertThat(testPeer.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testPeer.getPassword()).isEqualTo(UPDATED_PASSWORD);

        // Validate the Peer in Elasticsearch
        verify(mockPeerSearchRepository, times(1)).save(testPeer);
    }

    @Test
    @Transactional
    public void updateNonExistingPeer() throws Exception {
        int databaseSizeBeforeUpdate = peerRepository.findAll().size();

        // Create the Peer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeerMockMvc.perform(put("/api/peers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(peer)))
            .andExpect(status().isBadRequest());

        // Validate the Peer in the database
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Peer in Elasticsearch
        verify(mockPeerSearchRepository, times(0)).save(peer);
    }

    @Test
    @Transactional
    public void deletePeer() throws Exception {
        // Initialize the database
        peerService.save(peer);

        int databaseSizeBeforeDelete = peerRepository.findAll().size();

        // Get the peer
        restPeerMockMvc.perform(delete("/api/peers/{id}", peer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Peer> peerList = peerRepository.findAll();
        assertThat(peerList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Peer in Elasticsearch
        verify(mockPeerSearchRepository, times(1)).deleteById(peer.getId());
    }

    @Test
    @Transactional
    public void searchPeer() throws Exception {
        // Initialize the database
        peerService.save(peer);
        when(mockPeerSearchRepository.search(queryStringQuery("id:" + peer.getId())))
            .thenReturn(Collections.singletonList(peer));
        // Search the peer
        restPeerMockMvc.perform(get("/api/_search/peers?query=id:" + peer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peer.getId().intValue())))
            .andExpect(jsonPath("$.[*].profilePictureContentType").value(hasItem(DEFAULT_PROFILE_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].profilePicture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PROFILE_PICTURE))))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].averageScore").value(hasItem(DEFAULT_AVERAGE_SCORE)))
            .andExpect(jsonPath("$.[*].interestAreas").value(hasItem(DEFAULT_INTEREST_AREAS.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Peer.class);
        Peer peer1 = new Peer();
        peer1.setId(1L);
        Peer peer2 = new Peer();
        peer2.setId(peer1.getId());
        assertThat(peer1).isEqualTo(peer2);
        peer2.setId(2L);
        assertThat(peer1).isNotEqualTo(peer2);
        peer1.setId(null);
        assertThat(peer1).isNotEqualTo(peer2);
    }
}
