package com.example.Gluco_APP.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List; // Pour renvoyer des collections
import java.util.Optional; // Pour gérer les valeurs optionnelles
import com.example.Gluco_APP.Model.Doctor; // Importation de l'entité
import com.example.Gluco_APP.Repository.DoctorRepository; // Importation du repository
import com.example.Gluco_APP.Service.DoctorService;
import com.example.Gluco_APP.Service.DoctorServiceimpl;
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorRepository doctorRepository;
    @Autowired
    public DoctorController(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }
    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        return ResponseEntity.ok().body(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = doctorRepository.save(doctor);
        return ResponseEntity.created(URI.create("/api/doctors/" + savedDoctor.getId())).body(savedDoctor);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctorDetails) {
        return doctorRepository.findById(id)
                .map(existingDoctor -> {
                    existingDoctor.setNom(doctorDetails.getNom());
                    Doctor updatedDoctor = doctorRepository.save(existingDoctor);
                    return ResponseEntity.ok().body(updatedDoctor);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        if (doctorRepository.existsById(id)) {
            doctorRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

