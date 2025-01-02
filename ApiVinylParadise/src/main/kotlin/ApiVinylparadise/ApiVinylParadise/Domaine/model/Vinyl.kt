package ApiVinylparadise.ApiVinylParadise.Domaine
import ApiVinylparadise.ApiVinylParadise.Domaine.Artist
import ApiVinylparadise.ApiVinylParadise.Domaine.Utilisateur
import java.sql.Date


data class Vinyl(
    val id: Int?, 
    val utilisateur: Utilisateur,
    val album :Album,
    val prix: Double,
    val description: String?

)
