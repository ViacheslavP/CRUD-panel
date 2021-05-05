package boot.viapivov.crudboot.service;

import boot.viapivov.crudboot.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername (String s) throws UsernameNotFoundException {
        UserDetails user = userRepository.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("User " + s + " wasn't found");
        }
        return user;
    }
}



