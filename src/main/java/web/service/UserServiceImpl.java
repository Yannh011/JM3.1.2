package web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.dao.UserDao;
import web.model.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public void add(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDao.save(user);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public void update(User user) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        if (encryptedPassword.matches(user.getPassword())) {
            user.setPassword(encryptedPassword);
        }
        userDao.save(user);
    }

    @Override
    @Transactional
    public User getById(long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User getByName(String name) {
        return userDao.getByName(name);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userDao.getByName(s);
    }
}
