package uadb.location.cdn_project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;
import uadb.location.cdn_project.dto.controller.mediaController.CreateFiles.CreateFilesResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/cdn")
@RestController
public class MediaController {

    @PostMapping(value = "/{proprietaireId}/{chambreId}")
    public ResponseEntity<List<CreateFilesResponse>> createFiles(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable Long proprietaireId,
            @PathVariable Long chambreId,
            HttpServletRequest request
    ) {
        String baseFolderPath = System.getProperty("user.dir") + File.separator + "Uploads";
        String ownerFolderPath = baseFolderPath + File.separator + proprietaireId + File.separator + chambreId;
        File ownerDirectory = new File(ownerFolderPath);

        if (!ownerDirectory.exists() && !ownerDirectory.mkdirs()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Impossible de créer le dossier de destination.");
        }

        List<CreateFilesResponse> returnedResponse = new ArrayList<>();

        try {
            Arrays.stream(files).forEach(file -> {
                String originalFilename = file.getOriginalFilename();
                String extension = "";

                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);
                }

                String uniqueFileName = UUID.randomUUID() + "." + extension;
                File destinationFile = new File(ownerDirectory, uniqueFileName);

                try {
                    file.transferTo(destinationFile);

                    String fileUrl = request.getScheme() + "://" +
                            request.getServerName() + ":" +
                            request.getServerPort() +
                            File.separator +
                            "api" +
                            File.separator +
                            "cdn" +
                            File.separator +
                            proprietaireId +
                            File.separator +
                            chambreId +
                            File.separator +
                            uniqueFileName;

                    returnedResponse.add(new CreateFilesResponse(fileUrl));

                } catch (IOException e) {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File upload failed: " + e.getMessage());
                }
            });

            return ResponseEntity.ok(returnedResponse);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors du traitement des fichiers : " + e.getMessage());
        }
    }

    @GetMapping("/{proprietaireId}/{chambreId}/{filename}")
    public ResponseEntity<byte[]> getImage(
            @PathVariable Long proprietaireId,
            @PathVariable Long chambreId,
            @PathVariable String filename) {

        String folderPath = System.getProperty("user.dir") + "/Uploads/" + proprietaireId + "/" + chambreId;
        File imageFile = new File(folderPath, filename);

        if (!imageFile.exists()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image non trouvée");
        }

        try {
            byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
            String contentType = Files.probeContentType(imageFile.toPath());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType != null ? contentType : "application/octet-stream"))
                    .body(imageBytes);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la lecture du fichier : " + e.getMessage());
        }
    }

    @DeleteMapping("/{proprietaireId}/{chambreId}/{filename}")
    public ResponseEntity<?> deleteMedia(
            @PathVariable Long proprietaireId,
            @PathVariable Long chambreId,
            @PathVariable String filename
    ) {

        String baseFolderPath = System.getProperty("user.dir") + File.separator + "Uploads";
        String ownerFolderPath = baseFolderPath + File.separator + proprietaireId + File.separator + chambreId;
        File targetFile = new File(ownerFolderPath, filename);

        try {
            // Vérification de l'existence du fichier
            if (!targetFile.exists()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Le fichier spécifié n'existe pas.");
            }

            // Vérification de sécurité : s'assurer que le fichier est bien dans le dossier attendu
            String canonicalFilePath = targetFile.getCanonicalPath();
            String canonicalOwnerPath = new File(ownerFolderPath).getCanonicalPath();

            if (!canonicalFilePath.startsWith(canonicalOwnerPath)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès non autorisé au fichier.");
            }

            // Suppression du fichier
            if (!targetFile.delete()) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Impossible de supprimer le fichier.");
            }

            // Vérification si le dossier de la chambre est vide et le supprimer si c'est le cas
            File chambreDirectory = new File(ownerFolderPath);
            if (chambreDirectory.exists() && chambreDirectory.isDirectory()) {
                String[] files = chambreDirectory.list();
                if (files != null && files.length == 0) {
                    chambreDirectory.delete();

                    // Vérification si le dossier du propriétaire est vide et le supprimer si c'est le cas
                    File proprietaireDirectory = new File(baseFolderPath + File.separator + proprietaireId);
                    if (proprietaireDirectory.exists() && proprietaireDirectory.isDirectory()) {
                        String[] proprietaireFiles = proprietaireDirectory.list();
                        if (proprietaireFiles != null && proprietaireFiles.length == 0) {
                            proprietaireDirectory.delete();
                        }
                    }
                }
            }

            return ResponseEntity.ok().build();

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la suppression du fichier : " + e.getMessage());
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur inattendue lors de la suppression : " + e.getMessage());
        }
    }
}
