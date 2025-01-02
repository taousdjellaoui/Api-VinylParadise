package ApiVinylparadise.ApiVinylParadise.Domaine

import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.VinylsDAO
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.VinylsDAOImpl
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.ArtistesDAOImpl
import ApiVinylparadise.ApiVinylParadise.AccesAuxDonnees.BD.UtilisateurDAOImpl
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.InvalidVinylException
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.InvalidTitleException
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VinylService(private val vinylsDAO: VinylsDAOImpl,
                   private val artistesDAO: ArtistesDAOImpl,
                   private val utlisateurDAO: UtilisateurDAOImpl
                   ) {

    
    fun rechercherToutVinyls(): List<Vinyl> {
        val vinyls = vinylsDAO.chercherTous()
        if (vinyls.isEmpty()) {
            throw ResourceNotFoundException("Aucun vinyle trouvé dans la base de données.")
        }
        return vinyls
    }

   
    @Transactional
    fun ajoutVinyl(vinyl: Vinyl): Vinyl {
        validerVinyl(vinyl)
    
        val artist = artistesDAO.chercherArtistParNom(vinyl.album.artist.nom).firstOrNull()
            ?: artistesDAO.ajouter(vinyl.album.artist)
            ?: throw InvalidVinylException("Impossible de créer l'artiste associé.")
    
        val utilisateurExistant = utlisateurDAO.chercherParEmail(vinyl.utilisateur.email)
        if (utilisateurExistant == null) {
            utlisateurDAO.ajouter(vinyl.utilisateur)
                ?: throw InvalidVinylException("Impossible de créer l'utilisateur associé.")
        }
        val album = vinylsDAO.chercherAlbumParTitre(vinyl.album.titre)
        ?: vinylsDAO.ajouterAlbum(vinyl.album.copy(artist = artist))
        ?: throw InvalidVinylException("aucun album associé a ce vinyl est trouvé")
    
    if (album.id == null) {
        throw InvalidVinylException("erreur: l'id du album est null")
    }
    
   
    
        
        val vinylAvecRelations = vinyl.copy(album = album)
        return vinylsDAO.ajouter(vinylAvecRelations)
            ?: throw InvalidVinylException("Impossible d'ajouter le vinyle.")
    }
    fun modifierVinyl(id: Int, vinyl: Vinyl): Vinyl {
        validerVinyl(vinyl)
        val vinylExistant = vinylsDAO.chercherTous().find { it.id == id }
            ?: throw ResourceNotFoundException("Le vinyle avec l'ID $id n'existe pas.")
        return vinylsDAO.modifier(id, vinyl)
            ?: throw ResourceNotFoundException("Impossible de mettre à jour le vinyle.")
    }

   
    fun supprimerVinyl(id: Int) {
        val vinylExists = vinylsDAO.chercherTous().any { it.id == id }
        if (!vinylExists) {
            throw ResourceNotFoundException("Impossible de supprimer : le vinyle avec l'ID $id n'existe pas.")
        }
        vinylsDAO.effacer(id)
    }

    
    fun trouverParTitreAlbum(titreAlbum: String): List<Vinyl> {
        validerTitreAlbum(titreAlbum)
        val vinyls = vinylsDAO.chercherParTitre(titreAlbum)
        if (vinyls.isEmpty()) {
            throw ResourceNotFoundException("Aucun vinyle trouvé pour l'album avec le titre \"$titreAlbum\".")
        }
        return vinyls
    }

    
    fun trouverParArtist(artistNom: String): List<Vinyl> {
        if (artistNom.isBlank()) {
            throw InvalidVinylException("Le nom de l'artiste ne peut pas être vide.")
        }
        val vinyls = vinylsDAO.chercherParArtiste(artistNom)
        if (vinyls.isEmpty()) {
            throw ResourceNotFoundException("Aucun vinyle trouvé pour l'artiste \"$artistNom\".")
        }
        return vinyls
    }

    
    private fun validerVinyl(vinyl: Vinyl) {
        if (vinyl.album.titre.isBlank()) {
            throw InvalidVinylException("Le titre de l'album ne peut pas être vide.")
        }
        if (vinyl.prix <= 0) {
            throw InvalidVinylException("Le prix doit être supérieur à 0.")
        }
    
        if (vinyl.utilisateur.email.isBlank()) {
            throw InvalidVinylException("Le vinyle doit être associé à un utilisateur valide.")
        }
    }

    
    private fun validerTitreAlbum(titre: String) {
        if (titre.isBlank()) {
            throw InvalidTitleException("Le titre de l'album ne peut pas être vide.")
        }
    }
    fun trouverParGenre(nomGenre: String): List<Vinyl> {
        if (nomGenre.isBlank()) {
            throw InvalidVinylException("Le nom du genre ne peut pas être vide.")
        }
    
        val vinyls = vinylsDAO.chercherParGenre(nomGenre)
        if (vinyls.isEmpty()) {
            throw ResourceNotFoundException("Aucun vinyle trouvé pour le genre \"$nomGenre\".")
        }
        return vinyls
    }
    fun trouverVinylParId(id: Int): Vinyl {
        return vinylsDAO.trouverVinylParId(id)
            ?: throw ResourceNotFoundException("Le vinyle avec l'ID $id n'existe pas.")
    }
}
