package com.wildboar.web.rest;

import com.wildboar.WildboarApp;

import com.wildboar.domain.ParentStudent;
import com.wildboar.domain.User;
import com.wildboar.domain.Student;
import com.wildboar.repository.ParentStudentRepository;
import com.wildboar.service.ParentStudentService;
import com.wildboar.service.dto.ParentStudentDTO;
import com.wildboar.service.mapper.ParentStudentMapper;
import com.wildboar.web.rest.errors.ExceptionTranslator;

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
import java.util.List;


import static com.wildboar.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ParentStudentResource REST controller.
 *
 * @see ParentStudentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WildboarApp.class)
public class ParentStudentResourceIntTest {

    @Autowired
    private ParentStudentRepository parentStudentRepository;


    @Autowired
    private ParentStudentMapper parentStudentMapper;
    

    @Autowired
    private ParentStudentService parentStudentService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restParentStudentMockMvc;

    private ParentStudent parentStudent;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParentStudentResource parentStudentResource = new ParentStudentResource(parentStudentService);
        this.restParentStudentMockMvc = MockMvcBuilders.standaloneSetup(parentStudentResource)
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
    public static ParentStudent createEntity(EntityManager em) {
        ParentStudent parentStudent = new ParentStudent();
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        parentStudent.setUser(user);
        // Add required entity
        Student student = StudentResourceIntTest.createEntity(em);
        em.persist(student);
        em.flush();
        parentStudent.setStudent(student);
        return parentStudent;
    }

    @Before
    public void initTest() {
        parentStudent = createEntity(em);
    }

    @Test
    @Transactional
    public void createParentStudent() throws Exception {
        int databaseSizeBeforeCreate = parentStudentRepository.findAll().size();

        // Create the ParentStudent
        ParentStudentDTO parentStudentDTO = parentStudentMapper.toDto(parentStudent);
        restParentStudentMockMvc.perform(post("/api/parent-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parentStudentDTO)))
            .andExpect(status().isCreated());

        // Validate the ParentStudent in the database
        List<ParentStudent> parentStudentList = parentStudentRepository.findAll();
        assertThat(parentStudentList).hasSize(databaseSizeBeforeCreate + 1);
        ParentStudent testParentStudent = parentStudentList.get(parentStudentList.size() - 1);
    }

    @Test
    @Transactional
    public void createParentStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = parentStudentRepository.findAll().size();

        // Create the ParentStudent with an existing ID
        parentStudent.setId(1L);
        ParentStudentDTO parentStudentDTO = parentStudentMapper.toDto(parentStudent);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParentStudentMockMvc.perform(post("/api/parent-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parentStudentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParentStudent in the database
        List<ParentStudent> parentStudentList = parentStudentRepository.findAll();
        assertThat(parentStudentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllParentStudents() throws Exception {
        // Initialize the database
        parentStudentRepository.saveAndFlush(parentStudent);

        // Get all the parentStudentList
        restParentStudentMockMvc.perform(get("/api/parent-students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(parentStudent.getId().intValue())));
    }
    

    @Test
    @Transactional
    public void getParentStudent() throws Exception {
        // Initialize the database
        parentStudentRepository.saveAndFlush(parentStudent);

        // Get the parentStudent
        restParentStudentMockMvc.perform(get("/api/parent-students/{id}", parentStudent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(parentStudent.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingParentStudent() throws Exception {
        // Get the parentStudent
        restParentStudentMockMvc.perform(get("/api/parent-students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParentStudent() throws Exception {
        // Initialize the database
        parentStudentRepository.saveAndFlush(parentStudent);

        int databaseSizeBeforeUpdate = parentStudentRepository.findAll().size();

        // Update the parentStudent
        ParentStudent updatedParentStudent = parentStudentRepository.findById(parentStudent.getId()).get();
        // Disconnect from session so that the updates on updatedParentStudent are not directly saved in db
        em.detach(updatedParentStudent);
        ParentStudentDTO parentStudentDTO = parentStudentMapper.toDto(updatedParentStudent);

        restParentStudentMockMvc.perform(put("/api/parent-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parentStudentDTO)))
            .andExpect(status().isOk());

        // Validate the ParentStudent in the database
        List<ParentStudent> parentStudentList = parentStudentRepository.findAll();
        assertThat(parentStudentList).hasSize(databaseSizeBeforeUpdate);
        ParentStudent testParentStudent = parentStudentList.get(parentStudentList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingParentStudent() throws Exception {
        int databaseSizeBeforeUpdate = parentStudentRepository.findAll().size();

        // Create the ParentStudent
        ParentStudentDTO parentStudentDTO = parentStudentMapper.toDto(parentStudent);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restParentStudentMockMvc.perform(put("/api/parent-students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(parentStudentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ParentStudent in the database
        List<ParentStudent> parentStudentList = parentStudentRepository.findAll();
        assertThat(parentStudentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParentStudent() throws Exception {
        // Initialize the database
        parentStudentRepository.saveAndFlush(parentStudent);

        int databaseSizeBeforeDelete = parentStudentRepository.findAll().size();

        // Get the parentStudent
        restParentStudentMockMvc.perform(delete("/api/parent-students/{id}", parentStudent.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ParentStudent> parentStudentList = parentStudentRepository.findAll();
        assertThat(parentStudentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParentStudent.class);
        ParentStudent parentStudent1 = new ParentStudent();
        parentStudent1.setId(1L);
        ParentStudent parentStudent2 = new ParentStudent();
        parentStudent2.setId(parentStudent1.getId());
        assertThat(parentStudent1).isEqualTo(parentStudent2);
        parentStudent2.setId(2L);
        assertThat(parentStudent1).isNotEqualTo(parentStudent2);
        parentStudent1.setId(null);
        assertThat(parentStudent1).isNotEqualTo(parentStudent2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParentStudentDTO.class);
        ParentStudentDTO parentStudentDTO1 = new ParentStudentDTO();
        parentStudentDTO1.setId(1L);
        ParentStudentDTO parentStudentDTO2 = new ParentStudentDTO();
        assertThat(parentStudentDTO1).isNotEqualTo(parentStudentDTO2);
        parentStudentDTO2.setId(parentStudentDTO1.getId());
        assertThat(parentStudentDTO1).isEqualTo(parentStudentDTO2);
        parentStudentDTO2.setId(2L);
        assertThat(parentStudentDTO1).isNotEqualTo(parentStudentDTO2);
        parentStudentDTO1.setId(null);
        assertThat(parentStudentDTO1).isNotEqualTo(parentStudentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(parentStudentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(parentStudentMapper.fromId(null)).isNull();
    }
}
