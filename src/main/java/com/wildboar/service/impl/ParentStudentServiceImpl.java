package com.wildboar.service.impl;

import com.wildboar.service.ParentStudentService;
import com.wildboar.domain.ParentStudent;
import com.wildboar.repository.ParentStudentRepository;
import com.wildboar.service.dto.ParentStudentDTO;
import com.wildboar.service.mapper.ParentStudentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ParentStudent.
 */
@Service
@Transactional
public class ParentStudentServiceImpl implements ParentStudentService {

    private final Logger log = LoggerFactory.getLogger(ParentStudentServiceImpl.class);

    private final ParentStudentRepository parentStudentRepository;

    private final ParentStudentMapper parentStudentMapper;

    public ParentStudentServiceImpl(ParentStudentRepository parentStudentRepository, ParentStudentMapper parentStudentMapper) {
        this.parentStudentRepository = parentStudentRepository;
        this.parentStudentMapper = parentStudentMapper;
    }

    /**
     * Save a parentStudent.
     *
     * @param parentStudentDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ParentStudentDTO save(ParentStudentDTO parentStudentDTO) {
        log.debug("Request to save ParentStudent : {}", parentStudentDTO);
        ParentStudent parentStudent = parentStudentMapper.toEntity(parentStudentDTO);
        parentStudent = parentStudentRepository.save(parentStudent);
        return parentStudentMapper.toDto(parentStudent);
    }

    /**
     * Get all the parentStudents.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParentStudentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ParentStudents");
        return parentStudentRepository.findAll(pageable)
            .map(parentStudentMapper::toDto);
    }

    /**
     * Get all the parentStudents by CurentUser.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParentStudentDTO> findByUserIsCurrentUser(Pageable pageable) {
        log.debug("Request to get all ParentStudents by CurrentUser");
        return parentStudentRepository.findByUserIsCurrentUser(pageable)
            .map(parentStudentMapper::toDto);
    }

    /**
     * Get one parentStudent by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ParentStudentDTO> findOne(Long id) {
        log.debug("Request to get ParentStudent : {}", id);
        return parentStudentRepository.findById(id)
            .map(parentStudentMapper::toDto);
    }

    /**
     * Delete the parentStudent by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ParentStudent : {}", id);
        parentStudentRepository.deleteById(id);
    }
}
