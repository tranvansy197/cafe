package com.cafe.server.security.principle;

import com.cafe.entity.User;
import com.cafe.entity.User_;
import com.cafe.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username){
        Specification<User> spec = (root, query, criteriaBuilder) -> {
            root.fetch(User_.ROLES);
            var idPredicate = criteriaBuilder.equal(root.get(User_.USERNAME), username);
            return criteriaBuilder.and(idPredicate);
        };
        User user = userRepository.findOne(spec).orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
        return UserPrinciple.build(user);
    }
}
