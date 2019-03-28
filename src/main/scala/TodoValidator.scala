object TodoValidator {
  def validate(createTodo: CreateTodo): Boolean = {
    !createTodo.title.isEmpty
  }
}
