package be.thomasmore.hartverlorenonderdentoren.userDetails;

import be.thomasmore.hartverlorenonderdentoren.model.Login.User;
import be.thomasmore.hartverlorenonderdentoren.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new MyUserDetails(user);
    }
//deze klasse maakt gebruik van een instantie van de UserRepository-interface in de methode
// loadUserByUsername() die wordt opgeroepen door Spring Security bij het authenticeren van de gebruikers.
}
