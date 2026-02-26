package org.example.library.solid.o.solution;

/**
 * ✅ OCP — Interface Strategy.
 * Pour ajouter un nouveau type de livre, il suffit de créer une nouvelle
 * implémentation de cette interface. On n'a JAMAIS besoin de modifier
 * BookRater — elle est fermée à la modification, ouverte à l'extension.
 */
public interface RatingStrategy {

    /**
     * Calcule la note du livre en fonction du nombre d'avis.
     * @param reviewCount nombre d'avis
     * @return note entre 0 et 5
     */
    double calculate(int reviewCount);

    /**
     * Identifiant du type de livre géré par cette stratégie.
     */
    String getBookType();
}
