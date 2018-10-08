package com.fernandotozetto.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandotozetto.cursomc.domain.Categoria;

/*
 * A Classe Repository é responsavel pelo acesso aos dados (DAO)
 * ações esperadas: Pesquisa, Inclusão, Alteração e Exclusão
 */

@Repository
public interface CategoriaRepository extends JpaRepository <Categoria, Integer> {

}
