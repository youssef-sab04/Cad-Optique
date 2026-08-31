package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.OrdonnanceLentille;
import cad.project.playload.OrdonnanceLentilleDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.OrdonnanceLentilleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrdonnanceLentilleServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private OrdonnanceLentilleRepository ordonnanceLentilleRepository;
    @Mock
    private ClientRepositry clientRepositry;

    @InjectMocks
    private OrdonnanceLentilleServiceImp ordonnanceLentilleServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable à l'ajout")
    void test_AddOrdLent_when_client_introuvable() {
        Long clientId = 99L;

        when(clientRepositry.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonnanceLentilleServiceImp.AddOrdLent(clientId, new OrdonnanceLentilleDTO()));
    }

    @Test
    @DisplayName("Doit lever APIException quand une ordonnance identique existe déjà")
    void test_AddOrdLent_when_ordonnance_deja_existante() {
        Long clientId = 1L;
        Client client = new Client();

        OrdonnanceLentille existante = new OrdonnanceLentille();
        existante.setPrescripteur("Dr Alami");
        existante.setDateEmission(LocalDate.of(2026, 1, 1));

        OrdonnanceLentilleDTO dto = new OrdonnanceLentilleDTO();
        dto.setPrescripteur("Dr Alami");
        dto.setDateEmission(LocalDate.of(2026, 1, 1));

        when(clientRepositry.findById(clientId)).thenReturn(Optional.of(client));
        when(ordonnanceLentilleRepository.findByClientId(clientId)).thenReturn(List.of(existante));

        assertThrows(APIException.class,
                () -> ordonnanceLentilleServiceImp.AddOrdLent(clientId, dto));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ordonnance est introuvable à la mise à jour")
    void test_UpdateOrdLent_when_id_introuvable() {
        Long id = 99L;

        when(ordonnanceLentilleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonnanceLentilleServiceImp.UpdateOrdLent(id, new OrdonnanceLentilleDTO()));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ordonnance est introuvable à la suppression")
    void test_DeleteOrdLent_when_id_introuvable() {
        Long id = 99L;

        when(ordonnanceLentilleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonnanceLentilleServiceImp.DeleteOrdLent(id));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ordonnance est introuvable à la lecture")
    void test_getOrdLenById_when_id_introuvable() {
        Long id = 99L;

        when(ordonnanceLentilleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonnanceLentilleServiceImp.getOrdLenById(id));
    }
}