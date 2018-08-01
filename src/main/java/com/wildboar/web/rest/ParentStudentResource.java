package com.wildboar.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.wildboar.service.ParentStudentService;
import com.wildboar.web.rest.errors.BadRequestAlertException;
import com.wildboar.web.rest.util.HeaderUtil;
import com.wildboar.web.rest.util.PaginationUtil;
import com.wildboar.service.dto.ParentStudentDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ParentStudent.
 */
@RestController
@RequestMapping("/api")
public class ParentStudentResource {

    private final Logger log = LoggerFactory.getLogger(ParentStudentResource.class);

    private static final String ENTITY_NAME = "parentStudent";

    private final ParentStudentService parentStudentService;

    public ParentStudentResource(ParentStudentService parentStudentService) {
        this.parentStudentService = parentStudentService;
    }

    /**
     * POST  /parent-students : Create a new parentStudent.
     *
     * @param parentStudentDTO the parentStudentDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new parentStudentDTO, or with status 400 (Bad Request) if the parentStudent has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/parent-students")
    @Timed
    public ResponseEntity<ParentStudentDTO> createParentStudent(@Valid @RequestBody ParentStudentDTO parentStudentDTO) throws URISyntaxException {
        log.debug("REST request to save ParentStudent : {}", parentStudentDTO);
        if (parentStudentDTO.getId() != null) {
            throw new BadRequestAlertException("A new parentStudent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParentStudentDTO result = parentStudentService.save(parentStudentDTO);
        return ResponseEntity.created(new URI("/api/parent-students/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /parent-students : Updates an existing parentStudent.
     *
     * @param parentStudentDTO the parentStudentDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated parentStudentDTO,
     * or with status 400 (Bad Request) if the parentStudentDTO is not valid,
     * or with status 500 (Internal Server Error) if the parentStudentDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/parent-students")
    @Timed
    public ResponseEntity<ParentStudentDTO> updateParentStudent(@Valid @RequestBody ParentStudentDTO parentStudentDTO) throws URISyntaxException {
        log.debug("REST request to update ParentStudent : {}", parentStudentDTO);
        if (parentStudentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParentStudentDTO result = parentStudentService.save(parentStudentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, parentStudentDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /parent-students : get all the parentStudents.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of parentStudents in body
     */
    @GetMapping("/parent-students")
    @Timed
    public ResponseEntity<List<ParentStudentDTO>> getAllParentStudents(Pageable pageable) {
        log.debug("REST request to get a page of ParentStudents");
        Page<ParentStudentDTO> page = parentStudentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parent-students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /parent-students/parent : get all Students belong to Parent.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of parentStudents in body
     */
    @GetMapping("/parent-students/parent")
    @Timed
    public ResponseEntity<List<ParentStudentDTO>> getAllParentStudentsBy(Pageable pageable) {
        log.debug("REST request to get a page of ParentStudents of current user");
        Page<ParentStudentDTO> page = parentStudentService.findByUserIsCurrentUser(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/parent-students");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    
    
    /**
     * GET  /parent-students/:id : get the "id" parentStudent.
     *
     * @param id the id of the parentStudentDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the parentStudentDTO, or with status 404 (Not Found)
     */
    @GetMapping("/parent-students/{id}")
    @Timed
    public ResponseEntity<ParentStudentDTO> getParentStudent(@PathVariable Long id) {
        log.debug("REST request to get ParentStudent : {}", id);
        Optional<ParentStudentDTO> parentStudentDTO = parentStudentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(parentStudentDTO);
    }

    /**
     * DELETE  /parent-students/:id : delete the "id" parentStudent.
     *
     * @param id the id of the parentStudentDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/parent-students/{id}")
    @Timed
    public ResponseEntity<Void> deleteParentStudent(@PathVariable Long id) {
        log.debug("REST request to delete ParentStudent : {}", id);
        parentStudentService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
