package ApiVinylparadise.ApiVinylParadise.Controleurs.Exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.InvalidGenreDataException

@ControllerAdvice
class GestionnaireExceptions {

    
   
     @ExceptionHandler(InvalidIdException::class)
     fun handleInvalidIdException(ex: InvalidIdException): ResponseEntity<ReponseErreur> {
         return ResponseEntity(
             ReponseErreur(
                 horodatage = LocalDateTime.now(),
                 statut = HttpStatus.BAD_REQUEST.value(),
                 erreur = "ID invalide",
                 message = ex.message ?: "L'ID fourni est invalide."
             ),
             HttpStatus.BAD_REQUEST
         )
     }
 

     @ExceptionHandler(InvalidVinylException::class)
     fun handleInvalidVinylException(ex: InvalidVinylException): ResponseEntity<ReponseErreur> {
         return ResponseEntity(
             ReponseErreur(
                 horodatage = LocalDateTime.now(),
                 statut = HttpStatus.BAD_REQUEST.value(),
                 erreur = "Vinyle invalide",
                 message = ex.message ?: "Les données du vinyle sont invalides."
             ),
             HttpStatus.BAD_REQUEST
         )
     }
 
     
     @ExceptionHandler(ResourceNotFoundException::class)
     fun handleResourceNotFoundException(ex: ResourceNotFoundException): ResponseEntity<ReponseErreur> {
         return ResponseEntity(
             ReponseErreur(
                 horodatage = LocalDateTime.now(),
                 statut = HttpStatus.NOT_FOUND.value(),
                 erreur = "Ressource introuvable",
                 message = ex.message ?: "La ressource demandée est introuvable."
             ),
             HttpStatus.NOT_FOUND
         )
     }
 
     
     @ExceptionHandler(Exception::class)
     fun handleGenericException(ex: Exception): ResponseEntity<ReponseErreur> {
         return ResponseEntity(
             ReponseErreur(
                 horodatage = LocalDateTime.now(),
                 statut = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                 erreur = "Erreur interne",
                 message = ex.message ?: "Une erreur inattendue s'est produite."
             ),
             HttpStatus.INTERNAL_SERVER_ERROR
         )
     }

     // Gestionnaire pour GenreNotFoundException
    @ExceptionHandler(GenreNotFoundException::class)
    fun handleGenreNotFoundException(ex: GenreNotFoundException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.NOT_FOUND.value(),
                erreur = "Genre introuvable",
                message = ex.message ?: "Le genre demandé est introuvable."
            ),
            HttpStatus.NOT_FOUND
        )
    }

    // Gestionnaire pour InvalidGenreNameException
    @ExceptionHandler(InvalidGenreNameException::class)
    fun handleInvalidGenreNameException(ex: InvalidGenreNameException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Nom de genre invalide",
                message = ex.message ?: "Le nom du genre est invalide."
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    // Gestionnaire pour InvalidGenreDataException
    @ExceptionHandler(InvalidGenreDataException::class)
    fun handleInvalidGenreDataException(ex: InvalidGenreDataException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Données de genre invalides",
                message = ex.message ?: "Les données fournies pour le genre sont invalides."
            ),
            HttpStatus.BAD_REQUEST
        )
    }



@ExceptionHandler(UtilisateurIntrouvableException::class)
    fun handleUtilisateurIntrouvableException(ex: UtilisateurIntrouvableException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.NOT_FOUND.value(),
                erreur = "Utilisateur introuvable",
                message = ex.message ?: "L'utilisateur demandé est introuvable."
            ),
            HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(DonneesUtilisateurInvalidesException::class)
    fun handleDonneesUtilisateurInvalidesException(ex: DonneesUtilisateurInvalidesException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Données utilisateur invalides",
                message = ex.message ?: "Les données fournies pour l'utilisateur sont invalides."
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(NomUtilisateurInvalideException::class)
    fun handleNomUtilisateurInvalideException(ex: NomUtilisateurInvalideException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Nom invalide",
                message = ex.message ?: "Le nom de l'utilisateur est invalide."
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(PrenomUtilisateurInvalideException::class)
    fun handlePrenomUtilisateurInvalideException(ex: PrenomUtilisateurInvalideException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Prénom invalide",
                message = ex.message ?: "Le prénom de l'utilisateur est invalide."
            ),
            HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(EmailUtilisateurInvalideException::class)
    fun handleEmailUtilisateurInvalideException(ex: EmailUtilisateurInvalideException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Email invalide",
                message = ex.message ?: "L'email de l'utilisateur est invalide."
            ),
            HttpStatus.BAD_REQUEST
        )
    }
    @ExceptionHandler(DonnéeExistanteException::class)
    fun handleDonnéeExistanteException(ex: DonnéeExistanteException): ResponseEntity<ReponseErreur> {
        return ResponseEntity(
            ReponseErreur(
                horodatage = LocalDateTime.now(),
                statut = HttpStatus.BAD_REQUEST.value(),
                erreur = "Resource Existant",
                message = ex.message ?: "Laressource existe déja."
            ),
            HttpStatus.BAD_REQUEST
        )
    }


}
data class ReponseErreur(
    val horodatage: LocalDateTime,
    val statut: Int,
    val erreur: String,
    val message: String
)
