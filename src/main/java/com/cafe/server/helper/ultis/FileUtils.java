package com.cafe.server.helper.ultis;

import com.cafe.server.exception.StorageException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
    private static final String PATH = "file/upload";

    public static String getExtension(String contentType) {
        MediaType mediaType = MediaType.valueOf(contentType);

        if (mediaType.equals(MediaType.IMAGE_PNG)) {
            return ".png";
        } else if (mediaType.equals(MediaType.IMAGE_JPEG)) {
            return ".jpg";
        } else if (mediaType.equals(MediaType.IMAGE_GIF)) {
            return ".gif";
        }
        throw new StorageException("File is not supported.");
    }

    public static String storeFile(MultipartFile file) {
        Path rootLocation = Paths.get(PATH);
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Cannot upload file, please try it again.");
        }
        String extension = getExtension(file.getContentType());
        String storageName = UUID.randomUUID() + extension;
        Path destinationFile = rootLocation.resolve(Paths.get(storageName)).normalize().toAbsolutePath();
        try {
            file.transferTo(destinationFile);
        } catch (IOException e) {
            throw new StorageException(e);
        }
        return destinationFile.toString();
    }

    public static byte[] getFileByte(String filePath) throws StorageException {
        Path path = Paths.get(filePath);
        try (FileInputStream file = new FileInputStream(path.toString());
             FileChannel channel = file.getChannel();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int bufferSize = 8196;
            if (bufferSize > channel.size()) {
                bufferSize = (int) channel.size();
            }
            ByteBuffer buff = ByteBuffer.allocate(bufferSize);
            while (channel.read(buff) > 0) {
                out.write(buff.array(), 0, buff.position());
                buff.clear();
            }
            return out.toByteArray();
        } catch (IOException ex) {
            throw new StorageException("Cannot view file now, please try it later.");
        }
    }
}
