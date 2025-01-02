package ApiVinylparadise.ApiVinylParadise.Controleurs.Exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidVinylException(message: String) : RuntimeException(message)


@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidIdException(message: String) : RuntimeException(message)


@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidTitleException(message: String) : RuntimeException(message)
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.NOT_FOUND)
class UtilisateurIntrouvableException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DonneesUtilisateurInvalidesException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class NomUtilisateurInvalideException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class PrenomUtilisateurInvalideException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EmailUtilisateurInvalideException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidGenreDataException(message: String) : RuntimeException(message)
@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidGenreNameException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class GenreNotFoundException(message: String) : RuntimeException(message)
@ResponseStatus(HttpStatus.BAD_REQUEST)
class DonnéeExistanteException(message: String) : RuntimeException(message)