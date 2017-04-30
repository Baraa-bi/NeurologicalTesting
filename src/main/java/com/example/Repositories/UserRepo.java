package com.example.Repositories;

import com.example.Models.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by baraa on 2/21/2017.
 */
public interface UserRepo extends CrudRepository<User,Long> {

    User findByUserName(String userName);
    User findByEmail(String email);

}
