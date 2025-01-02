package ApiVinylparadise.ApiVinylParadise.Controleur

import ApiVinylparadise.ApiVinylParadise.Domaine.Services.UtilisateurService
import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/utilisateurs")
@Tag(name = "Utilisateurs", description = "Gestion des utilisateurs dans le système")
class UtilisateursControleur(private val utilisateurService: UtilisateurService) {

    @Operation(
        summary = "Afficher les rôles de l'utilisateur connecté",
        description = "Retourne les rôles (présents dans le JWT) de l'utilisateur authentifié.",
        responses = [
            ApiResponse(responseCode = "200", description = "Rôles de l'utilisateur retournés.")
        ]
    )
    @GetMapping("/roles")
    fun afficherRolesUtilisateur(authentication: Authentication): ResponseEntity<List<String>> {
        val roles = authentication.authorities.map { it.authority }
        return ResponseEntity.ok(roles)
    }

    @Operation(
        summary = "Obtenir tous les utilisateurs",
        description = "Retourne une liste de tous les utilisateurs enregistrés.",
        responses = [
            ApiResponse(responseCode = "200", description = "Liste des utilisateurs retournée."),
            ApiResponse(responseCode = "204", description = "Aucun utilisateur trouvé.")
        ]
    )
    @GetMapping
    @PreAuthorize("hasRole('utilisateur') or hasRole('admin')")
    fun obtenirTousLesUtilisateurs(): ResponseEntity<List<Utilisateur>> {
        val utilisateurs = utilisateurService.obtenirUtilisateurs()
        return if (utilisateurs.isNotEmpty())
            ResponseEntity.ok(utilisateurs)
        else
            ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Rechercher des utilisateurs par nom",
        description = "Retourne une liste d'utilisateurs correspondant au nom spécifié.",
        responses = [
            ApiResponse(responseCode = "200", description = "Utilisateurs trouvés."),
            ApiResponse(responseCode = "404", description = "Aucun utilisateur trouvé.")
        ]
    )
    @GetMapping(params = ["nom"])
    @PreAuthorize("hasRole('utilisateur') or hasRole('admin')")
    fun rechercherUtilisateursParNom(@RequestParam nom: String): ResponseEntity<List<Utilisateur>> {
        if (nom.isBlank()) throw NomUtilisateurInvalideException("Le nom ne peut pas être vide.")
        val utilisateurs = utilisateurService.chercherUtilisateurParNom(nom)
        return if (utilisateurs.isNotEmpty())
            ResponseEntity.ok(utilisateurs)
        else
            throw UtilisateurIntrouvableException("Aucun utilisateur trouvé pour le nom fourni.")
    }

    @Operation(
        summary = "Rechercher des utilisateurs par prénom",
        description = "Retourne une liste d'utilisateurs correspondant au prénom spécifié.",
        responses = [
            ApiResponse(responseCode = "200", description = "Utilisateurs trouvés."),
            ApiResponse(responseCode = "404", description = "Aucun utilisateur trouvé.")
        ]
    )
    @GetMapping(params = ["prenom"])
    @PreAuthorize("hasRole('utilisateur') or hasRole('admin')")
    fun rechercherUtilisateursParPrenom(@RequestParam prenom: String): ResponseEntity<List<Utilisateur>> {
        if (prenom.isBlank()) throw PrenomUtilisateurInvalideException("Le prénom ne peut pas être vide.")
        val utilisateurs = utilisateurService.chercherUtilisateurParPrenom(prenom)
        return if (utilisateurs.isNotEmpty())
            ResponseEntity.ok(utilisateurs)
        else
            throw UtilisateurIntrouvableException("Aucun utilisateur trouvé pour le prénom fourni.")
    }

    @Operation(
        summary = "Rechercher un utilisateur par email",
        description = "Retourne les détails d'un utilisateur correspondant à l'email fourni.",
        responses = [
            ApiResponse(responseCode = "200", description = "Utilisateur trouvé."),
            ApiResponse(responseCode = "404", description = "Aucun utilisateur trouvé.")
        ]
    )
    @GetMapping(params = ["email"])
    @PreAuthorize("hasRole('utilisateur') or hasRole('admin')")
    fun rechercherUtilisateurParEmail(@RequestParam email: String): ResponseEntity<Utilisateur> {
        if (email.isBlank()) throw EmailUtilisateurInvalideException("L'email ne peut pas être vide.")
        val utilisateur = utilisateurService.chercherUtilisateurParEmail(email)
            ?: throw UtilisateurIntrouvableException("Aucun utilisateur trouvé pour l'email fourni.")
        return ResponseEntity.ok(utilisateur)
    }

    @Operation(
        summary = "Ajouter un utilisateur",
        description = "Crée un nouvel utilisateur.",
        responses = [
            ApiResponse(responseCode = "201", description = "Utilisateur créé avec succès."),
            ApiResponse(responseCode = "400", description = "Données invalides pour créer l'utilisateur.")
        ]
    )
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    fun ajouterUtilisateur(@RequestBody utilisateur: Utilisateur): ResponseEntity<Utilisateur> {
        val utilisateurCree = utilisateurService.creerUtilisateur(utilisateur)
            ?: throw DonneesUtilisateurInvalidesException("Impossible de créer l'utilisateur. Données invalides.")
        return ResponseEntity.status(HttpStatus.CREATED).body(utilisateurCree)
    }

    @Operation(
        summary = "Modifier un utilisateur par email",
        description = "Met à jour les informations d'un utilisateur identifié par son email.",
        responses = [
            ApiResponse(responseCode = "200", description = "Utilisateur modifié avec succès."),
            ApiResponse(responseCode = "404", description = "Utilisateur introuvable."),
            ApiResponse(responseCode = "400", description = "Données invalides.")
        ]
    )
    @PutMapping("/{email}")
    @PreAuthorize("hasRole('admin')")
    fun modifierUtilisateurParEmail(@PathVariable email: String, @RequestBody utilisateur: Utilisateur): ResponseEntity<Utilisateur> {
        if (email.isBlank()) throw EmailUtilisateurInvalideException("L'email ne peut pas être vide.")
        val utilisateurModifie = utilisateurService.modifierUtilisateurParEmail(email, utilisateur)
            ?: throw UtilisateurIntrouvableException("L'utilisateur avec l'email $email est introuvable.")
        return ResponseEntity.ok(utilisateurModifie)
    }

    @Operation(
        summary = "Supprimer un utilisateur par email",
        description = "Supprime un utilisateur correspondant à l'email fourni.",
        responses = [
            ApiResponse(responseCode = "204", description = "Utilisateur supprimé avec succès."),
            ApiResponse(responseCode = "404", description = "Utilisateur introuvable.")
        ]
    )
    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('admin')")
    fun supprimerUtilisateurParEmail(@PathVariable email: String): ResponseEntity<Void> {
        if (email.isBlank()) throw EmailUtilisateurInvalideException("L'email ne peut pas être vide.")
        val utilisateur = utilisateurService.chercherUtilisateurParEmail(email)
            ?: throw UtilisateurIntrouvableException("L'utilisateur avec l'email $email est introuvable.")
        utilisateurService.effacerUtilisateurParEmail(email)
        return ResponseEntity.noContent().build()
    }
}
