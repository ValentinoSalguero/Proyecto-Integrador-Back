package com.techlab.categorias;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor; 

@Data
@NoArgsConstructor
@AllArgsConstructor 
@Entity
@Table(name = "categorias") 
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(unique = true, nullable = false) 
    private String nombre;
    private String descripcion;

}
