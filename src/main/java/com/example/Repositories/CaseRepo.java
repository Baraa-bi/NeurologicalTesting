package com.example.Repositories;

import com.example.Models.Case;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by baraa on 2/25/2017.
 */
public interface CaseRepo extends CrudRepository<Case,Long> {

    Iterable<Case> findBycreatedBy(long createdBy);
}
