package ApiVinylparadise.ApiVinylParadise.Domaine
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.GenresDAOImpl
import ApiVinylparadise.ApiVinylParadise.Controleurs.Exception.*
import org.springframework.stereotype.Service
@Service
class GenresService(private val genresDAO: GenresDAOImpl) {
    fun obtenirTousLesGenres(): List<Genre> =
        genresDAO.chercherTous()

    fun obtenirGenreParId(id: Int): Genre {
        validateId(id)
        return genresDAO.chercherParId(id) ?: throw GenreNotFoundException("Le genre avec l'ID $id n'existe pas.")
    }

    fun obtenirGenresParNom(nom: String): List<Genre> {
        validateName(nom)
        val genres = genresDAO.chercherGenreParNom(nom)
        if (genres.isEmpty()) {
            throw GenreNotFoundException("Aucun genre trouvé avec le nom \"$nom\".")
        }
        return genres
    }

    fun ajouterGenre(nouveauGenre: Genre): Genre {
        validateGenre(nouveauGenre)
        return genresDAO.ajouter(nouveauGenre) ?: throw InvalidGenreDataException("Impossible d'ajouter le genre.")
    }

    fun modifierGenre(id: Int, genreModifie: Genre): Genre {
        validateId(id)
        validateGenre(genreModifie)
        return genresDAO.modifier(id, genreModifie) ?: throw GenreNotFoundException("Le genre avec l'ID $id n'existe pas.")
    }

    fun supprimerGenre(id: Int) {
        validateId(id)
        val genre = genresDAO.chercherParId(id)
            ?: throw GenreNotFoundException("Le genre avec l'ID $id n'existe pas.")
        genresDAO.effacer(id)
    }

    private fun validateId(id: Int) {
        if (id <= 0) {
            throw InvalidIdException("L'ID doit être supérieur à 0.")
        }
    }

    private fun validateName(name: String) {
        if (name.isBlank()) {
            throw InvalidGenreNameException("Le nom du genre ne peut pas être vide.")
        }
    }

    private fun validateGenre(genre: Genre) {
        if (genre.nom.isBlank()) {
            throw InvalidGenreDataException("Le nom du genre ne peut pas être vide.")
        }
    }
}
