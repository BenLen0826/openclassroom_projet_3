package app.rental.projet_3.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import app.rental.projet_3.exceptions.InvalidFileException;
import app.rental.projet_3.exceptions.StorageException;

@Service
public class ImageStorageService {

	// Chemin dans l'URL, a ne pas confondre avec app.upload-dir (dossier sur le disque)
	public static final String URL_PATH = "/uploads/";

	// Liste blanche : pas de SVG, qui peut contenir du JavaScript
	private static final List<String> ALLOWED_CONTENT_TYPES = List.of("image/jpeg", "image/png", "image/gif", "image/webp");

	private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "gif", "webp");

	private final Path uploadDir;

	public ImageStorageService(@Value("${app.upload-dir}") String uploadDir) {
		this.uploadDir = Path.of(uploadDir);
	}

	public String save(MultipartFile file) {
		if (file.isEmpty()) {
			throw new InvalidFileException("Picture is empty");
		}

		String contentType = file.getContentType();
		String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

		// Vérifications du type/extention/contenu de l'image
		if (contentType == null || extension == null
				|| !ALLOWED_CONTENT_TYPES.contains(contentType)
				|| !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
			throw new InvalidFileException("Picture must be a JPEG, PNG, GIF or WEBP image");
		}

		// Attribution de nom unique à l'image
		String filename = UUID.randomUUID() + "." + extension.toLowerCase();

		try {
			Files.createDirectories(uploadDir);
			Files.copy(file.getInputStream(), uploadDir.resolve(filename));
		} catch (IOException e) {
			throw new StorageException("Could not save file", e);
		}

		return filename;
	}
}
