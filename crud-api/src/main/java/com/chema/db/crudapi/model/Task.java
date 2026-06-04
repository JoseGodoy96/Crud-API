package com.chema.db.crudapi.model;

// Importa todas las anotaciones de JPA/Hibernate
import jakarta.persistence.*;

// Importa una clase de java para manejar fechas y horas
import java.time.LocalDateTime;

// Import para validar que no esten campos en blanco y para que haya un limite en el tamaño del mensaje enviado
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Import para usar lombok
import lombok.Getter;
import lombok.Setter;

// Entity se usa para indicar que esto es una base de datos y Table para definir el nombre
@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Task {

    // Marca el atributo como clave primaria
    @Id
    // Genera el id automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    // Obliga a que title sea obligatorio
    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    private String description;

    // Aqui guardas un Enum
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Este metodo se ejecuta automaticamente asigna la fecha automaticamente y si no define estado se pone como pendiente por defecto
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = TaskStatus.PENDING;
        }
    }

    // Se ejecuta automaticamente antes de hacer update
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
