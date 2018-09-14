package com.fernandotozetto.cursomc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fernandotozetto.cursomc.domain.ItemPedido;;

@Repository
public interface ItemPedidoRepository extends JpaRepository <ItemPedido, Integer> {

}
