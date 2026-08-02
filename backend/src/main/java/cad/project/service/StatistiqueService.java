package cad.project.service;

import cad.project.playload.DashboardStatsDTO;

public interface StatistiqueService {
    DashboardStatsDTO getDashboardStats(int annee);
    byte[] genererRapportPdf(int annee);
}