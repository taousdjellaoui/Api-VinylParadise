package ApiVinylparadise.ApiVinylParadise.AccesAuxdonnes

interface DAO<T> {
    fun chercherTous(): List<T>
    fun ajouter(element: T): T?
    fun modifier(id: Int, element: T): T?
    fun effacer(id: Int)
}
