package com.api.estudo.services;

import com.api.estudo.database.repositories.ConstructorRepository;
import com.api.estudo.database.repositories.DriverRepository;
import com.api.estudo.exceptions.EntityNotFoundException;
import com.api.estudo.models.RaceUser;
import com.api.estudo.database.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final ConstructorRepository constructorRepository;


    public UserService(UserRepository userRepository, DriverRepository driverRepository, ConstructorRepository constructorRepository) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.constructorRepository = constructorRepository;
    }

    public RaceUser findUserByLogin(String login) {
        Optional<RaceUser> user = userRepository
                .findByLogin(login);

        if(user.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        RaceUser found = user.get();
        found.setLogin(found.getLogin().trim());
        return found;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        log.info("[ UserService ] - Finding user by login.");
        return findUserByLogin(username);
    }

    public RaceUser getUserById(Integer idUsuario) {
        Optional<RaceUser> user = userRepository.findById(idUsuario);
        if(user.isEmpty()) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        RaceUser found = user.get();
        found.setLogin(found.getLogin().trim());
        return found;
    }

    public String getNameByRef(String personRef) {
        String loginWithoutSuffix = personRef.substring(0, personRef.length() - 2);
        List<Object[]> selectResultDriver = driverRepository.selectDriverGivenRef(loginWithoutSuffix);

        if(isResultPresent(selectResultDriver)) {
            String forename = selectResultDriver.get(0)[0] == null ? "" : selectResultDriver.get(0)[0].toString();
            String surname = selectResultDriver.get(0)[1].toString() == null ? "" : selectResultDriver.get(0)[1].toString();
            return  forename + " " + surname;
        }

        List<Object[]> selectResultConstructor = constructorRepository.selectConstructorGivenRef(loginWithoutSuffix);
        if(isResultPresent(selectResultConstructor)) {
            return selectResultConstructor.get(0)[0].toString();
        }

        return personRef;
    }

    private boolean isResultPresent(List<Object[]> selectResultDriver) {
        return selectResultDriver != null && !selectResultDriver.isEmpty();
    }
}
