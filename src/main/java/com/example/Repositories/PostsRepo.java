package com.example.Repositories;

import com.example.Models.Case;
import com.example.Models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by baraa on 4/19/2017.
 */
public interface PostsRepo extends JpaRepository<Post,Long> {

    Iterable<Post> findBycreatedBy(long createdBy);
}
