package one.digitalinnovation.gof.service;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.model.Cliente;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> buscarTodos();

    ClienteDTO buscarPorId(Long id);

    ClienteDTO inserir(ClienteDTO clienteDto);

    ClienteDTO atualizar(Long id, ClienteDTO clienteDto);

    void deletar(Long id);

}
