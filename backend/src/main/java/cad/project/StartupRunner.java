package cad.project;

import cad.project.repositries.DevisRepositry;
import cad.project.repositries.Mouvement_StockRepositry;
import cad.project.repositries.SaleOrderRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class StartupRunner implements CommandLineRunner {


    @Autowired
    private Mouvement_StockRepositry mouvementStockRepositry;

    @Autowired
    private SaleOrderRepositry saleOrderRepositry;

    @Autowired
    private DevisRepositry devisRepositry;



    @Override
    public void run(String... args) throws Exception {
        System.out.println("Application démarrée !");

       // saleOrderRepositry.deleteAll();
        // devisRepositry.deleteAll();

        // Ton code ici
    }
}