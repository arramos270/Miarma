package com.example.Miarma.Services;

import com.example.Miarma.Dto.CreateUserDto;
import com.example.Miarma.Models.Seguimiento;
import com.example.Miarma.Models.UserEntity;
import com.example.Miarma.Repositories.PostRepository;
import com.example.Miarma.Repositories.SeguimientoRepository;
import com.example.Miarma.Repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
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
