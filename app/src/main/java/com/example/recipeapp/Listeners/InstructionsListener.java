package com.example.recipeapp.Listeners;

import com.example.recipeapp.Models.InstuctionResponse;

import java.util.List;

public interface InstructionsListener {
    void didFetch(List<InstuctionResponse> response, String message);
    void didError(String message);
}
