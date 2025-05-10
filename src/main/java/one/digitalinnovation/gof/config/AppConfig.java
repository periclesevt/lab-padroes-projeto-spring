package one.digitalinnovation.gof.config;

import one.digitalinnovation.gof.dto.ClienteDTO;
import one.digitalinnovation.gof.model.Cliente;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();

        mapper.addMappings(new PropertyMap<Cliente, ClienteDTO>() {
            @Override
            protected void configure(){
                map().setCep(source.getEndereco().getCep());
            }
        });
        return mapper;
    }
}
