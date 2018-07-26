package initial.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import initial.models.Users;
import initial.repositories.UsersRepository;

import static java.util.Collections.emptyList;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UsersRepository usersRepository;

    public UserDetailsServiceImpl(UsersRepository applicationUserRepository) {
        this.usersRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getUsername(), user.getPassword(), emptyList());
    }
}
