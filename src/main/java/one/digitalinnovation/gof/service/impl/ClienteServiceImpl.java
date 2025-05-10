package one.digitalinnovation.gof.service.impl;

import one.digitalinnovation.gof.dto.ClienteDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import one.digitalinnovation.gof.model.Cliente;
import one.digitalinnovation.gof.model.ClienteRepository;
import one.digitalinnovation.gof.model.Endereco;
import one.digitalinnovation.gof.model.EnderecoRepository;
import one.digitalinnovation.gof.service.ClienteService;
import one.digitalinnovation.gof.service.ViaCepService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class ClienteServiceImpl implements ClienteService{

    @Autowired private ClienteRepository clienteRepository;
    @Autowired private EnderecoRepository enderecoRepository;
    @Autowired private ViaCepService viaCepService;
    @Autowired private ModelMapper mapper;

    @Override
    public List<ClienteDTO> buscarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(c -> mapper.map(c, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClienteDTO buscarPorId(Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return mapper.map(c, ClienteDTO.class);
    }

    @Override
    public ClienteDTO inserir(ClienteDTO dto) {
        Cliente entidade = converterParaEntidade(dto);
        salvarClienteComCep(entidade);
        return mapper.map(entidade, ClienteDTO.class);
    }

    @Override
    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        Optional<Cliente> opc = clienteRepository.findById(id);
        if (opc.isPresent()) {
            Cliente entidade = converterParaEntidade(dto);
            entidade.setId(id);
            salvarClienteComCep(entidade);
            return mapper.map(entidade, ClienteDTO.class);
        } else {
            throw new RuntimeException("Cliente não encontrado");
        }
    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }

    private Cliente converterParaEntidade(ClienteDTO dto) {
        // mapeia nome e id automaticamente; 'cep' precisa virar um Endereco parcial
        Cliente c = mapper.map(dto, Cliente.class);
        Endereco e = new Endereco();
        e.setCep(dto.getCep());
        c.setEndereco(e);
        return c;
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novo = viaCepService.consultarCep(cep);
            enderecoRepository.save(novo);
            return novo;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
