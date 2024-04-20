import api.utils.CharGrid;
import core.utils.ImplCharGrid;

public class CharGridTest {
    public static void main(String[] args) {
        CharGrid grid = new ImplCharGrid();
        for (int i = 0; i < 10; ++i) grid.setChar(i, i, 'X');
        CharGrid other = new ImplCharGrid();
        other.insertString(0, 0, "Ceci est un\npremier test");
        grid.insert(-5, -5, other);
        System.out.println(grid);
        System.out.println("-".repeat(20));
        other = new ImplCharGrid();
        other.insertString(0, 0, "Ceci est un deuxieme test".replaceAll(" ", "\n"));
        grid.addSidePanel(CharGrid.SidePanelDirection.RIGHT, other);
        System.out.println(grid);
    }
}
