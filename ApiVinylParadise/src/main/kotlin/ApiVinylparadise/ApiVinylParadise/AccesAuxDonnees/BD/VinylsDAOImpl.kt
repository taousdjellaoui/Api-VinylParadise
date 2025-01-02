package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes

import org.springframework.stereotype.Repository
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.dao.EmptyResultDataAccessException
import ApiVinylparadise.ApiVinylParadise.Domaine.Vinyl
import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur
import ApiVinylparadise.ApiVinylParadise.Domaine.Genre
import ApiVinylparadise.ApiVinylParadise.Domaine.Artist
import ApiVinylparadise.ApiVinylParadise.Domaine.Album

@Repository
class VinylsDAOImpl(private val bd: JdbcTemplate) : VinylsDAO {

    
    override fun chercherTous(): List<Vinyl> {
        val sql = """
            SELECT v.id, v.prix, v.description,
                   al.id AS album_id, al.titre AS album_titre, al.date_sortie AS album_date_sortie, al.url_image AS album_image,
                   ar.id AS artiste_id, ar.nom AS artiste_nom,
                   u.email AS utilisateur_email, u.nom AS utilisateur_nom, u.prénom AS utilisateur_prenom
            FROM vinyles v
            JOIN albums al ON v.album_id = al.id
            JOIN artiste ar ON al.artist_id = ar.id
            JOIN utilisateur u ON v.utilisateur_email = u.email
        """
        return bd.query(sql) { rs, _ ->
            Vinyl(
                id = rs.getInt("id"),
                prix = rs.getDouble("prix"),
                description = rs.getString("description"),
                album = Album(
                    id = rs.getInt("album_id"),
                    titre = rs.getString("album_titre"),
                    dateDeSortie = rs.getDate("album_date_sortie"),
                    image_url = rs.getString("album_image"),
                    artist = Artist(
                        id = rs.getInt("artiste_id"),
                        nom = rs.getString("artiste_nom")
                    ),
                    genres = chercherGenresPourAlbum(rs.getInt("album_id"))
                ),
                utilisateur = Utilisateur(
                    email = rs.getString("utilisateur_email"),
                    nom = rs.getString("utilisateur_nom"),
                    prenom = rs.getString("utilisateur_prenom")
                )
            )
        }
    }

    private fun chercherGenresPourAlbum(albumId: Int): List<Genre> {
        val sql = """
            SELECT g.id, g.nom
            FROM genre g
            JOIN albums_genre ag ON g.id = ag.genre_id
            WHERE ag.albums_id = ?
        """
        return bd.query(sql, arrayOf(albumId)) { rs, _ ->
            Genre(
                id = rs.getInt("id"),
                nom = rs.getString("nom")
            )
        }
    }

    
    override fun ajouter(vinyl: Vinyl): Vinyl? {
        val rowsAffected = bd.update(
            """
                INSERT INTO vinyles (utilisateur_email, album_id, prix, description)
                VALUES (?, ?, ?, ?)
            """,
            vinyl.utilisateur.email, vinyl.album.id, vinyl.prix, vinyl.description
        )
        return if (rowsAffected > 0) vinyl else null
    }

   
    override fun modifier(id: Int, vinyl: Vinyl): Vinyl? {
        val rowsAffected = bd.update(
            """
                UPDATE vinyles
                SET utilisateur_email = ?, album_id = ?, prix = ?, description = ?
                WHERE id = ?
            """,
            vinyl.utilisateur.email, vinyl.album.id, vinyl.prix, vinyl.description, id
        )
        return if (rowsAffected > 0) vinyl else null
    }

    
    override fun effacer(id: Int) {
        bd.update("DELETE FROM vinyles WHERE id = ?", id)
    }

    
    override fun chercherParTitre(titre: String): List<Vinyl> {
        val sql = """
            SELECT v.id, v.prix, v.description,
                   al.id AS album_id, al.titre AS album_titre, al.date_sortie AS album_date_sortie, al.url_image AS album_image,
                   ar.id AS artiste_id, ar.nom AS artiste_nom,
                   u.email AS utilisateur_email, u.nom AS utilisateur_nom, u.prénom AS utilisateur_prenom
            FROM vinyles v
            JOIN albums al ON v.album_id = al.id
            JOIN artiste ar ON al.artist_id = ar.id
            JOIN utilisateur u ON v.utilisateur_email = u.email
            WHERE al.titre LIKE ?
        """
        return bd.query(sql, arrayOf("%$titre%")) { rs, _ ->
            Vinyl(
                id = rs.getInt("id"),
                prix = rs.getDouble("prix"),
                description = rs.getString("description"),
                album = Album(
                    id = rs.getInt("album_id"),
                    titre = rs.getString("album_titre"),
                    dateDeSortie = rs.getDate("album_date_sortie"),
                    image_url = rs.getString("album_image"),
                    artist = Artist(
                        id = rs.getInt("artiste_id"),
                        nom = rs.getString("artiste_nom")
                    ),
                    genres = chercherGenresPourAlbum(rs.getInt("album_id"))
                ),
                utilisateur = Utilisateur(
                    email = rs.getString("utilisateur_email"),
                    nom = rs.getString("utilisateur_nom"),
                    prenom = rs.getString("utilisateur_prenom")
                )
            )
        }
    }

 
    override fun chercherParArtiste(nomArtiste: String): List<Vinyl> {
        val sql = """
            SELECT v.id, v.prix, v.description,
                   al.id AS album_id, al.titre AS album_titre, al.date_sortie AS album_date_sortie, al.url_image AS album_image,
                   ar.id AS artiste_id, ar.nom AS artiste_nom,
                   u.email AS utilisateur_email, u.nom AS utilisateur_nom, u.prénom AS utilisateur_prenom
            FROM vinyles v
            JOIN albums al ON v.album_id = al.id
            JOIN artiste ar ON al.artist_id = ar.id
            JOIN utilisateur u ON v.utilisateur_email = u.email
            WHERE ar.nom LIKE ?
        """
        return bd.query(sql, arrayOf("%$nomArtiste%")) { rs, _ ->
            Vinyl(
                id = rs.getInt("id"),
                prix = rs.getDouble("prix"),
                description = rs.getString("description"),
                album = Album(
                    id = rs.getInt("album_id"),
                    titre = rs.getString("album_titre"),
                    dateDeSortie = rs.getDate("album_date_sortie"),
                    image_url = rs.getString("album_image"),
                    artist = Artist(
                        id = rs.getInt("artiste_id"),
                        nom = rs.getString("artiste_nom")
                    ),
                    genres = chercherGenresPourAlbum(rs.getInt("album_id"))
                ),
                utilisateur = Utilisateur(
                    email = rs.getString("utilisateur_email"),
                    nom = rs.getString("utilisateur_nom"),
                    prenom = rs.getString("utilisateur_prenom")
                )
            )
        }
    }
     override fun chercherParGenre(nomGenre: String): List<Vinyl> {
        val sql = """
            SELECT v.id, v.prix, v.description,
                   al.id AS album_id, al.titre AS album_titre, al.date_sortie AS album_date_sortie, al.url_image AS album_image,
                   ar.id AS artiste_id, ar.nom AS artiste_nom,
                   u.email AS utilisateur_email, u.nom AS utilisateur_nom, u.prénom AS utilisateur_prenom,
                   g.id AS genre_id, g.nom AS genre_nom
            FROM vinyles v
            JOIN albums al ON v.album_id = al.id
            JOIN artiste ar ON al.artist_id = ar.id
            JOIN utilisateur u ON v.utilisateur_email = u.email
            JOIN albums_genre ag ON al.id = ag.albums_id
            JOIN genre g ON ag.genre_id = g.id
            WHERE g.nom LIKE ?
        """
        return bd.query(sql, arrayOf("%$nomGenre%")) { rs, _ ->
            Vinyl(
                id = rs.getInt("id"),
                prix = rs.getDouble("prix"),
                description = rs.getString("description"),
                album = Album(
                    id = rs.getInt("album_id"),
                    titre = rs.getString("album_titre"),
                    dateDeSortie = rs.getDate("album_date_sortie"),
                    image_url = rs.getString("album_image"),
                    artist = Artist(
                        id = rs.getInt("artiste_id"),
                        nom = rs.getString("artiste_nom")
                    ),
                    genres = chercherGenresPourAlbum(rs.getInt("album_id"))
                ),
                utilisateur = Utilisateur(
                    email = rs.getString("utilisateur_email"),
                    nom = rs.getString("utilisateur_nom"),
                    prenom = rs.getString("utilisateur_prenom")
                )
            )
        }
    
    }
    
    fun ajouterAlbum(album: Album): Album? {
        val rowsAffected = bd.update(
            """
                INSERT INTO albums (titre, date_sortie, artist_id, url_image)
                VALUES (?, ?, ?, ?)
            """,
            album.titre, album.dateDeSortie, album.artist.id, album.image_url
        )
    
        if (rowsAffected > 0) {
            // Retrieve the ID of the newly inserted album
            val generatedId = bd.queryForObject("SELECT LAST_INSERT_ID()", Int::class.java)
            if (generatedId == null) {
              
                return null
            }
    
          
    
            val albumWithId = album.copy(id = generatedId)
    
            // Add relations to the albums_genre table
            album.genres.forEach { genre ->
                if (genre.id != null) {
                   
                    ajouterRelationAlbumGenre(albumWithId.id!!, genre.id!!)
                } else {
                 
                }
            }
    
            return albumWithId
        } 
    
        return null
    }
    fun chercherAlbumParTitre(titre: String): Album? {
        val sql = """
            SELECT id, titre, date_sortie, artist_id, url_image
            FROM albums
            WHERE titre = ?
        """
        return try {
            bd.queryForObject(sql, arrayOf(titre)) { rs, _ ->
                Album(
                    id = rs.getInt("id"),
                    titre = rs.getString("titre"),
                    dateDeSortie = rs.getDate("date_sortie"),
                    image_url = rs.getString("url_image"),
                    artist = Artist(id = rs.getInt("artist_id"), nom = "") 
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null
        }
    }
    private fun ajouterRelationAlbumGenre(albumId: Int, genreId: Int) {
        bd.update(
            """
                INSERT INTO albums_genre (albums_id, genre_id)
                VALUES (?, ?)
            """,
            albumId, genreId
        )
    }
    fun trouverVinylParId(id: Int): Vinyl? {
        val sql = """
            SELECT v.id, v.prix, v.description,
                   al.id AS album_id, al.titre AS album_titre, al.date_sortie AS album_date_sortie, al.url_image AS album_image,
                   ar.id AS artiste_id, ar.nom AS artiste_nom,
                   u.email AS utilisateur_email, u.nom AS utilisateur_nom, u.prénom AS utilisateur_prenom
            FROM vinyles v
            JOIN albums al ON v.album_id = al.id
            JOIN artiste ar ON al.artist_id = ar.id
            JOIN utilisateur u ON v.utilisateur_email = u.email
            WHERE v.id = ?
        """
        return try {
            bd.queryForObject(sql, arrayOf(id)) { rs, _ ->
                Vinyl(
                    id = rs.getInt("id"),
                    prix = rs.getDouble("prix"),
                    description = rs.getString("description"),
                    album = Album(
                        id = rs.getInt("album_id"),
                        titre = rs.getString("album_titre"),
                        dateDeSortie = rs.getDate("album_date_sortie"),
                        image_url = rs.getString("album_image"),
                        artist = Artist(
                            id = rs.getInt("artiste_id"),
                            nom = rs.getString("artiste_nom")
                        ),
                        genres = chercherGenresPourAlbum(rs.getInt("album_id"))
                    ),
                    utilisateur = Utilisateur(
                        email = rs.getString("utilisateur_email"),
                        nom = rs.getString("utilisateur_nom"),
                        prenom = rs.getString("utilisateur_prenom")
                    )
                )
            }
        } catch (e: EmptyResultDataAccessException) {
            null // Return null if no result is found
        }
    }
    
}
