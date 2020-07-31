package fr.synchroneyes.mineral.Utils;

import java.util.LinkedList;

/**
 * Classe représentant une liste circulaire
 *
 * @param <E>
 */
public class CircularList<E> extends LinkedList<E> {

    /**
     * Permet de récuperer un index à une position donnée
     *
     * @param index
     * @return
     */
    @Override
    public E get(int index) {
        return super.get(index % size());
    }
}
