package com.wildboar.repository;

import com.wildboar.domain.ParentStudent;
import com.wildboar.domain.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ParentStudent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParentStudentRepository extends JpaRepository<ParentStudent, Long> {

    @Query("select wb_parent_student from ParentStudent wb_parent_student where wb_parent_student.user.login = ?#{principal.username}")
    Page<ParentStudent> findByUserIsCurrentUser(Pageable pageable);

    List<ParentStudent> findByStudent(Student student);
}
