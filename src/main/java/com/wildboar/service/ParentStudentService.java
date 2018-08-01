package com.wildboar.service;

import com.wildboar.service.dto.ParentStudentDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ParentStudent.
 */
public interface ParentStudentService {

    /**
     * Save a parentStudent.
     *
     * @param parentStudentDTO the entity to save
     * @return the persisted entity
     */
    ParentStudentDTO save(ParentStudentDTO parentStudentDTO);

    /**
     * Get all the parentStudents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ParentStudentDTO> findAll(Pageable pageable);

    /**
     * Get all the parentStudents by CurrentUser.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ParentStudentDTO> findByUserIsCurrentUser(Pageable pageable);

        
    /**
     * Get the "id" parentStudent.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ParentStudentDTO> findOne(Long id);

    /**
     * Delete the "id" parentStudent.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
