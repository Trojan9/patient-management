package com.pm.patientservice.service;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponseDTO;
import com.pm.patientservice.exception.EmailAlreadyExistException;
import com.pm.patientservice.exception.PatientNotFoundException;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
public class PatientService {
    private PatientRepository patientRepository;
//    add a constructor to init the repository access
    public  PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients (){
        List<Patient> patients = patientRepository.findAll();
//        this is just like calling toMap((pat)=>{})..or forEach((pat)=>{}) in javascript

//        List<PatientResponseDTO> patientResponseDTO = patients.stream().map(patient -> PatientMapper.toDTO(patient)).toList();

//        some do it this way, to shorten it PatientMapper::toDTO..but same thing above
        return patients.stream().map(PatientMapper::toDTO).toList();
    }
    public  PatientResponseDTO createPatient (PatientRequestDTO patientRequestDTO){
        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())){
            throw new EmailAlreadyExistException("A  patient with this email "+"already exist "+ patientRequestDTO.getEmail());
        }


        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toDTO(newPatient);
    }



    public PatientResponseDTO updatePatient (UUID id, PatientRequestDTO patientRequestDTO){
        Patient patient = patientRepository.findById(id).orElseThrow(()->new PatientNotFoundException("Patient Not Found With Id: "+ id));

//        now if we are updating email, we need to check if email already exist also
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),id)){
            throw new EmailAlreadyExistException("A  patient with this email "+"already exist "+ patientRequestDTO.getEmail());
        }
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        //now we update the patient
        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);

    }

    public  void deletePatient (UUID id){
        patientRepository.deleteById(id);
    }
}
