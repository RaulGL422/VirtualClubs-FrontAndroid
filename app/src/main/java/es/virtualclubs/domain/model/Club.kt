package es.virtualclubs.domain.model

data class Club(
  val id: String,
  val name: String,
  val sport: String,
  val memberCount: Int
)
