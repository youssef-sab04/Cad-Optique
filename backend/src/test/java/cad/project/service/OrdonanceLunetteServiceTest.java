package cad.project.service;

import cad.project.exceptions.APIException;
import cad.project.exceptions.ResourceNotFoundException;
import cad.project.model.Client;
import cad.project.model.OrdonnanceLunette;
import cad.project.playload.OrdonnanceLunetteDTO;
import cad.project.repositries.ClientRepositry;
import cad.project.repositries.OrdonanceLunetteRepositry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrdonanceLunetteServiceTest {

    @Mock
    private ModelMapper modelMapper;
    @Mock
    private OrdonanceLunetteRepositry ordonanceLunetteRepositry;
    @Mock
    private ClientRepositry clientRepositry;
    @Mock
    private FileServiceImp fileServiceImp;

    @InjectMocks
    private OrdonanceLunetteServiceImp ordonanceLunetteServiceImp;

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable à l'ajout")
    void test_AddOrdLun_when_client_introuvable() {
        Long clientId = 99L;

        when(clientRepositry.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonanceLunetteServiceImp.AddOrdLun(clientId, new OrdonnanceLunetteDTO()));
    }

    @Test
    @DisplayName("Doit lever APIException quand une ordonnance identique existe déjà")
    void test_AddOrdLun_when_ordonnance_deja_existante() {
        Long clientId = 1L;
        Client client = new Client();

        OrdonnanceLunette existante = new OrdonnanceLunette();
        existante.setPrescripteur("Dr Alami");
        existante.setDateEmission(LocalDate.of(2026, 1, 1));

        OrdonnanceLunetteDTO dto = new OrdonnanceLunetteDTO();
        dto.setPrescripteur("Dr Alami");
        dto.setDateEmission(LocalDate.of(2026, 1, 1));

        when(clientRepositry.findById(clientId)).thenReturn(Optional.of(client));
        when(ordonanceLunetteRepositry.findByClientId(clientId)).thenReturn(List.of(existante));

        assertThrows(APIException.class,
                () -> ordonanceLunetteServiceImp.AddOrdLun(clientId, dto));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand le client est introuvable pour le scan")
    void test_addScanOrd_when_client_introuvable() {
        Long clientId = 99L;

        when(clientRepositry.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonanceLunetteServiceImp.addScanOrd(clientId, null));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ordonnance est introuvable à la mise à jour")
    void test_UpdateOrdLun_when_id_introuvable() {
        Long id = 99L;

        when(ordonanceLunetteRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonanceLunetteServiceImp.UpdateOrdLun(id, new OrdonnanceLunetteDTO()));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ordonnance est introuvable à la suppression")
    void test_DeleteOrdLun_when_id_introuvable() {
        Long id = 99L;

        when(ordonanceLunetteRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonanceLunetteServiceImp.DeleteOrdLun(id));
    }

    @Test
    @DisplayName("Doit lever ResourceNotFoundException quand l'ordonnance est introuvable à la lecture")
    void test_getOrdLunById_when_id_introuvable() {
        Long id = 99L;

        when(ordonanceLunetteRepositry.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> ordonanceLunetteServiceImp.getOrdLunById(id));
    }
}