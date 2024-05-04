package com.example.recipeapp;

import android.content.Context;

import com.example.recipeapp.Listeners.InstructionsListener;
import com.example.recipeapp.Listeners.RandomRecipeResponseListeners;
import com.example.recipeapp.Listeners.RecipeDetailsListener;
import com.example.recipeapp.Listeners.SimilarRecipeListener;
import com.example.recipeapp.Models.InstuctionResponse;
import com.example.recipeapp.Models.RandomRecipeApiResponse;
import com.example.recipeapp.Models.RecipeDetailsResponse;
import com.example.recipeapp.Models.SimilarRecipes;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) {
        this.context = context;
    }

    public void getRandomRecipes(RandomRecipeResponseListeners listeners, List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(context.getString(R.string.api_value), "10", tags);
        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()){
                    listeners.didError(response.message());
                    return;
                }
                listeners.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listeners.didError(t.getMessage());
            }
        });
    }

    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_value));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getSimilarRecipes(SimilarRecipeListener listener, int id){
        CallSimilarRecipes callSimilarRecipes = retrofit.create(CallSimilarRecipes.class);
        Call<List<SimilarRecipes>> call = callSimilarRecipes.callSimilaeRecipe(id, "4", context.getString(R.string.api_value));
        call.enqueue(new Callback<List<SimilarRecipes>>() {
            @Override
            public void onResponse(Call<List<SimilarRecipes>> call, Response<List<SimilarRecipes>> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<SimilarRecipes>> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    public void getInstructions(InstructionsListener listener, int id){
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        Call<List<InstuctionResponse>> call = callInstructions.callInstuctions(id, context.getString(R.string.api_value));
        call.enqueue(new Callback<List<InstuctionResponse>>() {
            @Override
            public void onResponse(Call<List<InstuctionResponse>> call, Response<List<InstuctionResponse>> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<InstuctionResponse>> call, Throwable throwable) {
                listener.didError(throwable.getMessage());
            }
        });
    }

    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("include-tags")List<String> tags
                );
    }

     private interface  CallRecipeDetails{
        @GET("recipes/{id}/information")
         Call<RecipeDetailsResponse> callRecipeDetails(
                 @Path("id") int id,
                 @Query("apiKey") String apiKey
        );
     }

     private interface CallSimilarRecipes{
        @GET("recipes/{id}/similar")
         Call<List<SimilarRecipes>> callSimilaeRecipe(
                 @Path("id") int id,
                 @Query("number") String number,
                 @Query("apiKey") String apiKey
        );
     }

     private interface CallInstructions{
        @GET("recipes/{id}/analyzedInstructions")
         Call<List<InstuctionResponse>> callInstuctions(
                 @Path("id") int id,
                 @Query("apiKey") String apiKey
        );
     }
}
