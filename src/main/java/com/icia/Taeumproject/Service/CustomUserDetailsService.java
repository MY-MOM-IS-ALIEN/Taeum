package com.icia.Taeumproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.icia.Taeumproject.Dao.MemberDao;
import com.icia.Taeumproject.Dto.SecurityUserDTO;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberDao memberDao;
   

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return
                Optional.ofNullable(memberDao.findUserByEmail(username))
                        .filter(m -> m != null)
                        .map(SecurityUserDTO::new).get();
    }

}