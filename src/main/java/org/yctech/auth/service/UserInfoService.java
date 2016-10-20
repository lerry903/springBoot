package org.yctech.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yctech.Application;
import org.yctech.auth.model.UserInfo;
import org.yctech.auth.repository.UserInfoRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by IntelliJ IDEA.
 * User: lerry
 * Date: 2016/10/20
 * Time: 17:17
 */
@Service
public class UserInfoService {
    private static final Logger logger  = LoggerFactory.getLogger(UserInfoService.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserInfoRepository userInfoRepo;

}
