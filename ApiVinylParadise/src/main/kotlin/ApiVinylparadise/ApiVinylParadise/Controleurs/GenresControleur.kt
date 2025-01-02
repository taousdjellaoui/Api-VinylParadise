package ApiVinylparadise.ApiVinylParadise.Controleur

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.*
import ApiVinylparadise.ApiVinylParadise.Domaine.*

@RestController
@RequestMapping("/genres")
@Tag(
    name = "Genres",
    description = "Points d'accès pour gérer les genres de vinyles."
)
class GenresControleur(val genresService: GenresService) {

    @Operation(
        summary = "Obtenir tous les genres.",
        description = "Retourne une liste de tous les genres disponibles.",
        responses = [
            ApiResponse(responseCode = "200", description = "Liste des genres récupérée avec succès."),
            ApiResponse(responseCode = "204", description = "Aucun genre trouvé.")
        ]
    )
    @GetMapping
    fun obtenirTousLesGenres(): ResponseEntity<List<Genre>> {
        val genres = genresService.obtenirTousLesGenres()
        return if (genres.isNotEmpty()) {
            ResponseEntity.ok(genres)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Obtenir un genre par ID.",
        description = "Retourne les informations d'un genre spécifique par son ID.",
        responses = [
            ApiResponse(responseCode = "200", description = "Genre trouvé avec succès."),
            ApiResponse(responseCode = "404", description = "Genre introuvable.")
        ]
    )
    @GetMapping("/{id}")
    fun obtenirGenreParId(@PathVariable id: Int): ResponseEntity<Genre> {
        if (id <= 0) {
            throw InvalidIdException("L'ID doit être supérieur à 0.")
        }
        val genre = genresService.obtenirGenreParId(id)
            ?: throw GenreNotFoundException("Le genre avec l'ID $id est introuvable.")
        return ResponseEntity.ok(genre)
    }

    @Operation(
        summary = "Rechercher des genres par nom.",
        description = "Retourne une liste de genres correspondant au nom fourni.",
        responses = [
            ApiResponse(responseCode = "200", description = "Genres trouvés avec succès."),
            ApiResponse(responseCode = "404", description = "Aucun genre trouvé pour le nom fourni.")
        ]
    )
    @GetMapping(params = ["nom"])
    fun obtenirGenresParNom(@RequestParam nom: String): ResponseEntity<List<Genre>> {
        if (nom.isBlank()) {
            throw InvalidGenreNameException("Le nom de genre ne peut pas être vide.")
        }
        val genres = genresService.obtenirGenresParNom(nom)
        return if (genres.isNotEmpty()) {
            ResponseEntity.ok(genres)
        } else {
            throw GenreNotFoundException("Aucun genre trouvé pour le nom fourni.")
        }
    }

    @Operation(
        summary = "Ajouter un nouveau genre.",
        description = "Ajoute un nouveau genre dans le système.",
        responses = [
            ApiResponse(responseCode = "201", description = "Genre ajouté avec succès."),
            ApiResponse(responseCode = "400", description = "Les données fournies sont invalides.")
        ]
    )
    @PostMapping
    fun ajouterGenre(@RequestBody genre: Genre): ResponseEntity<Genre> {
        validateGenreData(genre)
        val nouveauGenre = genresService.ajouterGenre(genre)
            ?: throw InvalidGenreDataException("Impossible de créer le genre. Données invalides.")
        return ResponseEntity.status(HttpStatus.CREATED).body(nouveauGenre)
    }

    @Operation(
        summary = "Modifier un genre existant.",
        description = "Met à jour les informations d'un genre existant.",
        responses = [
            ApiResponse(responseCode = "200", description = "Genre modifié avec succès."),
            ApiResponse(responseCode = "404", description = "Genre introuvable."),
            ApiResponse(responseCode = "400", description = "Les données fournies sont invalides.")
        ]
    )
    @PutMapping("/{id}")
    fun modifierGenre(@PathVariable id: Int, @RequestBody genre: Genre): ResponseEntity<Genre> {
        if (id <= 0) {
            throw InvalidIdException("L'ID doit être supérieur à 0.")
        }
        validateGenreData(genre)
        val genreModifie = genresService.modifierGenre(id, genre)
            ?: throw GenreNotFoundException("Le genre avec l'ID $id est introuvable.")
        return ResponseEntity.ok(genreModifie)
    }

    @Operation(
        summary = "Supprimer un genre.",
        description = "Supprime un genre en utilisant son ID.",
        responses = [
            ApiResponse(responseCode = "204", description = "Genre supprimé avec succès."),
            ApiResponse(responseCode = "404", description = "Genre introuvable.")
        ]
    )
    @DeleteMapping("/{id}")
    fun supprimerGenre(@PathVariable id: Int): ResponseEntity<Void> {
        if (id <= 0) {
            throw InvalidIdException("L'ID doit être supérieur à 0.")
        }
        val genre = genresService.obtenirGenreParId(id)
            ?: throw GenreNotFoundException("Le genre avec l'ID $id est introuvable.")
        genresService.supprimerGenre(id)
        return ResponseEntity.noContent().build()
    }

    private fun validateGenreData(genre: Genre) {
        if (genre.nom.isBlank()) {
            throw InvalidGenreNameException("Le nom du genre ne peut pas être vide.")
        }
    }
}
