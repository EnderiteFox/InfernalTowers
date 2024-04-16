package core.utils;

import api.utils.CharGrid;

import java.util.ArrayList;
import java.util.List;

public class ImplCharGrid implements CharGrid {
    private final List<String> tab = new ArrayList<>();

    @Override
    public void setChar(int x, int y, char chr) {
        for (int i = tab.size(); i < y; ++i) tab.add("");
        StringBuilder builder = new StringBuilder(tab.get(y));
        builder.append(" ".repeat(Math.max(0, y - builder.length())));
        builder.setCharAt(x, chr);
        tab.set(y, builder.toString());
    }

    @Override
    public void display() {
        tab.forEach(System.out::println);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        tab.forEach(builder::append);
        return builder.toString();
    }
}
