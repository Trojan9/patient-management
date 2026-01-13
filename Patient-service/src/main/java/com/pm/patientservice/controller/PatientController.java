package com.pm.patientservice.controller;

import com.pm.patientservice.DTO.PatientRequestDTO;
import com.pm.patientservice.DTO.PatientResponseDTO;
import com.pm.patientservice.DTO.validators.CreatePatientValidationGroup;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patients") //means this will handle all requests or api routes /patients
@Tag(name = "Patient", description = "APi for managing patients")  // for the swagger documentation
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @GetMapping
    @Operation(summary = "Get Patients") // add summary for swagger documentaion
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.ok().body(patients);
    }

//    @valid performs validation that we specified in RequestDTO
//    @RequestBody ensures the request body is sent
    @PostMapping
    @Operation(summary = "Create a new Patients")
    public ResponseEntity<PatientResponseDTO> createPatients(@Validated({
            Default.class, CreatePatientValidationGroup.class
    }) @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);

    }

    //let's the id from the path to be passed to the api service function
    @PutMapping("/{id}")
    @Operation(summary = "Update a Patients information")
    public ResponseEntity<PatientResponseDTO> updatePatients(@PathVariable UUID id,@Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO){
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id,patientRequestDTO);
        return ResponseEntity.ok().body(patientResponseDTO);
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Patient record")
    public ResponseEntity<Void> deletePatients(@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
