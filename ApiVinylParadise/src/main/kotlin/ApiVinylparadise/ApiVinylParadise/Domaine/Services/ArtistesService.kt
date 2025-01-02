package ApiVinylparadise.ApiVinylParadise.Domaine
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.ArtistesDAOImpl
import org.springframework.stereotype.Service
@Service
class ArtistesService(private val artistesDAO: ArtistesDAOImpl) {

    
    fun rechercheArtistes(): List<Artist> {
        return artistesDAO.chercherTous()
    }

    
    fun rechercheArtisteParId(id: Int): Artist? {
        return artistesDAO.chercherParId(id)
    }

   
    fun rechercheArtistesParNom(nom: String): List<Artist> {
        return artistesDAO.chercherArtistParNom(nom)
    }

    
    fun ajouterArtiste(artist: Artist): Artist? {
        return artistesDAO.ajouter(artist)
    }

    
    fun modifierArtiste(id: Int, artist: Artist): Artist? {
        return artistesDAO.modifier(id, artist)
    }

   
    fun supprimerArtiste(id: Int) {
        artistesDAO.effacer(id)
    }
}