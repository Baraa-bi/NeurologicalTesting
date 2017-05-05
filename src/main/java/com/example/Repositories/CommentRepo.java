package com.example.Repositories;

import com.example.Models.Comment;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by baraa on 5/3/2017.
 */
public interface CommentRepo extends CrudRepository<Comment,Long> {

    Iterable<Comment> findByPostId(Long postId);
}
