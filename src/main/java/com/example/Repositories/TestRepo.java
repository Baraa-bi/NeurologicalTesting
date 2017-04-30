package com.example.Repositories;

import com.example.Models.Case;
import com.example.Models.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by baraa on 4/2/2017.
 */


public interface TestRepo extends CrudRepository<Test,Long> {

    Iterable<Test> findBycaseId(long caseId);
    Iterable<Test> removeByCaseId(long id);

}
