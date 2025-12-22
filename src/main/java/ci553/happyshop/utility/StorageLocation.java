package ci553.happyshop.utility;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public final class StorageLocation {

    public static final String imageFolder = "images/";
    public static final Path imageFolderPath = Paths.get(imageFolder);

    public static final String imageResetFolder = "images_resetDB";
    public static final Path imageResetFolderPath = Paths.get(imageResetFolder);

    public static final String ordersFolder = "orders";
    public static final Path ordersPath = Paths.get(ordersFolder);
    public static final Path orderedPath = ordersPath.resolve("ordered");
    public static final Path progressingPath = ordersPath.resolve("progressing");
    public static final Path collectedPath = ordersPath.resolve("collected");

    public static final String orderCounterFile = "orderCounter.txt";
    public static final Path orderCounterPath = ordersPath.resolve(orderCounterFile);

    private StorageLocation() { }

    public static void ensureAllDirectories() throws IOException {
        Files.createDirectories(imageFolderPath);
        Files.createDirectories(imageResetFolderPath);
        Files.createDirectories(orderedPath);
        Files.createDirectories(progressingPath);
        Files.createDirectories(collectedPath);
        Files.createDirectories(orderCounterPath.getParent());
    }

    public static Path resolveImage(String imageFileName) {
        if (imageFileName == null || imageFileName.isBlank())
            throw new IllegalArgumentException("imageFileName required");
        return imageFolderPath.resolve(imageFileName);
    }

    public static String imageUrl(String imageFileName) {
        if (imageFileName == null || imageFileName.isBlank())
            throw new IllegalArgumentException("imageFileName required");
        return imageFolder + imageFileName;
    }
}
