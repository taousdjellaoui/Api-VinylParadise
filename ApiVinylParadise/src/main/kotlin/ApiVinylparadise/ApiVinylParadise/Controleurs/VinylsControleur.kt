package ApiVinylparadise.ApiVinylParadise.Controleur

import ApiVinylparadise.ApiVinylParadise.Domaine.Vinyl
import ApiVinylparadise.ApiVinylParadise.Domaine.VinylService
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.ResourceNotFoundException
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.InvalidVinylException
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.InvalidIdException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication

@RestController
@RequestMapping("/vinyls")
@Tag(
    name = "Vinyls",
    description = "Points d'accès pour gérer les enregistrements vinyles"
)
class VinylsControleur(val vinylService: VinylService) {


    @Operation(
        summary = "Obtenir tous les vinyles",
        description = "Retourne la liste de tous les vinyles disponibles.",
        responses = [
            ApiResponse(responseCode = "200", description = "Liste retournée avec succès."),
            ApiResponse(responseCode = "204", description = "Aucun vinyle trouvé.")
        ]
    )

    // Afficher les roles ou permissions de l'utilisateur connecté
    @GetMapping("/roles")
    fun afficherRolesUtilisateur(authentication: Authentication): ResponseEntity<List<String>> {
        val roles = authentication.authorities.map { it.authority } // Recup les permission et rôle de l'utilisateur
        return ResponseEntity.ok(roles)
    }

    // Lire la liste de tous les vinyls 

    @GetMapping
    @PreAuthorize("hasAuthority('read:vinyls')")
    fun obtenirTousLesVinyls(): ResponseEntity<List<Vinyl>> {
        val vinyls = vinylService.rechercherToutVinyls()
        return if (vinyls.isNotEmpty()) ResponseEntity.ok(vinyls) else ResponseEntity.noContent().build()
    }



    @Operation(
        summary = "Obtenir un vinyle par ID",
        description = "Retourne les détails d'un vinyle basé sur son identifiant.",
        responses = [
            ApiResponse(responseCode = "200", description = "Vinyle trouvé."),
            ApiResponse(responseCode = "404", description = "Le vinyle n'existe pas.")
        ]
    )

    // Lire un vinyl spécifique 
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('read:vinyl')")
    fun obtenirVinylParId(@PathVariable id: Int): ResponseEntity<Vinyl> {
        if (id <= 0) throw InvalidIdException("L'ID doit être supérieur à 0.")
        val vinyl = vinylService.trouverVinylParId(id)
        return ResponseEntity.ok(vinyl)
    }

    // Rechercher des vinyls par titre ou genre 


    @Operation(
        summary = "Rechercher des vinyles par titre d'album",
        description = "Retourne une liste de vinyles associés au titre d'album fourni.",
        responses = [
            ApiResponse(responseCode = "200", description = "Vinyles trouvés."),
            ApiResponse(responseCode = "204", description = "Aucun vinyle trouvé.")
        ]
    )

    // Rechercher des vinyls par titre ou genre 

    @GetMapping(params = ["titreAlbum"])
    @PreAuthorize("hasAuthority('search:vinyls')")
    fun chercherVinylsParTitreAlbum(@RequestParam titreAlbum: String): ResponseEntity<List<Vinyl>> {
        if (titreAlbum.isBlank()) throw InvalidVinylException("Le titre de l'album ne peut pas être vide.")
        val vinyls = vinylService.trouverParTitreAlbum(titreAlbum)
        return if (vinyls.isNotEmpty()) ResponseEntity.ok(vinyls) else ResponseEntity.noContent().build()
    }


    @Operation(
        summary = "Créer un nouveau vinyle",
        description = "Ajoute un nouveau vinyle au système.",
        responses = [
            ApiResponse(responseCode = "201", description = "Vinyle créé avec succès."),
            ApiResponse(responseCode = "400", description = "Erreur dans les données fournies.")
        ]
    )

    @GetMapping(params = ["genre"])
    @PreAuthorize("hasAuthority('search:vinyls')")
    fun chercherVinylsParGenre(@RequestParam genre: String): ResponseEntity<List<Vinyl>> {
        if (genre.isBlank()) throw InvalidVinylException("Le nom du genre ne peut pas être vide.")
        val vinyls = vinylService.trouverParGenre(genre)
        return if (vinyls.isNotEmpty()) ResponseEntity.ok(vinyls) else ResponseEntity.noContent().build()
    }

    // Ajouter un nouveau vinyl


    @PostMapping
    @PreAuthorize("hasAuthority('create:vinyls')")
    fun creerVinyl(@RequestBody vinyl: Vinyl): ResponseEntity<Vinyl> {
        val createdVinyl = vinylService.ajoutVinyl(vinyl)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdVinyl)
    }


    // Mettre à jour un vinyl (update:vinyls ou update:own_vinyls)

    @Operation(
        summary = "Mettre à jour un vinyle par ID",
        description = "Met à jour les détails d'un vinyle existant.",
        responses = [
            ApiResponse(responseCode = "200", description = "Vinyle mis à jour."),
            ApiResponse(responseCode = "404", description = "Le vinyle n'existe pas.")
        ]
    )

    // Mettre à jour un vinyl (update:vinyls ou update:own_vinyls)

    @PutMapping("/{id}")
@PreAuthorize("hasAuthority('update:vinyls') or hasAuthority('update:own_vinyls')")
fun mettreAJourVinyl(
    @PathVariable id: Int,
    @RequestBody vinyl: Vinyl,
    authentication: Authentication
): ResponseEntity<Vinyl> {
    if (id <= 0) throw InvalidIdException("L'ID doit être supérieur à 0.")

    val emailUtilisateurConnecte = authentication.name 
    val vinylExistant = vinylService.trouverVinylParId(id)

    // verifie que le vinyl existe
    if (vinylExistant == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }

    // verifie les permissions et la propriété du vinyl
    if (authentication.authorities.any { it.authority == "update:own_vinyls" } &&
        vinylExistant.utilisateur.email != emailUtilisateurConnecte
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }


    // verifie que le vinyl existe
    if (vinylExistant == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }


    @Operation(
        summary = "Supprimer un vinyle par ID",
        description = "Supprime un vinyle du système.",
        responses = [
            ApiResponse(responseCode = "204", description = "Vinyle supprimé avec succès."),
            ApiResponse(responseCode = "404", description = "Le vinyle n'existe pas.")
        ]
    )

    // verifie les permissions et la propriété du vinyl
    if (authentication.authorities.any { it.authority == "update:own_vinyls" } &&
        vinylExistant.utilisateur.email != emailUtilisateurConnecte
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    // met à jour le vinyl
    val updatedVinyl = vinylService.modifierVinyl(id, vinyl)
    return ResponseEntity.ok(updatedVinyl)
}


    // Supprimer un vinyl (delete:vinyls ou delete:own_vinyls)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('delete:vinyls') or hasAuthority('delete:own_vinyls')")
    fun supprimerVinyl(
        @PathVariable id: Int,
        authentication: Authentication
    ): ResponseEntity<Void> {
        if (id <= 0) throw InvalidIdException("L'ID doit être supérieur à 0.")

        val emailUtilisateurConnecte = authentication.name // mail ou ID de l'utilisateur connecté
        val vinylExistant = vinylService.trouverVinylParId(id)

        // Si l'utilisateur n'a que la permission delete:own_vinyls, verifier qu'il est propriétaire du vinyl
        if (authentication.authorities.any { it.authority == "delete:own_vinyls" } &&
            vinylExistant.utilisateur.email != emailUtilisateurConnecte
        ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        vinylService.supprimerVinyl(id)
        return ResponseEntity.noContent().build()
    }
}

