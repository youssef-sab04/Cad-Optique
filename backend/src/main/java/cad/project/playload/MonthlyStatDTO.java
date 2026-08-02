package cad.project.playload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyStatDTO {
    private int mois;
    private double ca;
    private long nombreVentes;
    private long nombreDevis;
    private double tauxConversion;
}