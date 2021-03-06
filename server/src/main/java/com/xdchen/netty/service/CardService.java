package com.xdchen.netty.service;

import com.xdchen.netty.model.Card;
import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CardService implements ICardService {
    @Override
    public List<Card> initCards() {
        List<Card> cards = new ArrayList<>(54);
        for (int i = 1; i < 14; i++) {
            for (int j = 1; j < 5; j++) {
                cards.add(new Card(i + 2, j));
            }
        }
        cards.add(new Card(16, 1));
        cards.add(new Card(16, 2));
        return cards;
    }

    @Override
    public void shuffleCards(List<Card> cards) {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int index1 = random.nextInt(54);
            int index2 = random.nextInt(54);
            Card card1 = cards.get(index1);
            cards.set(index1, cards.get(index2));
            cards.set(index2, card1);
        }
    }

    @Override
    public List<Card> dealCards(Map<String, List<Card>> userCardsMap, List<Card> cards) {
        List<String> users = new ArrayList<>(3);
        for (String username : userCardsMap.keySet()) {
            userCardsMap.get(username).clear();
            users.add(username);
        }
        for (int i = 0; i < 17; i++) {
            for (int j = 0; j < 3; j++) {
                userCardsMap.get(users.get(j)).add(cards.remove(0));
            }
        }
        return cards;
    }
}
