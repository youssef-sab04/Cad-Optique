package cad.project.service;

import cad.project.exceptions.ResourceNotFoundException;
import cad.project.playload.ExamenDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.ExamenRepository;
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
public class ExamenServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ExamenRepository examenRepository;
    @Mock
    private ClientRepositry clientRepositry;

    @InjectMocks
    private ExamenServiceImp examenServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable à l'ajout")
    void test_AddExamen_when_client_introuvable() {
        Long clientId = 99L;

        when(clientRepositry.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> examenServiceImp.AddExamen(clientId, new ExamenDTO()));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'examen est introuvable à la mise à jour")
    void test_UpdateExamen_when_id_introuvable() {
        Long id = 99L;

        when(examenRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> examenServiceImp.UpdateExamen(id, new ExamenDTO()));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'examen est introuvable à la suppression")
    void test_DeleteExamen_when_id_introuvable() {
        Long id = 99L;

        when(examenRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> examenServiceImp.DeleteExamen(id));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'examen est introuvable à la lecture")
    void test_getExamen_when_id_introuvable() {
        Long id = 99L;

        when(examenRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> examenServiceImp.getExamen(id));
    }
}