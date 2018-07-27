package com.wildboar.repository;

import com.wildboar.domain.ParentStudent;
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
    List<ParentStudent> findByUserIsCurrentUser();

}
