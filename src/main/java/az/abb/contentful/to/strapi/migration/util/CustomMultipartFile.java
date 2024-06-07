package az.abb.contentful.to.strapi.migration.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

    private final File file;

    public CustomMultipartFile(File file) {
        this.file = file;
    }

    @NotNull
    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public String getOriginalFilename() {
        return file.getName();
    }

    @Override
    public String getContentType() {
        try {
            return Files.probeContentType(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error while extracting MIME type of file", e);
        }
    }

    @Override
    public boolean isEmpty() {
        return file.length() == 0;
    }

    @Override
    public long getSize() {
        return file.length();
    }

    @NotNull
    @Override
    public byte[] getBytes() throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    @NotNull
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public void transferTo(@NotNull File dest) throws IllegalStateException {
        throw new UnsupportedOperationException();
    }

}
