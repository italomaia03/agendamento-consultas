package tech.ada.java.agendamentoconsultas.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.ada.java.agendamentoconsultas.model.Admin;
import tech.ada.java.agendamentoconsultas.model.Doctor;
import tech.ada.java.agendamentoconsultas.model.Patient;
import tech.ada.java.agendamentoconsultas.repository.AdminRepository;
import tech.ada.java.agendamentoconsultas.repository.DoctorRepository;
import tech.ada.java.agendamentoconsultas.repository.PatientRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorizationService implements UserDetailsService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username);
    }

    private UserDetails getUser(String username) {
        Optional<Doctor> userDoctor = doctorRepository.findByEmail(username);
        Optional<Patient> userPatient = patientRepository.findByEmail(username);
        Optional<Admin> userAdmin = adminRepository.findByEmail(username);

        if (userDoctor.isPresent()) {
            return userDoctor.get();
        } else if (userPatient.isPresent()) {
            return userPatient.get();
        } else if (userAdmin.isPresent()) {
            return userAdmin.get();
        }
        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
