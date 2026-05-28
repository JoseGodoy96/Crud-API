package com.chema.db.crudapi.repository;

// Llama a la base de datos
import com.chema.db.crudapi.model.Task;

/*
Es una interfaz genérica que Spring ya implementa automáticamente.
Te da:

save
findById
findAll
delete
count
existsById
*/

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
