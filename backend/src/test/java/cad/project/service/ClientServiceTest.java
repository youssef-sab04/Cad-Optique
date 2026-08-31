package cad.project.service;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.playload.ClientDTO;
import cad.project.repositries.ClientRepositry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ClientRepositry clientRepositry;

    @InjectMocks
    private ClientServiceImp clientServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable à la mise à jour")
    void test_updateClient_when_id_introuvable() {
        Long id = 99L;

        when(clientRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientServiceImp.updateClient(id, new ClientDTO()));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable à la suppression")
    void test_deleteClient_when_id_introuvable() {
        Long id = 99L;

        when(clientRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientServiceImp.deleteClient(id));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable à la lecture")
    void test_getClient_when_id_introuvable() {
        Long id = 99L;

        when(clientRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clientServiceImp.getClient(id));
    }
}