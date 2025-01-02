package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes
import ApiVinylparadise.ApiVinylParadise.Domaine.Genre

interface GenreDAO: DAO<Genre> {
    override fun chercherTous(): List<Genre>
    override fun ajouter(genre: Genre): Genre?
    override fun modifier(id: Int, genre: Genre): Genre?
    override fun effacer(id: Int)
}