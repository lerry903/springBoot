package org.yctech.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.yctech.auth.model.UserInfo;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/20
 * Time: 16:43
 */
@Repository
public interface UserInfoRepository extends JpaSpecificationExecutor<UserInfo>, JpaRepository<UserInfo, Long> {

}
