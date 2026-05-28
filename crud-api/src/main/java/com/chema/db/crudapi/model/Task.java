package com.chema.db.crudapi.model;

// Importa todas las anotaciones de JPA/Hibernate
import jakarta.persistence.*;

// Importa una clase de java para manejar fechas y horas
import java.time.LocalDateTime;

// Import para validar que no esten campos en blanco y para que haya un limite en el tamaño del mensaje enviado
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Entity se usa para indicar que esto es una base de datos y Table para definir el nombre
@Entity
@Table(name = "tasks")
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

    // Obligatorio en hibernate tener un constructor vacio para crear objetos automaticamente
    public Task() {
    }

    // Constructor para crear objetos completos
    public Task(Long id, String title, String description, TaskStatus status,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
