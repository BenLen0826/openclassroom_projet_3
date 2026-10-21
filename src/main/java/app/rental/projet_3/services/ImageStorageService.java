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

	/**
	 * Vérifie que le fichier est une image autorisée (JPEG, PNG, GIF ou WEBP),
	 * puis l'enregistre dans le dossier d'upload sous un nom unique.
	 *
	 * @param file l'image envoyée dans le formulaire
	 * @return le nom du fichier enregistré
	 * @throws InvalidFileException si le fichier est vide ou n'est pas une image autorisée (400)
	 * @throws StorageException si le fichier ne peut pas être écrit sur le disque (500)
	 */
	public String save(MultipartFile file) {
		if (file.isEmpty()) {
			throw new InvalidFileException("Picture is empty");
		}

		String contentType = file.getContentType();
		String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

		// Vérification du type MIME et de l'extension (le contenu du fichier n'est pas analysé)
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
