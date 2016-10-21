package org.yctech.auth.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yctech.auth.model.UserInfo;
import org.yctech.auth.repository.UserInfoRepository;
import org.yctech.auth.service.UserInfoService;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/20
 * Time: 17:07
 */
@RestController
@RequestMapping("/user")
public class UserInfoController {
    private static final Logger logger  = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoRepository userInfoRepo;

    @RequestMapping("/{id}")
    @Secured("ROLE_USER")
    public UserInfo findUserById(@PathVariable("id") Long id) {
        logger.info("UserId :"+id);
        UserInfo userInfo = userInfoRepo.findOne(id);
        return userInfo;
    }


}
