package fr.synchroneyes.mineral.Core.Coffre;

import java.util.LinkedList;

public enum Animations {

    FIVE_LINES_HEART("1-0-9-27-36-37-43-44-35-17-8-7-5-13-3-11-20-30-40-32-24-15"),
    FIVE_LINES_UP_TO_DOWN_LEFT_TO_RIGHT("0-9-18-27-36-37-28-19-10-1-2-11-20-29-38-39-30-21-12-3-4-5-6-7-8-17-16-15-14-13-22-23-24-25-26-35-34-33-32-31-40-41-42-43-44"),
    THREE_LINES_OUTLINES("0-9-18-19-20-21-22-23-24-25-26-17-8-7-6-5-4-3-2-1-11-12-13-14-15-16"),
    THREE_LINES_SIMPLE_PROGRESS_BAR("9-10-11-12-13-14-15-16-17"),
    FIVE_LINES_AROUND_THEN_CENTER("1-0-9-27-36-37-43-44-35-17-8-7-20-21-22-23-24");

    private String contenu;

    Animations(String contenu) {
        this.contenu = contenu;
    }

    /**
     * Converti une animation en une animation appliquable
     *
     * @return
     */
    public LinkedList<Integer> toList() {
        String[] tmp = contenu.split("-");
        LinkedList<Integer> liste = new LinkedList<>();

        for (String integer : tmp) {
            liste.add(Integer.parseInt(integer));
        }

        return liste;
    }
}
