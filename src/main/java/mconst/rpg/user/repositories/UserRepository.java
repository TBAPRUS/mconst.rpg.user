package mconst.rpg.user.repositories;

import mconst.rpg.user.models.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long>, PagingAndSortingRepository<UserEntity, Long> {
    public UserEntity findByUsername(String username);
    public UserEntity findByEmail(String email);
    public UserEntity findByUsernameAndIdNot(String username, Long id);
    public UserEntity findByEmailAndIdNot(String email, Long id);
//    public UserEntity findByUsernameOrEmail(String username, String email);
}
