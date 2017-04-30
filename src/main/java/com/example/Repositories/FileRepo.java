package com.example.Repositories;

import com.example.Models.File;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baraa on 4/9/2017.
 */
public interface FileRepo extends JpaRepository<File,Long> {
    File findByCaseId(Long caseId);
    File findByUserId(Long userId);
    File findByPostId(Long postId);
}
