package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes
import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.query
import ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes.*
import ApiVinylparadise.ApiVinylParadise.Domaine.Genre
import org.springframework.jdbc.core.RowMapper
import org.springframework.dao.EmptyResultDataAccessException
import java.sql.ResultSet

@Repository
class GenresDAOImpl(private val jdbcTemplate: JdbcTemplate) : GenreDAO
{

    override fun chercherTous(): List<Genre> =
    jdbcTemplate.query("SELECT id, nom FROM genre") { réponse, _ ->
        Genre(
            id = réponse.getInt("id"),
            nom = réponse.getString("nom")
        )
    }

    fun chercherParId(id: Int): Genre? =
        try {
            jdbcTemplate.queryForObject("SELECT id, nom FROM genre WHERE id = ?", { réponse, _ ->
                Genre(
                    id = réponse.getInt("id"),
                    nom = réponse.getString("nom")
                )
            }, id)
        } catch (e: EmptyResultDataAccessException) {
            null 
        }

    fun chercherGenreParNom(nom: String): List<Genre> =
        jdbcTemplate.query("SELECT id, nom FROM genre WHERE nom LIKE ?", { réponse, _ ->
            Genre(
                id = réponse.getInt("id"),
                nom = réponse.getString("nom")
            )
        }, "%$nom%")

    override fun ajouter(genre: Genre): Genre? {
        val rowsAffected = jdbcTemplate.update("INSERT INTO genre (nom) VALUES (?)", genre.nom)

        return if (rowsAffected > 0) {
            val generatedId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Int::class.java)
            genre.copy(id = generatedId ?: 0)
        } else {
            null
        }
    }

    override fun modifier(id: Int, genre: Genre): Genre? {
        val rowsAffected = jdbcTemplate.update("UPDATE genre SET nom = ? WHERE id = ?", genre.nom, id)

        return if (rowsAffected > 0) {
            genre.copy(id = id)
        } else {
            null
        }
    }

    override fun effacer(id: Int) {
        jdbcTemplate.update("DELETE FROM genre WHERE id = ?", id)
    }
}