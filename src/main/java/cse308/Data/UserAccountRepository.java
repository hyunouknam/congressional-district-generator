package cse308.Data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cse308.Users.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer>{

}
