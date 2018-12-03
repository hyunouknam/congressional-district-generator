package cse308.Data;

import org.springframework.data.repository.CrudRepository;

import cse308.Users.UserAccount;

public interface UserAccountRepository extends CrudRepository<UserAccount, Integer>{

}
