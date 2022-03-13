package com.example.Miarma.services;

import com.example.Miarma.dto.CreateUserDto;
import com.example.Miarma.models.UserEntity;
import com.example.Miarma.repositories.SeguimientoRepository;
import com.example.Miarma.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("userDetailsService")
@RequiredArgsConstructor
public class UserEntityService extends BaseService<UserEntity, UUID, UserEntityRepository> implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private UserEntityRepository userEntityRepository;
    private SeguimientoRepository seguimientoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.repositorio.findFirstByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username + " no encontrado"));
    }

    public UserEntity save(CreateUserDto newUser) {
        if (newUser.getPassword().contentEquals(newUser.getPassword2())) {
            UserEntity userEntity = UserEntity.builder()
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .avatar(newUser.getAvatar())
                    .fullName(newUser.getFullname())
                    .email(newUser.getEmail())
                    .role("USER")
                    .build();
            return save(userEntity);
        } else {
            return null;
        }
    }

    public Optional<UserEntity> findUserByUsername(String username){
        return userEntityRepository.findFirstByUsername(username);
    }

    public List<UserEntity> peticionesDeSeguimientoAMi(UUID id){
        List<UserEntity> meQuierenSeguir = new ArrayList();
        seguimientoRepository.getMyFollowersWaiting(id).forEach(
                peticion -> meQuierenSeguir.add(peticion.getSeguido())
        );
        return meQuierenSeguir;
    }

}
