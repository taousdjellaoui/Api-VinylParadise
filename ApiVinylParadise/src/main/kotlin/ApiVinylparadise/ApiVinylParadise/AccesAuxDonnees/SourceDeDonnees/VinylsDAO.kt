package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes

import ApiVinylparadise.ApiVinylParadise.Domaine.Vinyl

interface VinylsDAO: DAO<Vinyl> {
    override fun chercherTous(): List<Vinyl>
    fun chercherParTitre(titre: String): List<Vinyl>
    fun chercherParArtiste(nomArtist: String): List<Vinyl>
    fun chercherParGenre(nomGenre: String): List<Vinyl>
    override fun ajouter(vinyl: Vinyl): Vinyl?
    override fun modifier(id: Int, vinyl: Vinyl): Vinyl?
    override fun effacer(id: Int)
   
}
