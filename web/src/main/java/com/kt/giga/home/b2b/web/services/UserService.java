package com.kt.giga.home.b2b.web.services;

import com.kt.giga.home.b2b.domain.AuthorityInfo;
import com.kt.giga.home.b2b.domain.B2bManager;
import com.kt.giga.home.b2b.domain.Role;
import com.kt.giga.home.b2b.entity.Manager;
import com.kt.giga.home.b2b.mapper.B2bManagerMapper;
import com.kt.giga.home.b2b.repository.ManagerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * com.kt.giga.home.b2b.services
 *      UserService
 *
 * Class 설명을 입력하세요.
 *
 * </pre>
 *
 * @author junypooh
 * @see
 * @since 2017-01-03 오후 5:33
 */
@Slf4j
@Service("userDetailsService")
public class UserService implements UserDetailsService {

    private final ManagerRepository managerRepository;

    @Autowired
    public UserService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    @Autowired
    private B2bManagerMapper b2bManagerMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        Manager manager = managerRepository.findById(username);
        Manager manager = managerRepository.findByIdAndStatus(username, Manager.Status.ACTIVATED.getStatusCd());

        if(manager == null) {
            throw new UsernameNotFoundException("등록된 사용자 정보를 찾을 수 없습니다");
        }

        if(manager.isLocked()) {
            throw new LockedException("해당 아이디는 정지 상태 입니다. 관리자에게 문의 부탁 드립니다.");
        }

        List<AuthorityInfo> authorities = new ArrayList<>();
        Role                   role        = manager.getRole();

        authorities.add(new AuthorityInfo(role.name()));

        while(role.getChildRole() != null) {
            Role childRole = role.getChildRole();

            authorities.add(new AuthorityInfo(childRole.name()));

            role = childRole;
        }

        B2bManager user = b2bManagerMapper.toB2bManager(manager);
        user.setAuthorities(authorities);

        if(log.isDebugEnabled())
            log.debug("user :: " + user);

        return user;
    }
}
