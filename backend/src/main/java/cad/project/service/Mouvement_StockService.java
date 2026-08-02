package cad.project.service;

import cad.project.playload.Mouvement_StockResponse;

public interface Mouvement_StockService {
    Mouvement_StockResponse getAllMvt(Integer pageNumber, Integer pageSize, String sortOrder);

    Mouvement_StockResponse getAllMvtProd(Long productId, Integer pageNumber, Integer pageSize, String sortOrder);
}
