package my.dev.recipeapp_testdeveloper

class Recipe {
    var id: Int = 0
    var recipeTypeId: Int = 0
    var name: String = ""
    var imageURL: String = ""
    var ingredients: String = ""
    var steps: String = ""

    constructor(id: Int, recipeTypeId: Int, name: String, imageURL: String, ingredients: String, steps: String) {
        this.id = id
        this.recipeTypeId = recipeTypeId
        this.name = name
        this.imageURL = imageURL
        this.ingredients = ingredients
        this.steps = steps
    }
}

class RecipeType {
    var id: Int = 0
    var name: String = ""

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }
}