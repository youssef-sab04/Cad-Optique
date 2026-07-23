package cad.project.service;

import cad.project.playload.OrdonnanceLunetteDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OrdonnanceOcrParser {

    private static final String NUMBER = "[+-]?\\s?\\d+[.,]?\\d*";
    private static final String OD_LABEL = "(?:O\\.?D\\.?\\b|Oeil\\s*droit|[OŒ]EIL\\s*DROIT)";
    private static final String OG_LABEL = "(?:O\\.?G\\.?\\b|Oeil\\s*gauche|[OŒ]EIL\\s*GAUCHE)";
    private static final Pattern AXE_PATTERN = Pattern.compile("(" + NUMBER + ")\\s*°");
    private static final Pattern ADD_KEYWORD_PATTERN = Pattern.compile("(?:add(?:ition)?)\\D{0,6}(" + NUMBER + ")", Pattern.CASE_INSENSITIVE);
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBER);

    private static final Pattern DOCTEUR_LABEL = Pattern.compile("(?:Dr\\.?|Docteur)\\s{0,3}:?\\s{0,3}", Pattern.CASE_INSENSITIVE);
    private static final Pattern NOM_PATTERN = Pattern.compile("[\\p{Lu}][\\p{L}.\\-]*(?:\\s[\\p{Lu}][\\p{L}.\\-]*){0,4}");

    private static final Map<String, Integer> MOIS = new LinkedHashMap<>();
    static {
        MOIS.put("janvier", 1); MOIS.put("fevrier", 2); MOIS.put("février", 2); MOIS.put("mars", 3);
        MOIS.put("avril", 4); MOIS.put("mai", 5); MOIS.put("juin", 6); MOIS.put("juillet", 7);
        MOIS.put("aout", 8); MOIS.put("août", 8); MOIS.put("septembre", 9); MOIS.put("octobre", 10);
        MOIS.put("novembre", 11); MOIS.put("decembre", 12); MOIS.put("décembre", 12);
    }
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "(\\d{1,2})\\s+(" + String.join("|", MOIS.keySet()) + ")\\s+(\\d{4})", Pattern.CASE_INSENSITIVE);

    public OrdonnanceLunetteDTO parse(String text) {
        OrdonnanceLunetteDTO dto = new OrdonnanceLunetteDTO();
        dto.setSphereOd(0f); dto.setCylindreOd(0f); dto.setAxeOd(0); dto.setAdditionOd(0f);
        dto.setSphereOg(0f); dto.setCylindreOg(0f); dto.setAxeOg(0); dto.setAdditionOg(0f);
        dto.setPrescripteur(extractPrescripteur(text));
        dto.setDateEmission(extractDateEmission(text));

        Matcher odM = Pattern.compile(OD_LABEL, Pattern.CASE_INSENSITIVE).matcher(text);
        Matcher ogM = Pattern.compile(OG_LABEL, Pattern.CASE_INSENSITIVE).matcher(text);
        int odIndex = odM.find() ? odM.start() : -1;
        int ogIndex = ogM.find() ? ogM.start() : -1;

        String odSegment = "";
        String ogSegment = "";
        if (odIndex >= 0) {
            int end = (ogIndex > odIndex) ? ogIndex : boundSegment(text, odIndex);
            odSegment = text.substring(odIndex, end);
        }
        if (ogIndex >= 0) {
            int end = boundSegment(text, ogIndex);
            ogSegment = text.substring(ogIndex, end);
        }

        Float globalAdd = extractAddKeyword(text);

        fillEye(odSegment, dto, true, globalAdd);
        fillEye(ogSegment, dto, false, globalAdd);

        if (dto.getSphereOd() == 0f && dto.getCylindreOd() == 0f && dto.getAxeOd() == 0) {
            parseParChamps(text, dto);
            if (dto.getAdditionOd() == 0f && globalAdd != null) dto.setAdditionOd(globalAdd);
            if (dto.getAdditionOg() == 0f && globalAdd != null) dto.setAdditionOg(globalAdd);
        }

        if (compterOccurrences(text, ADD_WORD_PATTERN) <= 1) {
            if (dto.getAdditionOd() == 0f && dto.getAdditionOg() != 0f) dto.setAdditionOd(dto.getAdditionOg());
            if (dto.getAdditionOg() == 0f && dto.getAdditionOd() != 0f) dto.setAdditionOg(dto.getAdditionOd());
        }

        return dto;
    }

    private static final Pattern ADD_WORD_PATTERN = Pattern.compile("add(?:ition)?(?:\\s*\\(add\\))?", Pattern.CASE_INSENSITIVE);

    // Marque la fin du bloc "œil" quand il faut élargir la fenêtre : notes/signature qui
    // suivent le tableau, pas les données de l'œil.
    private static final Pattern STOP_PATTERN = Pattern.compile(
            "\\b(correction|monture|recommand\\w*|verres\\s+progressif\\w*|antireflet\\w*|durcis|" +
                    "signature|cachet|n[°º]?\\s*ordre|docteur)\\b", Pattern.CASE_INSENSITIVE);

    // Une ligne = les données d'un œil dans la plupart des formats. Mais certains OCR
    // (Gemini/GPT) éclatent chaque nombre sur sa propre ligne : dans ce cas la ligne
    // seule ne contient presque aucun chiffre, donc on élargit jusqu'au prochain
    // marqueur de fin de bloc (STOP_PATTERN) ou à une limite de secours.
    private int boundSegment(String text, int start) {
        int nl = text.indexOf('\n', start);
        int ligneEnd = nl >= 0 ? nl : text.length();
        Matcher chiffreM = NUMBER_PATTERN.matcher(text.substring(start, ligneEnd));
        int nbChiffres = 0;
        while (chiffreM.find()) nbChiffres++;
        if (nbChiffres >= 2) return ligneEnd;

        Matcher stopM = STOP_PATTERN.matcher(text);
        int cap = Math.min(text.length(), start + 300);
        if (stopM.find(start) && stopM.start() < cap) return stopM.start();
        return cap;
    }

    private int compterOccurrences(String text, Pattern pattern) {
        Matcher m = pattern.matcher(text);
        int count = 0;
        while (m.find()) count++;
        return count;
    }

    // Fallback pour les ordonnances en tableau à 2 colonnes (ex: "Sph : +2.50   Sph : +2.25")
    // ou l'OCR renvoie les 2 valeurs OD/OG sur la meme ligne, apres un seul label de champ.
    private static final Pattern SPH_LABEL = Pattern.compile("Sph(?:[eè]re)?\\s*:?", Pattern.CASE_INSENSITIVE);
    private static final Pattern CYL_LABEL = Pattern.compile("Cyl(?:indre)?\\s*:?", Pattern.CASE_INSENSITIVE);
    private static final Pattern AXE_LABEL = Pattern.compile("Axe\\s*:?", Pattern.CASE_INSENSITIVE);

    private void parseParChamps(String text, OrdonnanceLunetteDTO dto) {
        List<Float> sph = extraireDeuxValeurs(text, SPH_LABEL, false);
        List<Float> cyl = extraireDeuxValeurs(text, CYL_LABEL, false);
        List<Float> axe = extraireDeuxValeurs(text, AXE_LABEL, true);

        if (sph.size() == 2) { dto.setSphereOd(sph.get(0)); dto.setSphereOg(sph.get(1)); }
        if (cyl.size() == 2) { dto.setCylindreOd(cyl.get(0)); dto.setCylindreOg(cyl.get(1)); }
        if (axe.size() == 2) { dto.setAxeOd(axe.get(0).intValue()); dto.setAxeOg(axe.get(1).intValue()); }
    }

    private List<Float> extraireDeuxValeurs(String text, Pattern labelPattern, boolean avecDegre) {
        List<Float> result = new ArrayList<>();
        Matcher labelM = labelPattern.matcher(text);
        if (!labelM.find()) return result;

        int start = labelM.end();
        int finLigne = text.indexOf('\n', start);
        int end = finLigne >= 0 ? finLigne : Math.min(text.length(), start + 60);
        String ligne = text.substring(start, end);

        Pattern nombrePattern = avecDegre ? AXE_PATTERN : NUMBER_PATTERN;
        Matcher numM = nombrePattern.matcher(ligne);
        while (numM.find() && result.size() < 2) {
            String v = (avecDegre ? numM.group(1) : numM.group()).trim();
            if (v.equals("+") || v.equals("-") || v.isEmpty()) continue;
            try {
                result.add(cleanNum(v));
            } catch (NumberFormatException ignored) {}
        }
        return result;
    }

    private void fillEye(String segment, OrdonnanceLunetteDTO dto, boolean isDroit, Float globalAdd) {
        if (segment.isEmpty()) return;

        Matcher axeM = AXE_PATTERN.matcher(segment);
        Integer axe = null;
        String cleaned = segment;
        if (axeM.find()) {
            axe = extraireAxe(axeM.group(1));
            cleaned = cleaned.substring(0, axeM.start()) + " " + cleaned.substring(axeM.end());
        }

        Float addFromKeyword = extractAddKeyword(segment);
        Matcher addM = ADD_KEYWORD_PATTERN.matcher(cleaned);
        if (addM.find()) {
            cleaned = cleaned.substring(0, addM.start()) + " " + cleaned.substring(addM.end());
        }

        List<Float> nums = new ArrayList<>();
        Matcher numM = NUMBER_PATTERN.matcher(cleaned);
        while (numM.find()) {
            String v = numM.group().trim();
            if (v.equals("+") || v.equals("-") || v.isEmpty()) continue;
            try {
                nums.add(cleanNum(v));
            } catch (NumberFormatException ignored) {}
        }

        Float sphere = nums.size() > 0 ? nums.get(0) : null;
        Float cylindre = nums.size() > 1 ? nums.get(1) : null;
        Float addFromPosition = nums.size() == 3 ? nums.get(2) : null;

        Float add = addFromKeyword != null ? addFromKeyword : (addFromPosition != null ? addFromPosition : globalAdd);

        if (isDroit) {
            if (sphere != null) dto.setSphereOd(sphere);
            if (cylindre != null) dto.setCylindreOd(cylindre);
            if (axe != null) dto.setAxeOd(axe);
            if (add != null) dto.setAdditionOd(add);
        } else {
            if (sphere != null) dto.setSphereOg(sphere);
            if (cylindre != null) dto.setCylindreOg(cylindre);
            if (axe != null) dto.setAxeOg(axe);
            if (add != null) dto.setAdditionOg(add);
        }
    }

    private Float extractAddKeyword(String segment) {
        Matcher m = ADD_KEYWORD_PATTERN.matcher(segment);
        if (m.find()) return cleanNum(m.group(1));
        return null;
    }

    // Nom du prescripteur : on prend la ligne après "Dr."/"Docteur", et on exige que le
    // nom commence juste après le label (sinon un champ vide suivi d'un autre label sur
    // la même ligne, ex. "Docteur :     Date :", ferait lire "Date" comme un nom).
    private String extractPrescripteur(String text) {
        Matcher labelM = DOCTEUR_LABEL.matcher(text);
        if (!labelM.find()) return null;
        int start = labelM.end();
        int finLigne = text.indexOf('\n', start);
        int end = finLigne >= 0 ? finLigne : text.length();
        String ligne = text.substring(start, end);
        Matcher nomM = NOM_PATTERN.matcher(ligne);
        if (nomM.find() && nomM.start() == 0) {
            return nomM.group().trim();
        }
        return null;
    }

    // Date de l'examen : uniquement le format textuel "jour Mois année" (ex. "28 Octobre
    // 2023"), pour ne jamais confondre avec une date de naissance au format jj/mm/aaaa.
    private LocalDate extractDateEmission(String text) {
        Matcher m = DATE_PATTERN.matcher(text);
        if (!m.find()) return null;
        int jour = Integer.parseInt(m.group(1));
        Integer mois = MOIS.get(m.group(2).toLowerCase());
        int annee = Integer.parseInt(m.group(3));
        if (mois == null) return null;
        try {
            return LocalDate.of(annee, mois, jour);
        } catch (Exception e) {
            return null;
        }
    }

    private int extraireAxe(String raw) {
        String digits = raw.replaceAll("[^0-9]", "");
        return digits.isEmpty() ? 0 : Integer.parseInt(digits);
    }

    private float cleanNum(String value) {
        return Float.parseFloat(value.replaceAll("\\s+", "").replace(",", "."));
    }
}