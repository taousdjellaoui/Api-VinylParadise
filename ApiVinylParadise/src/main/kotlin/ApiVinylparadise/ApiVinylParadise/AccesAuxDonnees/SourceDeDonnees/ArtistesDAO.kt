package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes
import ApiVinylparadise.ApiVinylParadise.Domaine.Artist

interface ArtistesDAO: DAO<Artist> {
    override fun chercherTous(): List<Artist>
    override fun ajouter(artist: Artist): Artist?
    override fun modifier(id: Int, artist: Artist): Artist?
    override fun effacer(id: Int)
}
