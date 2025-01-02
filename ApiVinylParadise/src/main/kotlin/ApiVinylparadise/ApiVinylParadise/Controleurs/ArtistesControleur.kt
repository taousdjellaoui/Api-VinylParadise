package ApiVinylparadise.ApiVinylParadise.Controleur

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody
import ApiVinylparadise.ApiVinylParadise.Domaine.*

@RestController
@RequestMapping("/artistes")
@Tag(
    name = "Artistes",
    description = "Points d'accès aux ressources liées aux artistes."
)
class ArtistesControleur(val artistesService: ArtistesService) {

    @Operation(
        summary = "Rechercher tous les artistes.",
        description = "Retourne une liste de tous les artistes disponibles.",
        responses = [
            ApiResponse(responseCode = "200", description = "Liste des artistes récupérée avec succès."),
            ApiResponse(responseCode = "404", description = "Aucun artiste trouvé.")
        ]
    )
    @GetMapping
    fun rechercheArtistes(): ResponseEntity<List<Artist>> {
        val artistes = artistesService.rechercheArtistes()
        return if (artistes.isNotEmpty()) {
            ResponseEntity.ok(artistes)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Rechercher un artiste par ID.",
        description = "Retourne les informations d'un artiste en utilisant son ID. ",
        responses = [
            ApiResponse(responseCode = "200", description = "Artiste trouvé."),
            ApiResponse(responseCode = "404", description = "Artiste introuvable.")
        ]
    )
    @GetMapping("/{id}")
    fun rechercheArtisteParId(@PathVariable id: Int): ResponseEntity<Artist> {
        val artiste = artistesService.rechercheArtisteParId(id)
        return if (artiste != null) {
            ResponseEntity.ok(artiste)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @Operation(
        summary = "Rechercher des artistes par nom.",
        description = "Retourne une liste des artistes correspondant à un nom donné.",
        responses = [
            ApiResponse(responseCode = "200", description = "Liste des artistes trouvée."),
            ApiResponse(responseCode = "404", description = "Aucun artiste trouvé pour le nom donné.")
        ]
    )
    @GetMapping(params = ["nom"])
    fun rechercheArtistesParNom(@RequestParam nom: String): ResponseEntity<List<Artist>> {
        val artistes = artistesService.rechercheArtistesParNom(nom)
        return if (artistes.isNotEmpty()) {
            ResponseEntity.ok(artistes)
        } else {
            ResponseEntity.noContent().build()
        }
    }

    @Operation(
        summary = "Ajouter un nouvel artiste.",
        description = "Crée un nouvel artiste en utilisant les informations fournies.",
        responses = [
            ApiResponse(responseCode = "201", description = "Artiste créé avec succès."),
            ApiResponse(responseCode = "400", description = "Requête invalide ou création impossible.")
        ]
    )
    @PostMapping
    fun ajouterArtiste(@RequestBody artist: Artist): ResponseEntity<Artist> {
        val artisteCréer = artistesService.ajouterArtiste(artist)
        return if (artisteCréer != null) {
            ResponseEntity.status(HttpStatus.CREATED).body(artisteCréer)
        } else {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @Operation(
        summary = "Modifier un artiste.",
        description = "Met à jour les informations d'un artiste existant.",
        responses = [
            ApiResponse(responseCode = "200", description = "Artiste modifié avec succès."),
            ApiResponse(responseCode = "404", description = "Artiste introuvable.")
        ]
    )
    @PutMapping("/{id}")
    fun modifierArtiste(@PathVariable id: Int, @RequestBody artist: Artist): ResponseEntity<Artist> {
        val artisteModifié = artistesService.modifierArtiste(id, artist)
        return if (artisteModifié != null) {
            ResponseEntity.ok(artisteModifié)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @Operation(
        summary = "Supprimer un artiste.",
        description = "Supprime un artiste en utilisant son ID.",
        responses = [
            ApiResponse(responseCode = "204", description = "Artiste supprimé avec succès."),
            ApiResponse(responseCode = "404", description = "Artiste introuvable.")
        ]
    )
    @DeleteMapping("/{id}")
    fun deleteArtist(@PathVariable id: Int): ResponseEntity<Void> {
        artistesService.supprimerArtiste(id)
        return ResponseEntity.noContent().build()
    }
}
