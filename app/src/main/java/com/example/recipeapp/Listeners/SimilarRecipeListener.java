package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.SimilarRecipes;

import java.util.List;

public interface SimilarRecipeListener {
    void didFetch(List<SimilarRecipes> recipes, String message);
    void didError(String message);
}
