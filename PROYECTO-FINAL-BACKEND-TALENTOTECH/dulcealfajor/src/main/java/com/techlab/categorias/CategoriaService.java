package com.techlab.categorias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Autowired
    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    public Categoria agregar(Categoria categoria) {
        if (categoria.getNombre() == null || categoria.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o vacío.");
        }
        return categoriaRepository.save(categoria);
    }

    public Optional<Categoria> actualizar(Integer id, Categoria nuevaCategoria) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            if (nuevaCategoria.getNombre() != null && !nuevaCategoria.getNombre().isBlank()) {
                categoriaExistente.setNombre(nuevaCategoria.getNombre());
            }
            if (nuevaCategoria.getDescripcion() != null) {
                categoriaExistente.setDescripcion(nuevaCategoria.getDescripcion());
            }
            return categoriaRepository.save(categoriaExistente);
        });
    }

    public boolean eliminar(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}