package ci553.happyshop.utility;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

/** Tiles the given stages in a rows × cols grid on the primary screen. */
public final class WindowLayout {
    private WindowLayout() {}

    public static void tileGrid(List<Stage> stages, int rows, int cols, double gapPx) {
        if (stages == null || stages.isEmpty()) return;

        Rectangle2D area = Screen.getPrimary().getVisualBounds();
        double totalGapX = gapPx * (cols + 1);
        double totalGapY = gapPx * (rows + 1);
        double cellW = Math.max(360, (area.getWidth()  - totalGapX) / cols);
        double cellH = Math.max(260, (area.getHeight() - totalGapY) / rows);

        for (int i = 0; i < stages.size(); i++) {
            Stage s = stages.get(i);
            int r = i / cols, c = i % cols;
            double x = area.getMinX() + gapPx + c * (cellW + gapPx);
            double y = area.getMinY() + gapPx + r * (cellH + gapPx);
            s.setX(x);
            s.setY(y);
            // Only bump tiny windows; respect existing sizes if already set
            if (s.getWidth()  < 300) s.setWidth(cellW);
            if (s.getHeight() < 200) s.setHeight(cellH);
        }
    }
}